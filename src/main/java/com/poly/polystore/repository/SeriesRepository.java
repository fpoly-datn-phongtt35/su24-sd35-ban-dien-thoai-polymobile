package com.poly.polystore.repository;

import com.poly.polystore.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.Collection;

public interface SeriesRepository extends JpaRepository<Series, Integer> {
    Collection<Series> findAllByDeletedIsFalse();
}