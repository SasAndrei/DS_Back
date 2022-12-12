package com.example.demo.repository;

import com.example.demo.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRepository extends JpaRepository<Reading, Integer> {

}
