package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(45678);
        SessionManager sessionManager = new SessionManager();
        ShutdownHook shutdownHook = new ShutdownHook(sessionManager);
        Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
        while(true) {
            Socket socket = serverSocket.accept();
            Session session = new Session(socket, sessionManager);
            Thread thread = new Thread(session);
            thread.start();
        }
    }

    static class ShutdownHook implements Runnable{
        private final SessionManager sessionManager;

        public ShutdownHook(SessionManager sessionManager) {
            this.sessionManager = sessionManager;
        }

        @Override
        public void run() {

        }
    }
}
