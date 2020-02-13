package com.company.todd.networking;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// TODO learn more about multithreading and improve this
// FIXME turn all exceptions to ToddException
public final class Server implements Disposable {
    private SocketHints hints;
    private ServerSocket socket;
    private final Array<Socket> clients;
    private final Array<Thread> acceptingThreads;

    private final Array<String> outputBuffer;

    public Server(String address, int port, ServerSocketHints hints) {
        socket = Gdx.net.newServerSocket(Net.Protocol.TCP, address, port, hints);
        this.hints = new SocketHints();
        clients = new Array<>();
        acceptingThreads = new Array<>();
        outputBuffer = new Array<>();
    }

    private Thread createThread(final BufferedReader reader) {
        Thread thread = new Thread(new Runnable() {
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
        acceptingThreads.add(thread);
        return thread;
    }

    private void stop() {
        for (Thread thread : acceptingThreads) {
            thread.interrupt();
        }
        acceptingThreads.clear();
    }

    public void acceptClient() {
        // TODO check timeout?
        Socket socket = this.socket.accept(hints);
        clients.add(socket);

        createThread(new BufferedReader(new InputStreamReader(socket.getInputStream())))
                .start();
    }

    /**
     * @param message must not contain '\n'
     */
    public void sendAll(String message) {
        if (message.contains("\n")) {
            throw new IllegalArgumentException("Sending message should not contain '\\n'");
        }

        byte[] bytes = message.getBytes();
        for (Socket client : clients) {
            try {
                client.getOutputStream().write(bytes);
            } catch (IOException e) {
                // TODO log it
                e.printStackTrace();
            }
        }
    }

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
        stop();
        for (Socket client : clients) {
            client.dispose();
        }
        socket.dispose();
    }
}
