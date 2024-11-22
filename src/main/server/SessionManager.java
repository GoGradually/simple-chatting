package main.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SessionManager {
    private final List<Session> sessions = new ArrayList<>();


    public synchronized void addSession(Session session) {
        sessions.add(session);
    }
    public synchronized void removeSession(Session session){
        sessions.remove(session);
    }

    public void closeAllSessions(){
        sessions.forEach(Session::close);
    }

    public String getUserList(){
        return sessions.stream()
                .map(Session::getUsername)
                .collect(Collectors.joining(", "));
    }

    public void spreadMessage(String message) {
        sessions
                .forEach(session -> session.receiveMessage(message));
    }
}
