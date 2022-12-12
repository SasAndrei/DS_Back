package com.example.demo.controller;

import com.example.demo.dto.DeviceReadingDto;
import com.example.demo.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/readings")
public class ReadingController {

    @Autowired
    private ReadingService readingService;

    @PostMapping
    public ResponseEntity<?> getReadingsOfDevice(@RequestBody DeviceReadingDto deviceReadingDto) {
        return new ResponseEntity<>(readingService.getReadingsOfDevice(deviceReadingDto), HttpStatus.OK);
    }

    @PostMapping(value = "/set")
    public ResponseEntity<?> setDemoReadings(@RequestBody DeviceReadingDto deviceReadingDto) {
        return new ResponseEntity<>(readingService.setDemoReadings(deviceReadingDto), HttpStatus.OK);
    }
}
