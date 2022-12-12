package com.example.demo.service;

import com.example.demo.dto.DeviceOwnedDataDto;
import com.example.demo.dto.OwnedDeviceDto;
import com.example.demo.model.Device;
import com.example.demo.model.OwnedDevice;
import com.example.demo.model.User;
import com.example.demo.repository.DeviceRepository;
import com.example.demo.repository.OwnedDeviceRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
public class OwnedDeviceService {

    @Autowired
    OwnedDeviceRepository ownedDeviceRepository;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserRepository userRepository;

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
}
