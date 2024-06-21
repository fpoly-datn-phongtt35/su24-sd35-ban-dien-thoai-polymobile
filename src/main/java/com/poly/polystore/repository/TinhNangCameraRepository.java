package com.poly.polystore.repository;

import com.poly.polystore.entity.TinhNangCamera;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface TinhNangCameraRepository extends JpaRepository<TinhNangCamera, Integer> {
    Collection<Object> findAllByDeletedIsFalse();
}