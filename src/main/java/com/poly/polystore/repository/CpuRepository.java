package com.poly.polystore.repository;

import com.poly.polystore.entity.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface CpuRepository extends JpaRepository<Cpu, Integer> {
    Collection<Cpu> findAllByDeletedIsFalse();
}