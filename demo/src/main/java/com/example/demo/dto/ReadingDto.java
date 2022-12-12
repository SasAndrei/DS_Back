package com.example.demo.dto;

import java.sql.Timestamp;

public record ReadingDto(Long consumption, Timestamp timeOfRecord) {
}
