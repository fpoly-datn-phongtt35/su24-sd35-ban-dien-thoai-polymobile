package com.poly.polystore.repository;

import com.poly.polystore.entity.MatKinhCamUng;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface MatKinhCamUngRepository extends JpaRepository<MatKinhCamUng, Integer> {
    Collection<Object> findAllByDeletedIsFalse();
}