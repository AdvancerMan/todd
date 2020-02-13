package com.company.todd.networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// FIXME turn all exceptions to ToddException
public final class Client implements Disposable {
    private Socket socket;
    private Thread acceptingThread;
    private final Array<String> outputBuffer;

    public Client() {
        outputBuffer = new Array<>();
    }

    private Thread createThread(final BufferedReader reader) {
        acceptingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String message;
                    try {
                        message = reader.readLine();
                    } catch (IOException e) {
                        // TODO log somewhere
                        e.printStackTrace();
                        continue;
                    }

                    synchronized (outputBuffer) {
                        outputBuffer.add(message);
                    }
                }
            }
        });
        return acceptingThread;
    }

    private void interruptThread() {
        acceptingThread.interrupt();
        acceptingThread = null;
    }

    public void connect(String host, int port, SocketHints hints) {
        if (socket != null) {
            throw new IllegalStateException("Connecting to a new server when already connected to another");
        }
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, host, port, hints);
        createThread(new BufferedReader(new InputStreamReader(socket.getInputStream())))
                .start();
    }

    public void disconnect() {
        if (socket == null) {
            throw new IllegalStateException("Disconnecting but is not connected to any server");
        }
        socket.dispose();
        socket = null;
        interruptThread();
    }

    /**
     * @param message must not contain '\n'
     */
    public void send(String message) {
        if (socket == null) {
            throw new IllegalStateException("Sending message but is not connected to any server");
        }
        if (message.contains("\n")) {
            throw new IllegalArgumentException("Sending message should not contain '\\n'");
        }

        try {
            socket.getOutputStream().write(message.getBytes());
        } catch (IOException e) {
            // TODO log it
            e.printStackTrace();
        }
    }

    // FIXME reconnecting to other server can change output???
    public Array<String> getOutput() {
        Array<String> ans;
        synchronized (outputBuffer) {
            ans = new Array<>(outputBuffer);
            outputBuffer.clear();
        }
        return ans;
    }

    @Override
    public void dispose() {
        if (socket != null) {
            disconnect();
        }
    }
}
