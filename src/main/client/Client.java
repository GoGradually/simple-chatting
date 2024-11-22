package main.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 45678);
             DataInputStream inputStream = new DataInputStream(socket.getInputStream());
             DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream())
        ) {
            ClientInput clientInput = new ClientInput(socket, inputStream, outputStream);
            ClientOutput clientOutput = new ClientOutput(socket, inputStream, outputStream);
            Thread inputThread = new Thread(clientInput);
            Thread outputThread = new Thread(clientOutput);

            inputThread.start();
            outputThread.start();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
