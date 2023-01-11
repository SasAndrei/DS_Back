package com.example.demo.controller;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.model.OwnedDevice;
import com.example.demo.rabbitMQ.Reciver;
import com.example.demo.rabbitMQ.ReadingRecive;
import com.example.demo.service.OwnedDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "")
public class NotificationController {

    @Autowired
    SimpMessagingTemplate template;

    @Autowired
    private OwnedDeviceService ownedDeviceService;

    @Autowired
    public NotificationController() {
        System.out.println("Create");
        Reciver reciver = new Reciver(this);
        reciver.run();
        System.out.println("Start");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody NotificationDTO notificationDTO) {
        template.convertAndSend("/topic/message", notificationDTO);
        return new ResponseEntity<>("Send", HttpStatus.OK);
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload NotificationDTO notificationDTO) {
        // receive message from client
    }

    @SendTo("/topic/message")
    public NotificationDTO broadcastMessage(@Payload NotificationDTO notificationDTO) {
        return notificationDTO;
    }

    public Boolean addFromRabbitMQ(ReadingRecive rabbitMQValue) {
        return ownedDeviceService.addFromRabbitMQ(rabbitMQValue);
    }

    public OwnedDevice getDevice(Integer id) {
        return  ownedDeviceService.getOwnedDevice(id);
    }
}
