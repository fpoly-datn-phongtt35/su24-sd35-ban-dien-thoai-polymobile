package com.poly.polystore.core.admin.ban_hang.repository.impl;

import com.azure.core.http.rest.PagedResponse;
import com.poly.polystore.core.admin.ban_hang.model.response.SanPhamDataTableBanHang;
import com.poly.polystore.core.admin.ban_hang.model.response.SanPhamProductResponse;
import com.poly.polystore.core.client.models.response.SanPhamResponse;
import com.poly.polystore.dto.Select2Response;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.SanPhamRepository;
import com.poly.polystore.utils.PageResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class SanPhamRepositoryImpl {
    @PersistenceContext
    private final EntityManager em;



    public PageImpl<SanPhamDataTableBanHang> findAllSanPhamDataTableBanHang(String searchKey, Integer[] seriesFilter, String orderBy, Pageable pageAble) {
       CriteriaBuilder cb = em.getCriteriaBuilder();
//       Tạo một đối tượng CriteriaQuery để chỉ định kiểu dữ liệu trả về là SanPhamDataTableBanHang.
       CriteriaQuery<SanPhamDataTableBanHang> query = cb.createQuery(SanPhamDataTableBanHang.class);
        // Tạo  thực thể chính đại diện cho sản phẩm.
        Root<SanPham> sp = query.from(SanPham.class);

        // Join với thực thể SanPhamChiTiet
        Join<SanPham, SanPhamChiTiet> spct = sp.join("sanPhamChiTiet", JoinType.LEFT);



        //Tạo danh sách điều kiện lọc
        List<Predicate> predicates = new ArrayList<>();

        // Lọc theo searchKey
        if (searchKey != null && !searchKey.isEmpty()) {
            String searchPattern = "%" + searchKey.toLowerCase() + "%";
            Predicate tenSanPhamPredicate = cb.like(cb.lower(sp.get("tenSanPham")), searchPattern);
            Predicate idPredicate = null;
            try {
                Integer idValue = Integer.parseInt(searchKey);
                idPredicate = cb.equal(sp.get("id"), idValue); // Điều kiện cho id
            } catch (NumberFormatException e) {
                idPredicate = cb.equal(cb.literal(true), false);
            }
            Predicate tagPredicate = cb.like(cb.lower(sp.get("tag")), searchPattern);

            // Kết hợp các điều kiện bằng toán tử OR
            Predicate searchPredicate = cb.or(tenSanPhamPredicate, idPredicate, tagPredicate);
            predicates.add(searchPredicate);
        }

        // Lọc theo seriesFilter
        if (seriesFilter != null && seriesFilter.length > 0) {
            Path<Integer> idSeriesPath = sp.get("series").get("id");
            Predicate seriesPredicate = idSeriesPath.in(seriesFilter);
            predicates.add(seriesPredicate);
        }

        //      Tạo một đối tượng mới của SanPhamDataTableBanHang từ các trường của thực thể SanPham và tổng số lượng từ SanPhamChiTiet.
        query.select(cb.construct(
                SanPhamDataTableBanHang.class,
                sp.get("id"),
                sp.get("anh").get("url"),
                sp.get("tenSanPham"),
                sp.get("trangThai"),
                cb.sum(spct.get("soLuong"))
        ));
        //Thêm các nhóm lọc
        query.where(predicates.toArray(new Predicate[predicates.size()]));

        // GROUP BY các trường
        query.groupBy(sp.get("id"), sp.get("tenSanPham"), sp.get("anh").get("url"), sp.get("trangThai"));

        // Sắp xếp
        if (orderBy != null && !orderBy.isEmpty()) {
            Pattern pattern= Pattern.compile("(\\w+?)(:)(\\w+?)");
            Matcher matcher = pattern.matcher(orderBy);
            if(matcher.find()){
                Order order = "asc".equalsIgnoreCase(matcher.group(3))
                        ? cb.asc(sp.get(matcher.group(1)))
                        : cb.desc(sp.get(matcher.group(1)));
                query.orderBy(order);
            }
        }



        // Tạo và thực thi truy vấn
        TypedQuery<SanPhamDataTableBanHang> typedQuery = em.createQuery(query);
        // Phân trang
        typedQuery.setFirstResult(pageAble.getPageNumber()*pageAble.getPageSize());
        typedQuery.setMaxResults(pageAble.getPageSize());
        var lstSPData = typedQuery.getResultList();


        //Tạo đối tượng đếm
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        //Tạo root đếm
        Root<SanPham> countRoot = countQuery.from(SanPham.class);

        List<Predicate> predicates2 = new ArrayList<>();

        // Lọc theo searchKey
        if (searchKey != null && !searchKey.isEmpty()) {
            String searchPattern = "%" + searchKey.toLowerCase() + "%";
            Predicate tenSanPhamPredicate = cb.like(cb.lower(countRoot.get("tenSanPham")), searchPattern);
            Predicate idPredicate = null;
            try {
                Integer idValue = Integer.parseInt(searchKey);
                idPredicate = cb.equal(countRoot.get("id"), idValue); // Điều kiện cho id
            } catch (NumberFormatException e) {
                idPredicate = cb.equal(cb.literal(true), false);
            }
            Predicate tagPredicate = cb.like(cb.lower(countRoot.get("tag")), searchPattern);

            // Kết hợp các điều kiện bằng toán tử OR
            Predicate searchPredicate = cb.or(tenSanPhamPredicate, idPredicate, tagPredicate);
            predicates2.add(searchPredicate);
        }

        // Lọc theo seriesFilter
        if (seriesFilter != null && seriesFilter.length > 0) {
            Path<Integer> idSeriesPath = countRoot.get("series").get("id");
            Predicate seriesPredicate = idSeriesPath.in(seriesFilter);
            predicates2.add(seriesPredicate);
        }

        // Tính tổng số bản ghi

        countQuery.select(cb.count(countRoot))
              .where(predicates2.toArray(new Predicate[0]));
        TypedQuery<Long> countTypedQuery = em.createQuery(countQuery);
        Long totalRecords = countTypedQuery.getSingleResult();

        var resp= new PageImpl<>(lstSPData, pageAble, totalRecords);


        return resp;

    }

    public List<Object[]> findAllByIds(Set<Integer> ids) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
        Root<SanPhamChiTiet> spct = query.from(SanPhamChiTiet.class);
        query.multiselect(spct.get("id"),
            cb.concat(
                cb.concat(cb.concat(
                        cb.concat(spct.get("sanPham").get("tenSanPham")," "),
                        spct.get("rom"))," "),
            spct.get("mauSac").get("ten"))
        ).where(spct.get("id").in(ids));
        TypedQuery<Object[]> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    public Page<SanPhamResponse> search(
            Integer pageNo,
            String khoangGia,
            List<String> hoTroSacToiDa,
            List<Integer> tinhNangDacBiets,
            List<Integer> tinhNangCameras,
            List<String> kichThuocManHinh,
            List<Integer> series,
            List<String> rom,
            String orderBy,
            String searchKey,
            Integer pageSize) {
        CriteriaBuilder cb= em.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createQuery(Tuple.class);
        Root<SanPham> sanPham=query.from(SanPham.class);
        Join<SanPham,SanPhamChiTiet> sanPhamChiTiet=sanPham.join("sanPhamChiTiet",JoinType.LEFT);
        Join<SanPham,Anh> anh=sanPham.join("anh",JoinType.LEFT);
        Join<SanPham,Series> seriesJoin=sanPham.join("series",JoinType.LEFT);
        Join<SanPhamChiTiet,PhieuGiamGia> dotGiamGia=sanPhamChiTiet.join("dotGiamGia",JoinType.LEFT);
        Join<SanPhamChiTiet,MauSac> mauSac=sanPhamChiTiet.join("mauSac",JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();



//        //Search key
//        if(searchKey != null && !searchKey.isEmpty()){
//            Expression tenSanPham= cb.concat(//Tên sản phẩm
//                    cb.concat(cb.concat(
//                            cb.concat(sanPhamChiTiet.get("sanPham").get("tenSanPham")," "),
//                            sanPhamChiTiet.get("rom"))," "),
//                    sanPhamChiTiet.get("mauSac").get("ten"));
//            Predicate predicate=cb.like(tenSanPham,"%"+searchKey+"%");
//            predicates.add(predicate);
//        }

        Expression giaSauGiam = cb.selectCase()
                        .when(cb.isNull(dotGiamGia),
                                sanPhamChiTiet.get("giaBan")
                        )
                        .when(cb.equal(dotGiamGia.get("donvi"), "%"),
                                cb.diff(sanPhamChiTiet.get("giaBan"),cb.prod(sanPhamChiTiet.get("giaBan"),dotGiamGia.get("giaTriGiam")))
                        )
                        .when(cb.equal(dotGiamGia.get("donvi"), "number"),
                                cb.diff(sanPhamChiTiet.get("giaBan"),dotGiamGia.get("giaTriGiam"))
                        )
                        // Nếu đơn vị giảm giá là số tiền cố định
                        .otherwise(
                                sanPhamChiTiet.get("giaBan")
                        );


        //Khoang gia
        if(null != khoangGia && !khoangGia.isEmpty()){
            var khoangGias=khoangGia.split(",");
            List<Predicate> preds=new ArrayList<>();

            for (int i = 0; i < khoangGias.length; i++) {
                Integer from=Integer.parseInt(khoangGias[i].split("-")[0])*1000000;
                Integer to=Integer.parseInt(khoangGias[i].split("-")[1])*1000000;
                Expression<BigDecimal> giaSauKhiApDung=null;
                Predicate predicatePriceFrom=cb.greaterThanOrEqualTo(giaSauGiam,from);
                Predicate predicatePriceTo=cb.lessThanOrEqualTo(giaSauGiam,to);
                Predicate price=cb.and(predicatePriceFrom,predicatePriceTo);
                preds.add(price);
            }
            predicates.add(cb.or(preds.toArray(new Predicate[0])));
        }


//        Cong nghe sac
        if(hoTroSacToiDa != null && !hoTroSacToiDa.isEmpty()){
            predicates.add(sanPham.get("pinVaSac").get("hoTroSacToiDa").in(hoTroSacToiDa));
        }

//        Series
        if(series != null && !series.isEmpty()){
            predicates.add(seriesJoin.get("id").in(series));
        }

//        TinhNangDacBiet
        if(tinhNangDacBiets != null && !tinhNangDacBiets.isEmpty()){
            List<Predicate> tinhNangDacBietPredicates = new ArrayList<>();
            tinhNangDacBiets.forEach(tndbId->{
                tinhNangDacBietPredicates.add(
                        cb.isMember(tndbId,sanPham.get("thongTinChung").get("tinhNangDacBietIds")));
            });
            Predicate predicateTinhNangDacBietPredicate=cb.and(tinhNangDacBietPredicates.toArray(new Predicate[0]));
            predicates.add(predicateTinhNangDacBietPredicate);
        }

//        KichThuocManHinh
        if(null != kichThuocManHinh && !kichThuocManHinh.isEmpty()){
            List<Predicate> preds=new ArrayList<>();
            kichThuocManHinh.forEach(ss->{
                var from=Float.parseFloat(ss.split("-")[0]);
                var to=Float.parseFloat(ss.split("-")[1]);

                Expression<Float> floatValue =  sanPham.get("manHinh").get("manHinhRong").as(Float.class);
                Predicate minScreenSize=cb.greaterThanOrEqualTo(floatValue,from);
                Predicate maxScreenSize=cb.lessThanOrEqualTo(floatValue,to);
                Predicate preScreenSize=cb.and(minScreenSize,maxScreenSize);
                preds.add(preScreenSize);
            });
            predicates.add(cb.or(preds.toArray(new Predicate[0])));
        }

//        TinhNangDacBiet
        if(tinhNangCameras != null && !tinhNangCameras.isEmpty()){
            List<Predicate> tinhNangCameraPredicates = new ArrayList<>();
            tinhNangCameras.forEach(tncmrId->{
                var pre1=cb.isMember(tncmrId,sanPham.get("cameraTruoc").get("tinhNangCameraIds"));
                var pre2=cb.isMember(tncmrId,sanPham.get("cameraSau").get("tinhNangCameraIds"));
                tinhNangCameraPredicates.add(cb.or(pre1,pre2));
            });
            Predicate predicateTinhNangDacBietPredicate=cb.and(tinhNangCameraPredicates.toArray(new Predicate[0]));
            predicates.add(predicateTinhNangDacBietPredicate);

        }
        Predicate preTrangThai=cb.equal(sanPham.get("trangThai"),SanPhamRepository.TrangThai.IN_STOCK);
        Predicate preTrangThaiSPCT=cb.equal(sanPhamChiTiet.get("trangThai"),SanPhamRepository.TrangThai.IN_STOCK);
        predicates.add(preTrangThai);
        predicates.add(preTrangThaiSPCT);
//

//
//        query.select(sanPham)
//                .where


//        query.select(cb.construct(
//                SanPhamResponse.class,
//                sanPham.get("id"),
//                sanPham.get("anh").get("url"),
//                sanPham.get("anh").get("name"),
//                sanPham.get("tenSanPham"),
//                cb.list(
//                        SanPhamResponse.SanPhamChiTiet.class,
//                        sanPhamChiTiet.get("id"),
//                        sanPhamChiTiet.get("rom"),
//                        sanPhamChiTiet.get("giaBan"),
//                        sanPhamChiTiet.get("trangThai"),
//                        cb.construct(
//                                SanPhamResponse.SanPhamChiTiet.PhieuGiamGia.class,
//                                sanPhamChiTiet.get("dotGiamGia").get("giaTriGiam"),
//                                sanPhamChiTiet.get("dotGiamGia").get("donVi"),
//                                sanPhamChiTiet.get("dotGiamGia").get("thoiGianKetThuc"),
//                                sanPhamChiTiet.get("dotGiamGia").get("thoiGianBatDau"),
//                                sanPhamChiTiet.get("dotGiamGia").get("deleted")
//
//                        ),
//                        cb.construct(
//                                SanPhamResponse.SanPhamChiTiet.MauSac.class,
//                                sanPhamChiTiet.get("mauSac").get("id"),
//                                sanPhamChiTiet.get("mauSac").get("ten"),
//                                sanPhamChiTiet.get("mauSac").get("ma")
//
//                        )
//
//                )
//
//        ));
            query.where(cb.and(predicates.toArray(new Predicate[0])));
            query.select(cb.tuple(sanPham.get("id").alias("id"),giaSauGiam)).distinct(true);
            Order order=null;
            if(orderBy.contains("id:desc")){
                order=cb.desc(sanPham.get("id"));
            }else if(orderBy.contains("asc"))
                order=cb.asc(giaSauGiam);
            else
                order=cb.desc(giaSauGiam);
            query.orderBy(order);


//            TypedQuery<Tuple> typeQueryIDSP = em.createQuery(query);
////            typeQueryIDSP.setFirstResult(pageNo*pageSize);
////            typeQueryIDSP.setMaxResults(pageSize);
//
//            List<Integer> lstIDSP=typeQueryIDSP.getResultList().stream().map(tuple -> tuple.get("id",Integer.class)).collect(Collectors.toList());
//

        query.select(cb.tuple(sanPham.get("id").alias("id"),giaSauGiam)).distinct(false);

        query.select(cb.tuple(
                    sanPham.get("id").alias("sanPhamId"),
                    anh.get("url").alias("sanPhamAnhUrl"),
                    anh.get("name").alias("sanPhamAnhName"),
                    sanPham.get("tenSanPham").alias("tenSanPham"),
                    sanPham.get("thoiGianBaoHanh").alias("thoiGianBaoHanh"),
                    sanPham.get("trangThai").alias("sanPhamTrangThai"),
                    sanPhamChiTiet.get("id").alias("spctId"),
                    sanPhamChiTiet.get("rom").alias("spctRom"),
                    sanPhamChiTiet.get("giaBan").alias("spctGiaBan"),
                    sanPhamChiTiet.get("trangThai").alias("spctTrangThai"),
                    dotGiamGia.get("giaTriGiam").alias("giaTriGiam"),
                    dotGiamGia.get("donvi").alias("donvi"),
                    dotGiamGia.get("thoiGianKetThuc").alias("thoiGianKetThuc"),
                    dotGiamGia.get("thoiGianBatDau").alias("thoiGianBatDau"),
                    dotGiamGia.get("deleted").alias("deleted"),
                    mauSac.get("id").alias("msId"),
                    mauSac.get("ten").alias("msTen"),
                    mauSac.get("ma").alias("msMa")

            ));
//        predicates.add(sanPham.get("id").in(lstIDSP));
        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Tuple> typedQuery = em.createQuery(query);


            //Map tuple to resp
        var respTuble=typedQuery.getResultList();

        Map<Integer,SanPhamResponse> mapResp = new LinkedHashMap<>();

        respTuble.forEach(tuple->{
            mapResp.putIfAbsent(tuple.get("sanPhamId",Integer.class),
                    SanPhamResponse.builder()
                            .tenSanPham(tuple.get("tenSanPham",String.class))
                            .id(tuple.get("sanPhamId",Integer.class))
                            .anhUrl(tuple.get("sanPhamAnhUrl",String.class))
                            .trangThai(tuple.get("sanPhamTrangThai", SanPhamRepository.TrangThai.class))
                            .thoiGianBaoHanh(tuple.get("thoiGianBaoHanh",String.class))
                            .anhName(tuple.get("sanPhamAnhName",String.class))
                            .sanPhamChiTiet(new HashSet<>())
                            .build()
                    );
            mapResp.get(tuple.get("sanPhamId",Integer.class)).getSanPhamChiTiet().add(
                    SanPhamResponse.SanPhamChiTiet.builder()
                            .id(tuple.get("spctId",Integer.class))
                            .rom(tuple.get("spctRom",String.class))
                            .giaBan(tuple.get("spctGiaBan", BigDecimal.class))
                            .trangThai(tuple.get("spctTrangThai",SanPhamRepository.TrangThai.class))
                            .dotGiamGia(new SanPhamResponse.SanPhamChiTiet.PhieuGiamGia(
                                    tuple.get("giaTriGiam",BigDecimal.class),
                                    tuple.get("donvi",String.class),
                                    tuple.get("thoiGianKetThuc", Instant.class),
                                    tuple.get("thoiGianBatDau", Instant.class),
                                    tuple.get("deleted", Boolean.class)
                            ))
                            .mauSac(new SanPhamResponse.SanPhamChiTiet.MauSac(
                                    tuple.get("msId",Integer.class),
                                    tuple.get("msTen",String.class),
                                    tuple.get("msMa",String.class)
                            ))
                            .build()
            );
        });

            Pageable pageable=PageRequest.of(pageNo, pageSize);
            Page resp=new PageImpl(new ArrayList( mapResp.values()),pageable,9999);

           return resp;
    }

//    public Page<Select2Response.Result> findTaiKhoanNhanVienByCodeLike(String searchKey, Pageable pageAble) {
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Select2Response.Result> query = cb.createQuery(Select2Response.Result.class);
//        Root<SanPhamChiTiet> root = query.from(SanPhamChiTiet.class);
//        Join<SanPhamChiTiet,SanPham> sp= root.join("sanPham",JoinType.LEFT);
//        Join<SanPhamChiTiet, MauSac> ms=root.join("mauSac",JoinType.LEFT);
//
//        query.select()
//
//
//
//        return null;
//    }
}
