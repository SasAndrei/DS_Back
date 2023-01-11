package com.example.demo.service;

import com.example.demo.dto.DeviceOwnedDataDto;
import com.example.demo.dto.DeviceReadingDto;
import com.example.demo.dto.OwnedDeviceDto;
import com.example.demo.dto.ReadingDto;
import com.example.demo.model.Device;
import com.example.demo.model.OwnedDevice;
import com.example.demo.model.Reading;
import com.example.demo.model.User;
import com.example.demo.rabbitMQ.ReadingRecive;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.OwnedDeviceRepository;
import com.example.demo.repository.ReadingRepository;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OwnedDeviceService {

    @Autowired
    OwnedDeviceRepository ownedDeviceRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(OwnedDeviceService.class);

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReadingRepository readingRepository;

    @Autowired
    private ReadingService readingService;

    public List<DeviceOwnedDataDto> getDevicesOwndeByUser(Integer user_id) {
        List<OwnedDevice> ownedDevices = ownedDeviceRepository.findAll().stream().filter((ownedDevice) -> ownedDevice.getUser().getId().equals(user_id)).toList();
        return ownedDevices.stream().map((ownedDevice) -> new DeviceOwnedDataDto(ownedDevice.getId(), ownedDevice.getDevice().getDescription(), ownedDevice.getAddress(), ownedDevice.getDevice().getMaxConsumption())).toList();
    }

    public OwnedDeviceDto mapOwnedDevice(OwnedDeviceDto ownedDevice) {
        OwnedDevice newOwnedDevice;
        Device device = deviceRepository.getReferenceById(ownedDevice.device_id());
        User user = userRepository.getReferenceById(ownedDevice.user_id());
        newOwnedDevice = new OwnedDevice(user, device, ownedDevice.address());
        ownedDeviceRepository.save(newOwnedDevice);
        return ownedDevice;
    }

    public void delete(Integer oid) {
        OwnedDevice ownedDevice = ownedDeviceRepository.getReferenceById(oid);
        ownedDeviceRepository.delete(ownedDevice);
    }

    public OwnedDevice getOwnedDevice(Integer id) {
        Optional<OwnedDevice> deviceOptional = ownedDeviceRepository.findById(id);
        if (!deviceOptional.isPresent()){
            LOGGER.error("Device with id {} was not found in db", id);
        }

        return deviceOptional.get();
    }

    public Boolean addFromRabbitMQ(ReadingRecive rabbitMQValue){
        Optional<OwnedDevice> deviceOptional = ownedDeviceRepository.findById(rabbitMQValue.getId());

        if (!deviceOptional.isPresent()){
            LOGGER.error("Device with id {} was not found in db", rabbitMQValue.getId());
        }
        Long date = rabbitMQValue.getTimestamp().getTime();

        List<ReadingDto> readingDtos = readingService.getReadingsOfDevicePerHour(new DeviceReadingDto(rabbitMQValue.getId(), date));

        Reading measurement = new Reading();
        measurement.setTimeOfRecord(rabbitMQValue.getTimestamp());
        measurement.setConsumption((long) rabbitMQValue.getConsumption());
        measurement.setOwnedDevice(deviceOptional.get());
        measurement = readingRepository.save(measurement);

        AtomicReference<Long> total = new AtomicReference<>((long) rabbitMQValue.getConsumption());
        readingDtos.stream().forEach((reading) -> {
            total.updateAndGet(v -> v + reading.consumption());});

        Long convert = total.get();

        if (convert > deviceOptional.get().getDevice().getMaxConsumption())
            return true;
        return  false;
    }
}
