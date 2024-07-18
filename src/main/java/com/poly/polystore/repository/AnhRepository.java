package com.poly.polystore.repository;

import com.poly.polystore.entity.Anh;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnhRepository extends JpaRepository<Anh, Integer> {

    Anh findOneByName(String fileName);
}