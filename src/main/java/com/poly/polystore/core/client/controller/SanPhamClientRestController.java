package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.client.models.response.SanPhamProductResponse;
import com.poly.polystore.core.client.models.response.SanPhamResponse;
import com.poly.polystore.entity.SanPham;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/san-pham")
@RequiredArgsConstructor
public class SanPhamClientRestController {
    private final SanPhamRepository sanPhamRepository;
    private final ModelMapper modelMapper;

    private final Integer pageSize=9;

    @GetMapping("")
    public ResponseEntity<?> getSanPham(@RequestParam(name="page",defaultValue = "0",required = false) Integer pageNo) {
        Sort sort=Sort.by(Sort.Direction.DESC,"id");
        Pageable page= PageRequest.of(pageNo, pageSize,sort);
        var spfc=SanPhamSpecifications.sanPhamStatusIn(
                SanPhamRepository.TrangThai.COMING_SOON,
                SanPhamRepository.TrangThai.IN_STOCK,
                SanPhamRepository.TrangThai.TEMPORARILY_OUT_OF_STOCK
        );
        var sp= sanPhamRepository.findAll(spfc,
                page).getContent().stream().map((spRepo) -> {
            var spResponse=modelMapper.map(spRepo, SanPhamResponse.class);
            spResponse.setSanPhamChiTiet(spRepo.getSanPhamChiTiet().stream().map((spctRepo) -> {
                var spctResp=modelMapper.map(spctRepo, SanPhamResponse.SanPhamChiTiet.class);
                modelMapper.map(spctResp, SanPhamResponse.SanPhamChiTiet.PhieuGiamGia.class);
                return spctResp;
            }).collect(Collectors.toSet()));
            return spResponse;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(sp);
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
