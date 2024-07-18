package com.poly.polystore.repository;

import com.poly.polystore.entity.Bluetooth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface BluetoothRepository extends JpaRepository<Bluetooth, Integer> {
    List<Bluetooth> findAllByDeletedIsFalseOrderByIdDesc();
}