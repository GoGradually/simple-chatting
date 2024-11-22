package main.client;

import main.CloseUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientOutput implements Runnable {
    private final Socket socket;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;

    public ClientOutput(Socket socket, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        while (true) {
            System.out.print("메시지를 입력하세요.");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            try {
                outputStream.writeUTF(input);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                CloseUtils.closeAll(socket, inputStream, outputStream);
                return;
            }
        }
    }
}
