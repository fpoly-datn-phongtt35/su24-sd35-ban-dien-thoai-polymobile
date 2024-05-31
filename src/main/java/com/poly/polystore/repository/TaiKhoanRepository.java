package com.poly.polystore.repository;

import com.poly.polystore.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {
    Optional<TaiKhoan> findBySoDienThoaiOrEmail();
}