package com.poly.polystore.controller;

import com.poly.polystore.Request.RequestAddKhachHang;
import com.poly.polystore.Response.KhachHangRepose;
import com.poly.polystore.entity.DiaChiGiaoHang;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.repository.DiaChiGiaoHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("*")
public class KhachHangController {

  private final KhachHangRepository khachHangRepository;
  private final DiaChiGiaoHangRepository diaChiGiaoHangRepository;

  public KhachHangController(KhachHangRepository khachHangRepository,
      DiaChiGiaoHangRepository diaChiGiaoHangRepository) {
    this.khachHangRepository = khachHangRepository;
    this.diaChiGiaoHangRepository = diaChiGiaoHangRepository;
  }


  @GetMapping("/getAll_khachHang")
  public List<KhachHangRepose> getAll_KhachHang() {
    return khachHangRepository.getAllKhachHang();
  }

  @PostMapping("/addKhachHang")
  public String addKhachHang(@RequestBody
  RequestAddKhachHang requestAddKhachHang, MultipartFile photo) throws IOException {
    KhachHang newKhachHang = new KhachHang();
    if (photo.isEmpty()) {
      return "khong có ảnh";
    } else {

      try {
        Path path = Paths.get("resources/static/Images/");
        String newPhoto = photo.getOriginalFilename();
        newKhachHang.setAnhKhachHang(newPhoto);
        InputStream inputStream = photo.getInputStream();
        Files.copy(inputStream, path.resolve(Objects.requireNonNull(photo.getOriginalFilename())),
            StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
      }

      newKhachHang.setTen(requestAddKhachHang.getTenKhachHang());

      newKhachHang.setEmail(requestAddKhachHang.getEmail());
      newKhachHang.setNgaySinh(requestAddKhachHang.getNgaySinh());
      newKhachHang.setSoDienThoai(requestAddKhachHang.getSoDienThoai());
      newKhachHang.setTrangThai(String.valueOf(1));
      KhachHang id_KhachHang = khachHangRepository.save(newKhachHang);

      DiaChiGiaoHang newDiaChi = new DiaChiGiaoHang();
      newDiaChi.setIdKhachHang(id_KhachHang);
      newDiaChi.setDiaChi(requestAddKhachHang.getDiaChi());
      diaChiGiaoHangRepository.save(newDiaChi);
      return "oke";
    }
  }

  @GetMapping("/delete/{id}")
  public String deleteKhachHang(@PathVariable("id") Integer id) {
    Long idKhachhang = diaChiGiaoHangRepository.deleteDiaChiGiaoHangByIdKhachHang(id);
    khachHangRepository.deleteById(Math.toIntExact(idKhachhang));
    return "remove";
  }

  @PostMapping("/updateKhachHang")
  public String updateKhachHang(@RequestBody
  RequestAddKhachHang requestAddKhachHang, MultipartFile photo) throws IOException {
//     xóa địa chỉ cũ của khách hàng

    KhachHang newKhachHang = new KhachHang();
    if (photo.isEmpty()) {
      return "khong có ảnh";
    } else {

      try {
        Path path = Paths.get("resources/static/Images/");
        String newPhoto = photo.getOriginalFilename();
        newKhachHang.setAnhKhachHang(newPhoto);
        InputStream inputStream = photo.getInputStream();
        Files.copy(inputStream, path.resolve(Objects.requireNonNull(photo.getOriginalFilename())),
            StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        e.printStackTrace();
      }

      newKhachHang.setTen(requestAddKhachHang.getTenKhachHang());
      newKhachHang.setId(requestAddKhachHang.getId());
      newKhachHang.setEmail(requestAddKhachHang.getEmail());
      newKhachHang.setNgaySinh(requestAddKhachHang.getNgaySinh());
      newKhachHang.setSoDienThoai(requestAddKhachHang.getSoDienThoai());
      newKhachHang.setTrangThai(String.valueOf(1));
      KhachHang id_KhachHang = khachHangRepository.save(newKhachHang);

      diaChiGiaoHangRepository.updateDiaChi(requestAddKhachHang.getDiaChi(),
          requestAddKhachHang.getId());
      return "oke";
    }
  }
}
