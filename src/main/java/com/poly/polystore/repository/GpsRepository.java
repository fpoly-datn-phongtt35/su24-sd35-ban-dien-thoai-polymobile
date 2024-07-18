package com.poly.polystore.repository;

import com.poly.polystore.entity.Gps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface GpsRepository extends JpaRepository<Gps, Integer> {
    Collection<Gps> findAllByDeletedIsFalseOrderByIdDesc();
}