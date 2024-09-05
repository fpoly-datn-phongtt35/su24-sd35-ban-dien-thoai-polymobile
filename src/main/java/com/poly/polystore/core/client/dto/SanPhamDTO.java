package com.poly.polystore.core.client.dto;

import com.poly.polystore.entity.*;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class SanPhamDTO {
    private Integer id;

    private Anh anh;

    private String tenSanPham;

    private BinhLuan binhLuan;

    private ManHinh manHinh;
    private FontCamera cameraTruoc;
    private BackCamera cameraSau;
    private HeDieuHanhVaCpu heDieuHanhVaCpu;

    private KetNoi ketNoi;

    private PinVaSac pinVaSac;
//    private ThongTinChung thongTinChung;

    private List<KhuyenMai> khuyenMai;
    private Series series;

    private String ram;

    private String thoiGianBaoHanh;

    private SanPhamRepository.TrangThai trangThai;

    private String moTa;

    private String tag;

    private Integer stt;
}
