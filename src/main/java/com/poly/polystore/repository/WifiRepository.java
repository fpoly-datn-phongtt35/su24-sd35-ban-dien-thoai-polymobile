package com.poly.polystore.repository;

import com.poly.polystore.entity.Wifi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface WifiRepository extends JpaRepository<Wifi, Integer> {
    Collection<Wifi> findAllByDeletedIsFalseOrderByIdDesc();
}