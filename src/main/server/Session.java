package main.server;

import main.CloseUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session implements Runnable{
    private final Socket socket;
    private final DataInputStream input;
    private final DataOutputStream output;
    private String username;
    private boolean closed = false;
    private final SessionManager sessionManager;

    public Session(Socket socket, SessionManager sessionManager) throws IOException {
        this.socket = socket;
        this.sessionManager = sessionManager;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        sessionManager.addSession(this);
    }

    @Override
    public void run() {

        try{
            String join = input.readUTF();
            if (!join.startsWith("/join")){

            }

            while(true){

            }
        }catch (IOException e){

        }

    }

    public String getUsername(){
        return username;
    }

    public void close(){
        if(closed){
            return;
        }
        CloseUtils.closeAll(socket, input, output);
        closed = true;
    }

    private void setUsername(String username){
        this.username = username;
    }
}
