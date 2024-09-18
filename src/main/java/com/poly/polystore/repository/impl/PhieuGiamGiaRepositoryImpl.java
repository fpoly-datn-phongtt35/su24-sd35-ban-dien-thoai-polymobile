package com.poly.polystore.repository.impl;

import com.google.common.base.Strings;
import com.poly.polystore.entity.MaGiamGia;
import com.poly.polystore.entity.PhieuGiamGia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PhieuGiamGiaRepositoryImpl {
    @PersistenceContext
    private EntityManager em;

    public Page<PhieuGiamGia> getMagiamgia(int page, int size, String code, String beginDate, String endDate) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT [ID]\n" +
                "\t    ,[Code]\n" +
                "      ,[Gia_tri_giam]\n" +
                "      ,[Don_vi]\n" +
                "      ,[Thoi_gian_ket_thuc]\n" +
                "      ,[Thoi_gian_bat_dau]\n" +
                "      ,[Create_at]\n" +
                "      ,[Update_at]\n" +
                "      ,[Last_modified_by]\n" +
                "      ,[soluong]\n" +
                "      ,[Deleted]\n" +
                "      ,[status]\n" +
                "  FROM [dbo].[PHIEU_GIAM_GIA] where [Deleted] = 0");
        if(!Strings.isNullOrEmpty(code)){
            sql.append(" and code = :code");
            params.put("code", code);
        }
        if(!Strings.isNullOrEmpty(beginDate)){
            sql.append(" and [Thoi_gian_bat_dau] >= :beginDate");
            params.put("beginDate", beginDate);
        }
        if(!Strings.isNullOrEmpty(endDate)){
            sql.append(" and [Thoi_gian_ket_thuc] <= :endDate");
            params.put("endDate", endDate);
        }
        Query countQuery = em.createNativeQuery("Select count(*) from (" + sql.toString() + ") as count");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }

        long totalRecords = ((Number) countQuery.getSingleResult()).longValue();
        Query query = em.createNativeQuery(sql.toString(), PhieuGiamGia.class);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        int firstResult = (page - 1) * size;
        query.setFirstResult(firstResult);
        query.setMaxResults(size);

        List<PhieuGiamGia> results = query.getResultList();
        return new PageImpl<>(results, PageRequest.of(page,size), totalRecords);
    }
}
