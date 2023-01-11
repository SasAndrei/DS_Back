package com.example.demo.rabbitMQ;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReadingRecive implements Serializable {
    private Integer id;
    private float consumption;
    private Timestamp timestamp;
}
