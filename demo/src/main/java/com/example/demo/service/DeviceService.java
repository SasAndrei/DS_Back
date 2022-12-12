package com.example.demo.service;

import com.example.demo.dto.DeviceDto;
import com.example.demo.dto.UserDto;
import com.example.demo.model.Device;
import com.example.demo.model.User;
import com.example.demo.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    public List<DeviceDto> allDevices() {
        return deviceRepository.findAll().stream().map((device) -> new DeviceDto(device.getId(), device.getDescription(), device.getMaxConsumption())).toList();
    }

    public DeviceDto createDevice(DeviceDto deviceDto) {
        Device newDevice = new Device(deviceDto.description(), deviceDto.maxConsumption());
        deviceRepository.save(newDevice);
        return deviceDto;
    }

    public void delete(Integer did) {
        Device device = deviceRepository.getReferenceById(did);
        deviceRepository.delete(device);
    }
}
