package main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static main.LogUtils.log;

public class Server {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(45678)){
            SessionManager sessionManager = new SessionManager();
            ShutdownHook shutdownHook = new ShutdownHook(sessionManager, serverSocket);
            Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
            while(true) {
                Socket socket = serverSocket.accept();
                Session session = new Session(socket, sessionManager);
                Thread thread = new Thread(session);
                thread.start();
            }
        } catch (IOException e) {
            log(e.getMessage());
        }

    }

    static class ShutdownHook implements Runnable{
        private final SessionManager sessionManager;
        private final ServerSocket serverSocket;

        public ShutdownHook(SessionManager sessionManager, ServerSocket serverSocket) {
            this.sessionManager = sessionManager;
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            sessionManager.closeAllSessions();
            try {
                serverSocket.close();
            } catch (IOException e) {
                log(e.getMessage());
            }
        }
    }
}
