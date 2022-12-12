package com.example.demo.controller;

import com.example.demo.dto.OwnedDeviceDto;
import com.example.demo.service.OwnedDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/owned")
public class OwnedDeviceController {

    @Autowired
    OwnedDeviceService ownedDeviceService;

    @GetMapping(value = "/user")
    public ResponseEntity<?> getDevicesOfUser(@RequestParam Integer id_user) {
        return new ResponseEntity<>(ownedDeviceService.getDevicesOwndeByUser(id_user), HttpStatus.OK);
    }

    @PostMapping(value = "/map")
    public ResponseEntity<?> mapDeviceOwnership(@RequestBody OwnedDeviceDto ownedDeviceDto) {
        return new ResponseEntity<>(ownedDeviceService.mapOwnedDevice(ownedDeviceDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteDeviceOwnership(@RequestParam Integer map_id) {
        ownedDeviceService.delete(map_id);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

}
