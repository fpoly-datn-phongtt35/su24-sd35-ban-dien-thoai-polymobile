package com.poly.polystore.core.admin.kho.repository;

import com.google.common.base.Strings;
import com.poly.polystore.core.admin.kho.model.request.NhapHangRequest;
import com.poly.polystore.core.admin.kho.model.response.LichSuKhoResponse;
import com.poly.polystore.dto.DataTableResponse;
import com.poly.polystore.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class LichSuKhoRepositoryImpl {
    @PersistenceContext
    private final EntityManager entityManager;



    public DataTableResponse findAll(Map<String, String> params) {
        var resp = new DataTableResponse();
        resp.setData(null);
        resp.setDraw(Integer.valueOf(params.get("draw")));
        resp.setRecordsTotal(0);
        resp.setRecordsFiltered(0);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        //       Tạo một đối tượng CriteriaQuery để chỉ định kiểu dữ liệu trả về là SanPhamDataTableBanHang.
        CriteriaQuery<LichSuKhoResponse> query = cb.createQuery(LichSuKhoResponse.class);
        Root<LichSuKho> lsk = query.from(LichSuKho.class);
        Join<LichSuKho, TaiKhoan> taiKhoan = lsk.join("taiKhoan", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<Predicate>();

        //Lọc theo search key
        if (!Strings.isNullOrEmpty(params.get("search[value]"))) {
            var predicate = cb.equal(cb.toString(lsk.get("id")),  params.get("search[value]") );
            predicates.add(predicate);
        }

        //Lọc theo nhân viên
        if (!Strings.isNullOrEmpty(params.get("columns[0][search][value]"))) {
            var ids=params.get("columns[0][search][value]").split(",");
            Predicate predicate=cb.equal(cb.literal(true),false);
            for (String id : ids) {
                predicate= cb.or(predicate,cb.equal(lsk.get("taiKhoan").get("id"), id));
            }
            predicates.add(predicate);
        }


        query.select(cb.construct(
                        LichSuKhoResponse.class,
                        lsk.get("id"),
                        lsk.get("thoiGian"),
                        lsk.get("ghiChu"),
                        lsk.get("deleted"),
                        taiKhoan.get("id"),
                        taiKhoan.get("ten")
                ))
                .where(predicates.toArray(new Predicate[0]));

        if (!Strings.isNullOrEmpty(params.get("order[0][column]"))) {
            Integer sortColumn = Integer.valueOf(params.get("order[0][column]"));
            String sortBy = params.get(String.format("columns[%d][data]", sortColumn));
            String sortOrder = params.get("order[0][dir]");
            if (!"".equals(sortBy)) {
                Order oder = "asc".equals(sortOrder) ?
                        cb.asc(lsk.get(sortBy))
                        : cb.desc(lsk.get(sortBy));
                query.orderBy(oder);
            }
        }
        var typeQuery = entityManager.createQuery(query);
        //Phân trnag
        typeQuery.setFirstResult(Integer.parseInt(params.get("start")));
        typeQuery.setMaxResults(Integer.parseInt(params.get("length")));

        var respResult = typeQuery.getResultList();


        //Tạo đối tượng đếm
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        //Tạo root đếm
        Root<LichSuKho> countRoot = countQuery.from(LichSuKho.class);


        List<Predicate> predicatesCount = new ArrayList<Predicate>();

        //Lọc theo search key
        if (!Strings.isNullOrEmpty(params.get("search[value]"))) {
            var predicate = cb.equal(cb.toString(countRoot.get("id")), "%" + params.get("search[value]") + "%");
            predicatesCount.add(predicate);
        }

        //Lọc theo nhân viên
        if (!Strings.isNullOrEmpty(params.get("columns[0][search][value]"))) {
            var ids=params.get("columns[0][search][value]").split(",");
            Predicate predicate=cb.equal(cb.literal(true),false);
            for (String id : ids) {
                predicate= cb.or(predicate,cb.equal(countRoot.get("taiKhoan").get("id"), id));
            }
            predicatesCount.add(predicate);
        }



        countQuery.select(cb.count(countRoot));

        var countResult = entityManager.createQuery(countQuery).getSingleResult();
        countQuery.where(predicatesCount.toArray(new Predicate[0]));
        var countResultFilter = entityManager.createQuery(countQuery).getSingleResult();


        resp.setRecordsFiltered(countResultFilter.intValue());
        resp.setRecordsTotal(countResult.intValue());
        resp.setDraw(Integer.valueOf(params.get("draw")));
        resp.setData(respResult);


//        for(Map.Entry<String,String> entry: params.entrySet()) {
//            if(entry.getKey().matches("columns\\[\\d+\\]\\[search+\\]\\[value\\]")
//                            && !Strings.isNullOrEmpty(entry.getValue())
//                    ){
//                        var column_number=Integer.parseInt(entry.getKey().replaceAll("\\D",""));
//                        var column_name=params.get(String.format("columns[%d][data]",column_number));
//                        predicates.add(cb.like(lsk.get(column_name), entry.getValue()));
//                        break;
//                    }
//        }

//        params.forEach((key,value)->{
//                    if(key.matches("columns\\[\\d+\\]\\[search+\\]\\[value\\]")
//                            && !Strings.isNullOrEmpty(value)
//                    ){
//                        var column_number=Integer.parseInt(key.replaceAll("\\D",""));
//                        var column_name=params.get(String.format("columns[%d][data]",column_number));
//                        predicates.add(cb.like(lsk.get(column_name), value));
//                    }
//                }
//        );

//        query.select(cb.construct(
//                LichSuKhoResponse.class,
//                lsk.get()
//        ))

        return resp;
    }

    public List<?> findById(Integer id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<LichSuKhoResponse.LichSuKhoChiTiet> query = cb.createQuery(LichSuKhoResponse.LichSuKhoChiTiet.class);
        Root<LichSuKho> lsk=query.from(LichSuKho.class);
        Join<LichSuKho,ChiTietLichSuKho> chiTietLichSuKho = lsk.join("chiTietLichSuKhos",JoinType.LEFT);
        Join<ChiTietLichSuKho, SanPhamChiTiet> spct = chiTietLichSuKho.join("sanPhamChiTiet", JoinType.LEFT);
        Join<SanPhamChiTiet, SanPham> sp = spct.join("sanPham", JoinType.LEFT);
        Join<SanPhamChiTiet, MauSac> ms = spct.join("mauSac", JoinType.LEFT);

        query
                .select(cb.construct(
                        LichSuKhoResponse.LichSuKhoChiTiet.class,
                        spct.get("id"),
                        chiTietLichSuKho.get("soLuong"),
                        cb.concat(
                                cb.concat(
                                        cb.concat(sp.get("tenSanPham"), cb.literal(" ")),
                                        cb.concat(spct.get("rom"), cb.literal(" "))),
                                ms.get("ten"))

                ))
                .where(cb.equal(lsk.get("id"),id));
        query.orderBy(cb.desc(sp.get("tenSanPham")),cb.desc(spct.get("rom")));
        var typeQuery=entityManager.createQuery(query);



        return typeQuery.getResultList();
    }
}
