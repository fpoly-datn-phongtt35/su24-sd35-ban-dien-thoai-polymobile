package com.poly.polystore.controller;

import com.poly.polystore.Request.RequestAddKhachHang;
import com.poly.polystore.Response.KhachHangRepose;
import com.poly.polystore.entity.DiaChiGiaoHang;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.repository.DiaChiGiaoHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class KhachHangController {

  private final KhachHangRepository khachHangRepository;
  private final DiaChiGiaoHangRepository diaChiGiaoHangRepository;

  public KhachHangController(KhachHangRepository khachHangRepository,
      DiaChiGiaoHangRepository diaChiGiaoHangRepository) {
    this.khachHangRepository = khachHangRepository;
    this.diaChiGiaoHangRepository = diaChiGiaoHangRepository;
  }


  @GetMapping("/list-khach-hang")
  public String getAll_KhachHang(Model model) {
    List<KhachHangRepose> KhachHangs = khachHangRepository.getAllKhachHang();
    model.addAttribute("KhachHangs", KhachHangs);
    return "index_listKhachHang";
  }

  @GetMapping("/khachhang/danh-sach-den")
  public String getAll_KhachHang_ds_den(Model model) {
   Integer soLuanHuy = 3;
    List<KhachHangRepose> KhachHangs = khachHangRepository.getAllKhachHang_den(soLuanHuy);
    model.addAttribute("KhachHangs", KhachHangs);
    return "index_listKhachHang";
  }

  @GetMapping("view-add-KhachHang")
  public String viewAdd_khachHang(Model model) {
    model.addAttribute("requestAddKhachHang", new RequestAddKhachHang());
    return "add_khachhang";
  }

  @PostMapping("/updateKhachHang")
  public String updateKhachHang(@ModelAttribute("requestAddKhachHang")
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
      Date ngaySinh;
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ngaySinh = sdf.parse(requestAddKhachHang.getNgaySinhInput());
      } catch (ParseException e) {
        // Handle the exception
        e.printStackTrace();
        return "Invalid date format";
      }
      newKhachHang.setNgaySinh(ngaySinh);
      newKhachHang.setSoDienThoai(requestAddKhachHang.getSoDienThoai());
      newKhachHang.setTrangThai(String.valueOf(1));
      KhachHang id_KhachHang = khachHangRepository.save(newKhachHang);

      diaChiGiaoHangRepository.updateDiaChi(requestAddKhachHang.getDiaChi(),
          id_KhachHang.getId());
      return "oke rồi nha";
    }
  }


  // create new a customer
  @PostMapping("add-KhachHang")
  public String addKhachHang(@ModelAttribute("requestAddKhachHang")
  RequestAddKhachHang requestAddKhachHang,@RequestParam("photo") MultipartFile photo
  ) throws IOException {
    KhachHang newKhachHang = new KhachHang();
    if (photo.isEmpty()) {
      return "redirect:/add_khachhang";
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

//    thêm địa chỉ trước

      String newDiaChiInput = requestAddKhachHang.getDiaChi() + " " + requestAddKhachHang.getXa()
          + " " + requestAddKhachHang.getHuyen() + " " + requestAddKhachHang.getThanhPho();
      System.out.println("địa chỉ sau khi công chuỗi" + newDiaChiInput);
      DiaChiGiaoHang newDiaChi = new DiaChiGiaoHang();
      newDiaChi.setDiaChi(newDiaChiInput);
      newDiaChi.setTenNguoiNhan(requestAddKhachHang.getTenNguoiNhan());
      newDiaChi.setSoDienThoai(requestAddKhachHang.getSoDienThoaiNguoiNhan());

      DiaChiGiaoHang idDiaChi = diaChiGiaoHangRepository.save(newDiaChi);
// thêm địa chỉ và id của nó
      Date ngaySinh;
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ngaySinh = sdf.parse(requestAddKhachHang.getNgaySinhInput());
      } catch (ParseException e) {
        // Handle the exception
        e.printStackTrace();
        return "Invalid date format";
      }
      newKhachHang.setTen(requestAddKhachHang.getTenKhachHang());
      newKhachHang.setEmail(requestAddKhachHang.getEmail());
//    thêm id địa chỉ vào bảng khách hàng
      newKhachHang.setIdDiaChi(idDiaChi);

      newKhachHang.setDeteted(0);
      newKhachHang.setNgaySinh(ngaySinh);
      newKhachHang.setSoDienThoai(requestAddKhachHang.getSoDienThoai());
      newKhachHang.setTrangThai("Hoạt Động");
      KhachHang id_KhachHang = khachHangRepository.save(newKhachHang);
      System.out.println(id_KhachHang);

//update id khách hàng tại bảng địa chỉ giao hàng
      diaChiGiaoHangRepository.updateId(id_KhachHang.getId(), idDiaChi.getId());
      return "oke";

    }
  }
  }
