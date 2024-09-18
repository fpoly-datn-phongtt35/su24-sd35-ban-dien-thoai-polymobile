package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.admin.ban_hang.repository.impl.SanPhamRepositoryImpl;
import com.poly.polystore.core.client.models.response.SanPhamProductResponse;
import com.poly.polystore.core.client.models.response.SanPhamResponse;
import com.poly.polystore.entity.SanPham;
import com.poly.polystore.entity.SanPhamChiTiet;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/san-pham")
@RequiredArgsConstructor
public class SanPhamClientRestController {
    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;

    private final Integer pageSize=9;
    private final SanPhamRepositoryImpl sanPhamRepositoryImpl;

    @GetMapping("")
    public ResponseEntity<?> getSanPham(
            @RequestParam(defaultValue = "0",required = false) Integer pageNo,//Số trang
            @RequestParam(defaultValue = "",name="price", required = false) String khoangGia,//Khoảng giá 20,300
            @RequestParam(defaultValue = "",name="cns", required = false) List<String> hoTroSacToiDa,//20w,30w
            @RequestParam(defaultValue = "",name="tndb", required = false) List<Integer> tinhNangDacBiets,//id=3,4,5
            @RequestParam(defaultValue = "",name="tncmr", required = false) List<Integer> tinhNangCameras,//id=3,4,5
            @RequestParam(defaultValue = "",name="manHinh", required = false) List<String> kichThuocManHinh,//id=3,4,5
            @RequestParam(defaultValue = "",name="series", required = false) List<Integer> series,//id=3,4,5
            @RequestParam(defaultValue = "",name="rom", required = false) List<String> rom,//rom=64gb,256gb
            @RequestParam(defaultValue = "id:desc",name="orderBy", required = false) String orderBy,// id || bestSale || promotion || priceAsc || priceDESC
            @RequestParam(defaultValue = "",name="searchKey", required = false) String searchKey,
            @RequestParam(defaultValue = "15",name="pageSize", required = false) Integer pageSize

            ) {

        Page<SanPhamResponse> resp= sanPhamRepositoryImpl.search(pageNo,khoangGia,hoTroSacToiDa,tinhNangDacBiets,tinhNangCameras,kichThuocManHinh,series,rom,orderBy,searchKey,pageSize);

        return ResponseEntity.ok(resp);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getSanPhamById(
            @PathVariable(name="id") SanPham sanPham
    ) {
        var spResponse=modelMapper.map(sanPham, SanPhamProductResponse.class);
        return ResponseEntity.ok(spResponse);
    }

    public class SanPhamSpecifications{
        public static Specification<SanPham> sanPhamStatusIn(SanPhamRepository.TrangThai... trangThais ) {
            return (root, query, criteriaBuilder) -> {
                Predicate[] predicates=new Predicate[trangThais.length];
                for (int i = 0; i < trangThais.length; i++) {
                    predicates[i]=criteriaBuilder.equal(root.get("trangThai"), trangThais[i]);
                }
                return criteriaBuilder.or(predicates);
            };
        }
    }
}
