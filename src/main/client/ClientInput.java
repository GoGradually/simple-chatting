package main.client;

import main.CloseUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientInput implements Runnable {

    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public ClientInput(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        while(true){
            try {
                String s = inputStream.readUTF();
                System.out.println(s);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                CloseUtils.closeAll(socket, inputStream, outputStream);
                return;
            }
        }
    }
}
