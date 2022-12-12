package com.example.demo.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "readings")
public class Reading {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owned_fk", nullable = false)
    private OwnedDevice ownedDevice;

    @Column(name = "consumption", nullable = false)
    private Long consumption;

    @Column(name = "time_of_record", nullable = false)
    private Timestamp timeOfRecord;

    public Reading() {
    }

    public Reading(OwnedDevice ownedDevice, Long consumption, Timestamp timeOfRecord) {
        this.ownedDevice = ownedDevice;
        this.consumption = consumption;
        this.timeOfRecord = timeOfRecord;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OwnedDevice getOwnedDevice() {
        return ownedDevice;
    }

    public void setOwnedDevice(OwnedDevice ownedDevice) {
        this.ownedDevice = ownedDevice;
    }

    public Long getConsumption() {
        return consumption;
    }

    public void setConsumption(Long consumption) {
        this.consumption = consumption;
    }

    public Timestamp getTimeOfRecord() {
        return timeOfRecord;
    }

    public void setTimeOfRecord(Timestamp timeOfRecord) {
        this.timeOfRecord = timeOfRecord;
    }

    public boolean compareDate(Timestamp time) {
        return  (this.timeOfRecord.getDay() == time.getDay()) &&
                (this.timeOfRecord.getMonth() == time.getMonth()) &&
                (this.timeOfRecord.getYear() == time.getYear());
    }
}
