package com.poly.polystore.repository;

import com.poly.polystore.entity.Imei;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImeiRepository extends JpaRepository<Imei, String> {
}