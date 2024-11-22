package main.client;

import main.CloseUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import static main.LogUtils.log;

public class Client {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private final MessageReceiver messageReceiver;
    private boolean closed = false;

    public Client() throws IOException {
        socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 45678), 1000);
        inputStream = new DataInputStream(socket.getInputStream());
        outputStream = new DataOutputStream(socket.getOutputStream());
        messageReceiver = new MessageReceiver(this, inputStream);
    }

    public void start() {
        Thread inputThread = new Thread(messageReceiver, "clientInput");
        inputThread.start();

        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                String input = scanner.nextLine();
                outputStream.writeUTF(input);
                if (input.equals("/exit")) {
                    inputThread.interrupt();
                    CloseUtils.closeAll(socket, inputStream, outputStream);
                    return;
                }
            }
        } catch (IOException e) {
            log(e.getMessage());
        } finally {
            close();
        }
    }

    public synchronized void close() {
        if (closed) return;
        closed = true;
        try {
            System.in.close();
        } catch (IOException e) {
            log(e.getMessage());
        }
        CloseUtils.closeAll(socket, inputStream, outputStream);
    }
}
