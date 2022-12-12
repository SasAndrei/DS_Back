package com.example.demo.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "owned_devices")
public class OwnedDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_fk", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_fk", nullable = false)
    private Device device;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ownedDevice",cascade = CascadeType.ALL)
    private List<Reading> reading;

    public OwnedDevice() {
    }

    public OwnedDevice(User user, Device device, String address) {
        this.user = user;
        this.device = device;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Reading> getReading() {
        return reading;
    }

    public void setReading(List<Reading> reading) {
        this.reading = reading;
    }



    public Long totalConsumption() {
        return this.reading.stream().mapToLong((read) -> read.getConsumption()).sum();
    }
}
