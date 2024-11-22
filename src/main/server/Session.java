package main.server;

import main.CloseUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static main.LogUtils.log;

public class Session implements Runnable {
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private String username = null;
    private boolean closed = false;
    private final SessionManager sessionManager;

    public Session(Socket socket, SessionManager sessionManager) throws IOException {
        this.socket = socket;
        this.sessionManager = sessionManager;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {

        while (true) {
            try {
                String inputString = input.readUTF();
                controlInput(inputString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public synchronized void close() {
        if (closed) return;
        closed = true;
        sessionManager.removeSession(this);
        CloseUtils.closeAll(socket, input, output);
    }

    public void receiveMessage(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            log(e.getMessage());
        }
    }

    private void controlInput(String inputString) throws IOException {
        if (inputString.startsWith("/join|")) {
            join(inputString.substring(6));
        } else if (inputString.startsWith("/message|")) {
            sendMessage(inputString.substring(9));
        } else if (inputString.startsWith("/change|")) {
            changeUsername(inputString.substring(8));
        } else if (inputString.equals("/users")) {
            getUserList();
        } else if (inputString.equals("/exit")) {
            close();
        }else{
            alert();
        }
    }


    private void join(String username) throws IOException {
        if (this.username != null) {
            output.writeUTF("이미 대화방에 참여했습니다.");
            return;
        }
        this.username = username;
        sessionManager.addSession(this);
        sessionManager.spreadMessage(username + "님이 대화방에 참여했습니다.");
    }

    private void sendMessage(String message) throws IOException {
        if (validateJoin()) return;
        sessionManager.spreadMessage("[" + username + "]: " + message);
    }


    private void changeUsername(String username) throws IOException {
        if (validateJoin()) return;
        sendMessage("[닉네임 변경]: " + this.username + "님이 이름을 " + username + "으로 변경하셨습니다.");
        this.username = username;
    }

    private void getUserList() throws IOException {
        String userList = sessionManager.getUserList();
        output.writeUTF(userList);

    }

    private boolean validateJoin() throws IOException {
        if (username == null) {
            output.writeUTF("먼저 /join|{name} 을 통해 대화방에 참여해주세요.");
            return true;
        }
        return false;
    }
    private void alert() throws IOException {
        output.writeUTF("올바른 형식으로 입력해주세요.");
    }
}
