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
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 45678), 1000);
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        try {
            MessageReceiver messageReceiver = new MessageReceiver(socket, inputStream, outputStream);
            Thread inputThread = new Thread(messageReceiver, "clientInput");
            inputThread.start();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                String input = scanner.nextLine();
                try {
                    outputStream.writeUTF(input);
                    if(input.equals("/exit")){
                        inputThread.interrupt();
                        CloseUtils.closeAll(socket, inputStream, outputStream);
                        return;
                    }
                } catch (IOException e) {
                    log(e.getMessage());
                    CloseUtils.closeAll(socket, inputStream, outputStream);
                    return;
                }
            }

        } catch (IOException e) {
            log(e.getMessage());
        }
    }
}
