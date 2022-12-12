package com.example.demo.repository;

import com.example.demo.dto.DeviceOwnedDataDto;
import com.example.demo.model.OwnedDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OwnedDeviceRepository extends JpaRepository<OwnedDevice, Integer> {

}
