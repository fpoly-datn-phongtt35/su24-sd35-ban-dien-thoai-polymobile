package com.poly.polystore.core.admin.nhan_vien.controller;

import com.poly.polystore.core.admin.nhan_vien.dto.CreateNhanVienRequest;
import com.poly.polystore.entity.NhanVien;
import com.poly.polystore.entity.Role;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.NhanVienRepository;
import com.poly.polystore.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/admin/nhanvien")
@RequiredArgsConstructor
public class NhanVienController {
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhanVienRepository nhanVienRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public String promotions(Model model, @RequestParam(name = "page", defaultValue = "1") int page,
                             @RequestParam(name = "size", defaultValue = "5") int size) {
        Page<NhanVien> nhanViens = nhanVienRepository.findAll(PageRequest.of(page - 1,size));
        model.addAttribute("list", nhanViens);
        return "admin/nhan-vien/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        return "admin/nhan-vien/create";
    }

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody CreateNhanVienRequest createNhanVienRequest) {
        if (nhanVienRepository.findByEmail(createNhanVienRequest.getEmail()) != null) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (nhanVienRepository.findByTenDangNhap(createNhanVienRequest.getLoginName()) != null) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setRole(Role.ROLE_EMPLOYEE);
        taiKhoan.setEmail(createNhanVienRequest.getEmail());
        taiKhoan.setTen(createNhanVienRequest.getName());
        taiKhoan.setMatKhau(passwordEncoder.encode("123456aA@"));
        taiKhoan.setNgaySinh(createNhanVienRequest.getDob().toInstant());
        taiKhoan.setTenDangNhap(createNhanVienRequest.getLoginName());
        taiKhoan.setSoDienThoai(createNhanVienRequest.getSdt());
        taiKhoan.setTrangthai(true);
        TaiKhoan taikhoanDB = taiKhoanRepository.save(taiKhoan);
        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien(UUID.randomUUID().toString());
        nhanVien.setDepartment(createNhanVienRequest.getDepartment());
        nhanVien.setPosition(createNhanVienRequest.getPosition());
        nhanVien.setGioiTinh(createNhanVienRequest.getGender());
        nhanVien.setIdentityCard(createNhanVienRequest.getCccd());
        nhanVien.setIdTaiKhoan(taikhoanDB);
        nhanVien.setDeleted(false);
        nhanVienRepository.save(nhanVien);
        return ResponseEntity.ok(nhanVien.getId());
    }

    @GetMapping("/{id}")
    public String updatePromotionPage(Model model, @PathVariable long id) {
        NhanVien nhanVien = nhanVienRepository.findById((int) id).get();
        model.addAttribute("nhanVien", nhanVien);
        return "admin/nhan-vien/edit";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePromotion(@Valid @RequestBody CreateNhanVienRequest createNhanVienRequest, @PathVariable long id) {
        NhanVien nhanVien = nhanVienRepository.findById((int) id).get();
        nhanVien.setMaNhanVien(UUID.randomUUID().toString());
        nhanVien.setDepartment(createNhanVienRequest.getDepartment());
        nhanVien.setPosition(createNhanVienRequest.getPosition());
        nhanVien.setGioiTinh(createNhanVienRequest.getGender());
        nhanVien.setIdentityCard(createNhanVienRequest.getCccd());
        nhanVienRepository.save(nhanVien);
        TaiKhoan taiKhoan = nhanVien.getIdTaiKhoan();
        taiKhoan.setEmail(createNhanVienRequest.getEmail());
        taiKhoan.setTen(createNhanVienRequest.getName());
        taiKhoan.setMatKhau(passwordEncoder.encode("123456aA@"));
        taiKhoan.setNgaySinh(createNhanVienRequest.getDob().toInstant());
        taiKhoan.setTenDangNhap(createNhanVienRequest.getLoginName());
        taiKhoan.setSoDienThoai(createNhanVienRequest.getSdt());
        taiKhoanRepository.save(taiKhoan);
        return ResponseEntity.ok(nhanVien.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePromotion(@PathVariable long id) {
        NhanVien nhanVien = nhanVienRepository.findById((int) id).get();
        nhanVien.setDeleted(true);
        nhanVienRepository.save(nhanVien);
        return ResponseEntity.ok("Xóa thành công");
    }
}
