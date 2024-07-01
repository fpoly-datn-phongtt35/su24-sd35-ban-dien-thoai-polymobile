package com.poly.polystore.repository;

import com.poly.polystore.entity.TinhNangDacBiet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface TinhNangDacBietRepository extends JpaRepository<TinhNangDacBiet, Integer> {
    Collection<TinhNangDacBiet> findAllByDeletedIsFalse();
}