package com.helmo.greenThumb.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "https://green-thumb-five.vercel.app/")
public class IPController {

    @GetMapping("/get-ip")
    public String getClientIp(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        return "Client IP: " + clientIp;
    }
}
