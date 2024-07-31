package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.client.models.request.RequestAddKhachHang;
import com.poly.polystore.core.client.models.response.KhachHangRepose;
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


    @GetMapping("/khach-hang")
    public String getAll_KhachHang(Model model) {
        List<KhachHangRepose> KhachHangs = khachHangRepository.getAllKhachHang();
        model.addAttribute("KhachHangs", KhachHangs);
        return "index_listKhachHang";
    }

    @GetMapping("view-add-KhachHang")
    public String viewAdd_khachHang(Model model) {
        model.addAttribute("requestAddKhachHang", new RequestAddKhachHang());
        return "add_khachhang";
    }

//  @PostMapping("add-KhachHang")
//  public String addKhachHang(@RequestBody
//  RequestAddKhachHang requestAddKhachHang
//      ,@RequestParam("anhKhachHang") MultipartFile anhKhachHang
//  ) throws IOException {
//    KhachHang newKhachHang = new KhachHang();
//    if (anhKhachHang.isEmpty()) {
//      return "redirect:/add_khachhang";
//    } else {
//      try {
//        Path path = Paths.get("resources/static/Images/");
//        String newPhoto = anhKhachHang.getOriginalFilename();
//        newKhachHang.setAnhKhachHang(newPhoto);
//        InputStream inputStream = anhKhachHang.getInputStream();
//        Files.copy(inputStream, path.resolve(Objects.requireNonNull(anhKhachHang.getOriginalFilename())),
//            StandardCopyOption.REPLACE_EXISTING);
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//
//      newKhachHang.setTen(requestAddKhachHang.getTenKhachHang());
//
//      newKhachHang.setEmail(requestAddKhachHang.getEmail());
//      newKhachHang.setNgaySinh(requestAddKhachHang.getNgaySinh());
//      newKhachHang.setSoDienThoai(requestAddKhachHang.getSoDienThoai());
//      newKhachHang.setTrangThai(String.valueOf(1));
//      KhachHang id_KhachHang = khachHangRepository.save(newKhachHang);
//
//      DiaChiGiaoHang newDiaChi = new DiaChiGiaoHang();
//      newDiaChi.setIdKhachHang(id_KhachHang);
//      newDiaChi.setDiaChi(requestAddKhachHang.getDiaChi());
//      diaChiGiaoHangRepository.save(newDiaChi);
//      return "add_khachhang";
//    }
//  }

//  @GetMapping("/delete/{id}")
//  public String deleteKhachHang(@PathVariable("id") Integer id) {
//    Long idKhachhang = diaChiGiaoHangRepository.deleteDiaChiGiaoHangByIdKhachHang(id);
//    khachHangRepository.deleteById(Math.toIntExact(idKhachhang));
//    return "remove";
//  }

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
            newKhachHang.setSoDienThoai(requestAddKhachHang.getSoDienThoai());
            KhachHang id_KhachHang = khachHangRepository.save(newKhachHang);

            diaChiGiaoHangRepository.updateDiaChi(requestAddKhachHang.getDiaChi(),
                    requestAddKhachHang.getId());
            return "oke rồi nha";
        }
    }


    // create new a customer
    @PostMapping("add-KhachHang")
    public String addKhachHang(@ModelAttribute("requestAddKhachHang")
                               RequestAddKhachHang requestAddKhachHang
    ) throws IOException {
        KhachHang newKhachHang = new KhachHang();

        newKhachHang.setTen(requestAddKhachHang.getTenKhachHang());
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
        newKhachHang.setSoDienThoai(requestAddKhachHang.getSoDienThoai());
        khachHangRepository.save(newKhachHang);
//    if(id_KhachHang.getId()!=0) {
//      return "redirect:/view-add-KhachHang";
//    }
        DiaChiGiaoHang newDiaChi = new DiaChiGiaoHang();
//    newDiaChi.setIdKhachHang(id_KhachHang);
        newDiaChi.setDiaChi(requestAddKhachHang.getDiaChi());
        diaChiGiaoHangRepository.save(newDiaChi);
        return "add_khachhang";

    }
}
