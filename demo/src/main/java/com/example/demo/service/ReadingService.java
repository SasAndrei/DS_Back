package com.example.demo.service;

import com.example.demo.dto.DeviceReadingDto;
import com.example.demo.dto.ReadingDto;
import com.example.demo.model.OwnedDevice;
import com.example.demo.model.Reading;
import com.example.demo.repository.OwnedDeviceRepository;
import com.example.demo.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class ReadingService {

    @Autowired
    private OwnedDeviceRepository ownedDeviceRepository;

    @Autowired
    private ReadingRepository readingRepository;

    public List<ReadingDto> getReadingsOfDevice(DeviceReadingDto deviceReadingDto) {
        List<Reading> readings = readingRepository.findAll();
        Timestamp selectedDate = new Timestamp(deviceReadingDto.date_in_mil());
        List<ReadingDto> readingsList = readings.stream().filter((reading) -> {return reading.getOwnedDevice().getId().equals(deviceReadingDto.device_id());})
                .filter((reading) -> {return reading.compareDate(selectedDate);})
                .map((reading) -> {return new ReadingDto(reading.getConsumption(), reading.getTimeOfRecord());}).toList();
        return readingsList;
    }

    public List<ReadingDto> getReadingsOfDevicePerHour(DeviceReadingDto deviceReadingDto) {
        List<Reading> readings = readingRepository.findAll();
        Timestamp selectedDate = new Timestamp(deviceReadingDto.date_in_mil());
        List<ReadingDto> readingsList = readings.stream().filter((reading) -> {return reading.getOwnedDevice().getId().equals(deviceReadingDto.device_id());})
                .filter((reading) -> {return reading.compareHour(selectedDate);})
                .map((reading) -> {return new ReadingDto(reading.getConsumption(), reading.getTimeOfRecord());}).toList();
        return readingsList;
    }

    public List<Reading> setDemoReadings(DeviceReadingDto deviceReadingDto) {
        Timestamp requestDate = new Timestamp(deviceReadingDto.date_in_mil());
        OwnedDevice ownedDevice = ownedDeviceRepository.getReferenceById(deviceReadingDto.device_id());
        for(int i = 0; i < 24; i++) {
            Timestamp readDate = requestDate;
            readDate.setHours(i);
            Reading reading = new Reading(ownedDevice, (long) i, readDate);
            readingRepository.save(reading);
        }
        return readingRepository.findAll();
    }
}
