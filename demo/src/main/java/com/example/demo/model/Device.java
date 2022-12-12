package com.example.demo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "maxConsumption", nullable = false)
    private Long maxConsumption;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device",cascade = CascadeType.ALL)
    private List<OwnedDevice> ownedDevices;

    public Device() {
    }

    public Device(String description, Long maxConsumption) {
        this.description = description;
        this.maxConsumption = maxConsumption;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMaxConsumption() {
        return maxConsumption;
    }

    public void setMaxConsumption(Long maxConsumption) {
        this.maxConsumption = maxConsumption;
    }

    public List<OwnedDevice> getOwnedDevices() {
        return ownedDevices;
    }

    public void setOwnedDevices(List<OwnedDevice> ownedDevices) {
        this.ownedDevices = ownedDevices;
    }
}
