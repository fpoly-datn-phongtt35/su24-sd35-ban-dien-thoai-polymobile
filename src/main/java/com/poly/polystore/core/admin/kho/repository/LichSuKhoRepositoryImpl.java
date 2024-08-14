package com.poly.polystore.core.admin.kho.repository;

import com.poly.polystore.core.admin.kho.model.response.LichSuKhoResponse;
import com.poly.polystore.entity.ChiTietLichSuKho;
import com.poly.polystore.entity.LichSuKho;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LichSuKhoRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;

    public List<LichSuKhoResponse> findAll(Map<String,String> param) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        //       Tạo một đối tượng CriteriaQuery để chỉ định kiểu dữ liệu trả về là SanPhamDataTableBanHang.
        CriteriaQuery<LichSuKhoResponse> query = cb.createQuery(LichSuKhoResponse.class);
        Root<LichSuKho> lsk = query.from(LichSuKho.class);
        Join<LichSuKho, ChiTietLichSuKho> chiTietLichSuKho=lsk.join("chiTietLichSuKho");
        List<Predicate> predicates = new ArrayList<Predicate>();

        if(param.containsKey("sea")) {

        }
        return null;
    }
}
