package com.poly.polystore.repository;

import com.poly.polystore.entity.Pin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PinRepository extends JpaRepository<Pin, Integer> {
}