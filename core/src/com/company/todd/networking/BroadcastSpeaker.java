package com.company.todd.networking;

import com.badlogic.gdx.utils.Disposable;
import com.company.todd.util.Pair;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Note that packet sizes are 1024 bytes
 */
// FIXME turn all exceptions to ToddException
public final class BroadcastSpeaker implements Disposable {
    private static final int BUFFER_SIZE = 1024;
    private static final int DEFAULT_PORT = 7777;
    // FIXME change marker
    public static final String SPEAKER_MARKER = "::com.company.todd.networking.SPEAKER_MARKER::";
    public static final String END_OF_MESSAGE_MARKER = "::END_OF_MARKER::";
    public static final String ANSWER_MARKER = "::ANSWER::";

    private Thread broadcastChannelListeningThread;
    private Function<Pair<String, String>, Pair<String, String>> broadcastChannelListener;
    private MulticastSocket socket;
    private InetAddress group;
    private byte[] buffer;
    private int port;

    public BroadcastSpeaker(InetAddress group, int port) {
        this.port = port;
        try {
            socket = new MulticastSocket(port);
            socket.joinGroup(group);
        } catch (IOException e) {
            throw new IllegalStateException("Unexpected IOException connecting to address " + group, e);
        }
        this.group = group;
        buffer = new byte[BUFFER_SIZE];

        broadcastChannelListeningThread = createThread();
    }

    public BroadcastSpeaker(InetAddress group) {
        this(group, DEFAULT_PORT);
    }

    public BroadcastSpeaker(String address, int port) throws UnknownHostException {
        this(InetAddress.getByName(address), port);
    }

    public BroadcastSpeaker(String address) throws UnknownHostException {
        this(address, DEFAULT_PORT);
    }

    private Thread createThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Pair<String, String> ans = getListener().apply(receive());
                    if (ans != null) {
                        answer(ans.first, ans.second);
                    }
                }
            }
        });
    }

    private Function<Pair<String, String>, Pair<String, String>> getListener() {
        return broadcastChannelListener;
    }

    public void setListener(Function<Pair<String, String>, Pair<String, String>> listener) {
        if (broadcastChannelListeningThread.isAlive()) {
            // FIXME change exception to ToddException
            throw new IllegalStateException("trying to set listener while thread is running");
        }
        broadcastChannelListener = listener;
    }

    public void startListening() {
        if (broadcastChannelListeningThread.isAlive()) {
            // FIXME change exception to ToddException
            throw new IllegalStateException("trying to start listening while thread is running");
        }
        broadcastChannelListeningThread.start();
    }

    private String packMessage(String message, String marker) {
        return SPEAKER_MARKER + marker + END_OF_MESSAGE_MARKER + message;
    }

    private Pair<String, String> unpackMessage(String markedMessage) {
        int markerStartIndex = SPEAKER_MARKER.length();
        int markerEndIndex = markedMessage.indexOf(END_OF_MESSAGE_MARKER);
        int messageStartIndex = markerEndIndex + END_OF_MESSAGE_MARKER.length();
        if (!markedMessage.startsWith(SPEAKER_MARKER) || markerEndIndex == -1) {
            return Pair.of(null, null);
        }
        return Pair.of(
                markedMessage.substring(messageStartIndex),
                markedMessage.substring(markerStartIndex, markerEndIndex)
        );
    }

    public void send(String message, String marker) {
        byte[] bytes = packMessage(message, marker).getBytes();
        try {
            socket.send(new DatagramPacket(bytes, bytes.length, group, port));
        } catch (IOException e) {
            // TODO log it
            e.printStackTrace();
        }
    }

    public Pair<String, String> receive() {
        Pair<String, String> ans = Pair.of(null, null);
        while (ans.first == null) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                // TODO log it
                e.printStackTrace();
            }
            ans = unpackMessage(new String(Arrays.copyOf(packet.getData(), packet.getLength())));
        }
        return ans;
    }

    public String receiveMarked(String marker) {
        Pair<String, String> ans;
        do {
            ans = receive();
        } while (!marker.equals(ans.second));
        return ans.first;
    }

    private String ask(String message, String marker) {
        send(message, marker);
        return receiveMarked(ANSWER_MARKER + marker);
    }

    private void answer(String message, String marker) {
        send(message, ANSWER_MARKER + marker);
    }

    @Override
    public void dispose() {
        broadcastChannelListeningThread.interrupt();
        socket.close();
    }
}
