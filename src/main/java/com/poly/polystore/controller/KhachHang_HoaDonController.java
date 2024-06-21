package com.poly.polystore.controller;


import com.poly.polystore.Response.HoaDonResponse;
import com.poly.polystore.repository.HoaDonRepository;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class KhachHang_HoaDonController {

  private final HoaDonRepository hoaDonRepository;

  public KhachHang_HoaDonController(HoaDonRepository hoaDonRepository) {
    this.hoaDonRepository = hoaDonRepository;
  }


  // hien thi tat ca
  @GetMapping("/lich-su-hoa-don")
  public String lichSuHoaDon(Model model) {
    List<HoaDonResponse> list = new ArrayList<>();
    list = hoaDonRepository.lichSuDHoaDon();
    for (HoaDonResponse hoaDonResponse : list) {
      System.out.println(hoaDonResponse.getTongTienHoaDon());
      System.out.println(hoaDonResponse.getAnhSanPham());
      System.out.println(hoaDonResponse.getIDHoaDon());
    }
    model.addAttribute("listLichSu", list);
    return "lichSuMuaHang";
  }

  // hien thi theo cac muc khac
  @GetMapping("/lich-su-hoa-donWhere/{trangthai}")
  public String lichSuHoaDon(Model model, @PathVariable("trangthai") String trangthai) {
    List<HoaDonResponse> list = new ArrayList<>();
    list = hoaDonRepository.lichSuDHoaDonTT(trangthai);

    model.addAttribute("listLichSu", list);
    return "lichSuMuaHang";
  }

  @GetMapping("/lich-su-hoa-donWhereID/{Id}")
  public String lichSuHoaDonChiTiet(Model model, @PathVariable("Id") String Id) {
    HoaDonResponse list = hoaDonRepository.lichSuDHoaDonID(Id);
    System.out.println(list.getThoiGianMuaHang());
    model.addAttribute("hoaDonbyID", list);
    return "hoaDonChiTiet";
  }
}
