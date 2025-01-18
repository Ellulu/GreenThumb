package com.helmo.greenThumb.services;



import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionManager {
    private final ConcurrentHashMap<String, Boolean> userConnections = new ConcurrentHashMap<>();

    public void addUser(String uid) {
        userConnections.put(uid, true);
        System.out.println("Utilisateur connecté : " + uid);
    }

    public void removeUser(String uid) {
        userConnections.remove(uid);
        System.out.println("Utilisateur déconnecté : " + uid);
    }

    public boolean isUserConnected(String uid) {
        return userConnections.containsKey(uid);
    }
}
