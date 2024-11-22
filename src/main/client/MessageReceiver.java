package main.client;

import main.CloseUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static main.LogUtils.log;

public class MessageReceiver implements Runnable {
    private final Client client;
    private final DataInputStream inputStream;

    public MessageReceiver(Client client, DataInputStream inputStream) {
        this.client = client;
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String s = inputStream.readUTF();
                System.out.println(s);
            }
        } catch (IOException e) {
            log(e.getMessage());
        }finally {
            client.close();
        }

    }
}
