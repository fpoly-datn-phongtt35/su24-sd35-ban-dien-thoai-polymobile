package com.poly.polystore.core.admin.nhan_vien.controller;

import com.azure.core.exception.ResourceNotFoundException;
import com.poly.polystore.core.admin.nhan_vien.dto.CreateNhanVienRequest;
import com.poly.polystore.entity.NhanVien;
import com.poly.polystore.Constant.ROLE;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.NhanVienRepository;
import com.poly.polystore.repository.TaiKhoanRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.qpid.proton.amqp.transport.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
    public String listNhanVien(Model model,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size) {

        int pageNumber = Math.max(0, page);
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<NhanVien> nhanViens = nhanVienRepository.findAll(pageRequest);

        if (pageNumber >= nhanViens.getTotalPages()) {
            pageNumber = nhanViens.getTotalPages() - 1;
            pageRequest = PageRequest.of(pageNumber, size);
            nhanViens = nhanVienRepository.findAll(pageRequest);
        }

        model.addAttribute("list", nhanViens);
        model.addAttribute("size", size);

        return "admin/nhan-vien/list";
    }

    @GetMapping("/search")
    public String findby_name(Model model,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "searchQuery", required = false) String searchQuery) {

        int pageNumber = Math.max(0, page);
        PageRequest pageRequest = PageRequest.of(pageNumber, size);

        Page<NhanVien> nhanViens;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Gọi phương thức tìm kiếm trong repository
            nhanViens = nhanVienRepository.findByMaNhanVienContainingOrIdTaiKhoan_TenContaining(searchQuery, searchQuery, pageRequest);
        } else {
            // Nếu không có từ khóa tìm kiếm, lấy tất cả nhân viên
            nhanViens = nhanVienRepository.findAll(pageRequest);
        }
        if (pageNumber >= nhanViens.getTotalPages() && nhanViens.getTotalPages() > 0) {
            pageNumber = nhanViens.getTotalPages() - 1;
            pageRequest = PageRequest.of(pageNumber, size);
            if (searchQuery != null && !searchQuery.isEmpty()) {
                nhanViens = nhanVienRepository.findByMaNhanVienContainingOrIdTaiKhoan_TenContaining(searchQuery, searchQuery, pageRequest);
            } else {
                nhanViens = nhanVienRepository.findAll(pageRequest);
            }
        }
        model.addAttribute("list", nhanViens);
        model.addAttribute("size", size);
        model.addAttribute("searchQuery", searchQuery);

        return "admin/nhan-vien/list";
    }
    @GetMapping("/details/{id}")
    public String viewDetails(@PathVariable("id") Integer id, Model model) {
        // Tìm nhân viên theo ID
        Optional<NhanVien> nhanVienOpt = nhanVienRepository.findById(id);

        // Kiểm tra nếu nhân viên tồn tại
        if (nhanVienOpt.isPresent()) {
            // Nếu có, thêm vào model và trả về trang chi tiết
            model.addAttribute("nhanVien", nhanVienOpt.get());
            return "admin/nhan-vien/details";
        } else {
            // Nếu không tìm thấy, chuyển hướng hoặc thông báo lỗi
            model.addAttribute("errorMessage", "Nhân viên không tồn tại");
            return "redirect:/admin/nhanvien/list"; // Chuyển hướng về danh sách nhân viên
        }
    }



    @PostMapping("/updateRole/{id}")
    public String updateRole(@PathVariable("id") Integer id, @RequestParam("role") String role) {
        try {
            ROLE newRole = ROLE.valueOf(role);
            NhanVien nhanVien = nhanVienRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

            TaiKhoan taiKhoan = nhanVien.getIdTaiKhoan();
            taiKhoan.setRole(newRole);
            taiKhoanRepository.save(taiKhoan);

            return "redirect:/admin/nhanvien/list"; // Redirect back to the list page
        } catch (Exception e) {
            // Handle the error properly
            return "error"; // Return to an error page or handle it appropriately
        }
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
        taiKhoan.setRole(ROLE.ROLE_EMPLOYEE);
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
