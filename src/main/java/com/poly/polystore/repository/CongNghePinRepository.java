package com.poly.polystore.repository;

import com.poly.polystore.entity.CongNghePin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface CongNghePinRepository extends JpaRepository<CongNghePin, Integer> {
    Collection<Object> findAllByDeletedIsFalse();
}