package com.poly.polystore.core.admin.san_pham.controller;

import com.poly.polystore.core.admin.san_pham.model.reponse.DataList;
import com.poly.polystore.core.admin.san_pham.model.reponse.ResponseDataList;
import com.poly.polystore.entity.SanPham;
import com.poly.polystore.repository.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/data-list-add-san-pham")
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SanPhamChiTietDataListController {
    MauSacRepository mauSacRepository;
    CongNgheManHinhRepository congNgheManHinhRepository;
    BluetoothRepository bluetoothRepository;
    CongNghePinRepository congNghePinRepository;
    CpuRepository cpuRepository;
    GpsRepository gpsRepository;
    MatKinhCamUngRepository matKinhCamUngRepository;
    SeriesRepository seriesRepository;
    TinhNangCameraRepository tinhNangCameraRepository;
    HeDieuHanhRepository heDieuHanhRepository;
    TinhNangDacBietRepository tinhNangDacBietRepository;
    WifiRepository wifiRepository;
    KhuyenMaiRepository khuyenMaiRepository;

    SanPhamController sanPhamController;
    ModelMapper modelMapper;
    SanPhamRepository sanPhamRepository;


    @GetMapping("")
    public ResponseDataList getAll() {

        var lstMauSac = mauSacRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstCongNgheManHinh = congNgheManHinhRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstBluetooth = bluetoothRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstCongNghePin = congNghePinRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstCpu = cpuRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstGps = gpsRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstMatKinhCamUng = matKinhCamUngRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstSeries = seriesRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstTinhNangCamera = tinhNangCameraRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstTinhNangDacBiet = tinhNangDacBietRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstWifi = wifiRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstKhuyenMai = khuyenMaiRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
        var lstHeDieuHanh = heDieuHanhRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());

        // Không phải thuộc tính chính

       Set<String> lstTen= new ConcurrentSkipListSet<>();
       Set<String> lstDoPhanGiai= new ConcurrentSkipListSet<>();
       Set<String> lstManHinhRong= new ConcurrentSkipListSet<>();
       Set<String> lstDoSangToiDa= new ConcurrentSkipListSet<>();
       Set<String> lstDoPhanGiaiCameraTruoc= new ConcurrentSkipListSet<>();
       Set<String> lstDoPhanGiaiCameraSau= new ConcurrentSkipListSet<>();
       Set<String> lstRam= new ConcurrentSkipListSet<>();
       Set<String> lstMangDiDong= new ConcurrentSkipListSet<>();
       Set<String> lstSim= new ConcurrentSkipListSet<>();
       Set<String> lstCongSac= new ConcurrentSkipListSet<>();
       Set<String> lstJackTaiNghe= new ConcurrentSkipListSet<>();
       Set<String> lstDungLuongPin= new ConcurrentSkipListSet<>();
       Set<String> lstLoaiPin= new ConcurrentSkipListSet<>();
       Set<String> lstHoTroSacToiDa= new ConcurrentSkipListSet<>();
        Set<String> lstThietKe= new ConcurrentSkipListSet<>();
        Set<String> lstChatLieu= new ConcurrentSkipListSet<>();
        Set<String> lstKichThuocKhoiLuong= new ConcurrentSkipListSet<>();

//        var lstSanPham =sanPhamRepository.findAllByDeletedIsFalse();
        var lstSanPham =sanPhamRepository.findAll();
        System.out.println(lstSanPham);
        lstSanPham.parallelStream().forEach(sp->{
            lstTen.add(sp.getTenSanPham());
            lstDoPhanGiai.add(sp.getManHinh().getDoPhanGiai());
            lstManHinhRong.add(sp.getManHinh().getManHinhRong());
            lstDoSangToiDa.add(sp.getManHinh().getDoSangToiDa());
            lstDoPhanGiaiCameraTruoc.add(sp.getCameraTruoc().getDoPhanGiai());
            lstDoPhanGiaiCameraSau.add(sp.getCameraSau().getDoPhanGiai());
            lstRam.add(sp.getRam());
            lstMangDiDong.add(sp.getKetNoi().getMangDiDong());
            lstSim.add(sp.getKetNoi().getSim());
            lstCongSac.add(sp.getKetNoi().getCongSac());
            lstJackTaiNghe.add(sp.getKetNoi().getJackTaiNghe());
            lstDungLuongPin.add(sp.getPinVaSac().getDungLuongPin());
            lstLoaiPin.add(sp.getPinVaSac().getLoaiPin());
            lstHoTroSacToiDa.add(sp.getPinVaSac().getHoTroSacToiDa());
        });

       var responseDataList= ResponseDataList.builder()
        .ten(lstTen)
        //Man hinh
        .congNgheManHinh(lstCongNgheManHinh)
        .doPhanGiai(lstDoPhanGiai)
        .manHinhRong(lstManHinhRong)
        .doSangToiDa(lstDoSangToiDa)
        .matKinhCamUng(lstMatKinhCamUng)

        //Camera sau
        .doPhanGiaiCameraTruoc(lstDoPhanGiaiCameraTruoc)
        .tinhNangCamera(lstTinhNangCamera)

        //Camera truoc
        .doPhanGiaiCameraSau(lstDoPhanGiaiCameraSau)

        //HDH va CPU
        .heDieuHanh(lstHeDieuHanh)
        .cpu(lstCpu)

        //Bo nho va Luu Tru
        .ram(lstRam)

        //Ket noi
        .mangDiDong(lstMangDiDong)
        .sim(lstSim)
        .wifi(lstWifi)
        .gps(lstGps)
        .bluetooth(lstBluetooth)
        .congSac(lstCongSac)
        .jackTaiNghe(lstJackTaiNghe)

        //Dung luong pin
        .dungLuongPin(lstDungLuongPin)
        .loaiPin(lstLoaiPin)
        .hoTroSacToiDa(lstHoTroSacToiDa)
        .congNghePin(lstCongNghePin)

        //Tien ich
        .tinhNangDacBiet(lstTinhNangDacBiet)

        //Thong tin chung
        .thietKe(lstThietKe)
        .chatLieu(lstChatLieu)
        .kichThuocKhoiLuong(lstKichThuocKhoiLuong)
        .mauSac(lstMauSac)
        .khuyenMai(lstKhuyenMai)
        .series(lstSeries)
                .build();
        return responseDataList;

    }


    @GetMapping("mau-sac")
    public List<DataList> getMauSac() {
        return mauSacRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());

    }

    @GetMapping("he-dieu-hanh")
    public List<DataList> getHeDieuHanh() {
        return heDieuHanhRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());

    }

    @GetMapping("cong-nghe-man-hinh")
    public List<DataList> getCongNgheManHinh() {
        return congNgheManHinhRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("bluetooth")
    public List<DataList> getBluetooth() {
        return bluetoothRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("cong-nghe-pin")
    public List<DataList> getCongNghePin() {
        return congNghePinRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("cpu")
    public List<DataList> getCPU() {
        return cpuRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("gps")
    public List<DataList> getGPS() {
        return gpsRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("mat-kinh-cam-ung")
    public List<DataList> getMatKinhCamUng() {
        return matKinhCamUngRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("series")
    public List<DataList> getSeries() {
        return seriesRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("tinh-nang-camera")
    public List<DataList> getTinhNangCamera() {
        return tinhNangCameraRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("tinh-nang-dac-biet")
    public List<DataList> getTinhNagDacBiet() {
        return tinhNangDacBietRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }

    @GetMapping("wifi")
    public List<DataList> getWifi() {
        return wifiRepository.findAllByDeletedIsFalse()
                .stream()
                .map(element->new DataList(element.getId(),element.getTen()))
                .collect(Collectors.toList());
    }



}

