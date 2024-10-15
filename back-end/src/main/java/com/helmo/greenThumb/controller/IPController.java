package com.helmo.greenThumb.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IPController {

    @GetMapping("/get-ip")
    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        System.out.println("Client IP: " + clientIp); // Affiche l'IP dans la console
        return "Client IP: " + clientIp; // Retourne l'IP comme réponse
    }
}
