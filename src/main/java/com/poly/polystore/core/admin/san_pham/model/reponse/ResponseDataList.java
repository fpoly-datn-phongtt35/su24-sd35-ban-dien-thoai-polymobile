package com.poly.polystore.core.admin.san_pham.model.reponse;

import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * DTO for {@link com.poly.polystore.entity.SanPham}
 */

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class ResponseDataList {
    Set<String> ten;
    //Man hinh
    List<DataList> congNgheManHinh;
    Set<String> doPhanGiai;
    Set<String> manHinhRong;
    Set<String> doSangToiDa;
    List<DataList> matKinhCamUng;

    //Camera sau
    Set<String> doPhanGiaiCameraTruoc;
    List<DataList> tinhNangCamera;

    //Camera truoc
    Set<String> doPhanGiaiCameraSau;

    //HDH va CPU
    List<DataList> heDieuHanh;
    List<DataList> cpu;

    //Bo nho va Luu Tru
    Set<String> ram;

    //Ket noi
    Set<String> mangDiDong;
    Set<String> sim;
    List<DataList> wifi;
    List<DataList> gps;
    List<DataList> bluetooth;
    Set<String> congSac;
    Set<String> jackTaiNghe;

    //Dung luong pin
    Set<String> dungLuongPin;
    Set<String> loaiPin;
    Set<String> hoTroSacToiDa;
    List<DataList> congNghePin;

    //Tien ich
    List<DataList> tinhNangDacBiet;

    //Thong tin chung
    Set<String> thietKe;
    Set<String> chatLieu;
    Set<String> kichThuocKhoiLuong;
    List<DataList> mauSac;
    List<DataList> khuyenMai;
    List<DataList> series;
}

