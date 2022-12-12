package com.example.demo.controller;

import com.example.demo.dto.DeviceDto;
import com.example.demo.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @GetMapping
    public ResponseEntity<List<DeviceDto>> getDevices() {
        return new ResponseEntity<>(deviceService.allDevices(), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createDevice(@RequestBody DeviceDto deviceDto) {
        return new ResponseEntity<>(deviceService.createDevice(deviceDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteDevice(@RequestParam Integer device_id) {
        deviceService.delete(device_id);
        return new ResponseEntity<>("Device deleted!", HttpStatus.OK);
    }

}
