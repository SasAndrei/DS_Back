package com.example.demo.controller;

import com.example.demo.model.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate template) {
        this.messagingTemplate = template;
    }

    public void send(String caregiver, Notification notification) {
        this.messagingTemplate.convertAndSend("/anomalous-activities/" + caregiver,
                notification);
    }
}