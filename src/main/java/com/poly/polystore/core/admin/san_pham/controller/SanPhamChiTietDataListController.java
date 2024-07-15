package com.poly.polystore.core.admin.san_pham.controller;

import com.poly.polystore.core.admin.san_pham.model.reponse.DataList;
import com.poly.polystore.core.admin.san_pham.model.reponse.DataSourcesSelect2;
import com.poly.polystore.core.admin.san_pham.model.reponse.ResponseDataList;
import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamTemplate;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
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

    @GetMapping("/{id}")
    public SanPhamTemplate getSanPhamTemplate(@PathVariable(name = "id") Integer id) {
        var sp = sanPhamRepository.findById(id);
        var sanPhamTemplate = sp
                .map((element) -> modelMapper.map(element, SanPhamTemplate.class)).orElse(null);
        sanPhamTemplate.setCameraSauTinhNangCameraIds(sp.get().getCameraTruoc()
                .getTinhNangCameras()
                .stream()
                .map(TinhNangCamera::getId)
                .collect(Collectors.toSet()));
        sanPhamTemplate.setCameraTruocTinhNangCameraIds(sp.get().getCameraSau()
                .getTinhNangCameras()
                .stream()
                .map(TinhNangCamera::getId)
                .collect(Collectors.toSet()));
        sanPhamTemplate.setKetNoiWifiIds(sp.get()
                .getKetNoi()
                .getWifi()
                .stream()
                .map(Wifi::getId)
                .collect(Collectors.toSet()));
        sanPhamTemplate.setKetNoiGpsIds(sp.get()
                .getKetNoi()
                .getGps()
                .stream()
                .map(Gps::getId)
                .collect(Collectors.toSet()));
        sanPhamTemplate.setKetNoiBluetoothIds(sp.get()
                .getKetNoi()
                .getBluetooth()
                .stream()
                .map(Bluetooth::getId)
                .collect(Collectors.toSet()));
        sanPhamTemplate.setPinVaSacCongNghePinIds(sp.get()
                .getPinVaSac()
                .getCongNghePin()
                .stream()
                .map(CongNghePin::getId)
                .collect(Collectors.toSet()));
        sanPhamTemplate.setKhuyenMaiIds(sp.get()
                .getKhuyenMai()
                .stream()
                .map(KhuyenMai::getId)
                .collect(Collectors.toList()));
        sanPhamTemplate.setSanPhamChiTietRoms(sp.get()
                .getSanPhamChiTiet()
                .stream()
                .map(SanPhamChiTiet::getRom)
                .collect(Collectors.toSet())
        );
        sanPhamTemplate.setSanPhamChiTietMauSacIds(sp.get()
                .getSanPhamChiTiet()
                .stream()
                .map(SanPhamChiTiet::getMauSac)
                .map(MauSac::getId)
                .collect(Collectors.toSet())
        );
        sanPhamTemplate.setThongTinChungTinhNangDacBietIds(sp.get()
                .getThongTinChung()
                .getTinhNangDacBiet()
                .stream()
                .map(tndb->tndb.getId())
                .collect(Collectors.toSet())
        );
        sanPhamTemplate.setRam(sp.get().getRam());
        sanPhamTemplate.setThoiGianBaoHanh(sp.get().getThoiGianBaoHanh());


        return sanPhamTemplate;
    }

    @GetMapping("")
    public ResponseDataList getAll() {

        var lstMauSac = mauSacRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstCongNgheManHinh = congNgheManHinhRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstBluetooth = bluetoothRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstCongNghePin = congNghePinRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstCpu = cpuRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstGps = gpsRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstMatKinhCamUng = matKinhCamUngRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstSeries = seriesRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstTinhNangCamera = tinhNangCameraRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstTinhNangDacBiet = tinhNangDacBietRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstWifi = wifiRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstKhuyenMai = khuyenMaiRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstHeDieuHanh = heDieuHanhRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        var lstSanPhamDataList = sanPhamRepository.findAllByOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTenSanPham()))
                .collect(Collectors.toList());
        // Không phải thuộc tính chính
        Set<String> lstTen = new ConcurrentSkipListSet<>();
        Set<String> lstDoPhanGiai = new ConcurrentSkipListSet<>();
        Set<String> lstManHinhRong = new ConcurrentSkipListSet<>();
        Set<String> lstDoSangToiDa = new ConcurrentSkipListSet<>();
        Set<String> lstDoPhanGiaiCameraTruoc = new ConcurrentSkipListSet<>();
        Set<String> lstDoPhanGiaiCameraSau = new ConcurrentSkipListSet<>();
        Set<String> lstRam = new ConcurrentSkipListSet<>();
        Set<String> lstMangDiDong = new ConcurrentSkipListSet<>();
        Set<String> lstSim = new ConcurrentSkipListSet<>();
        Set<String> lstCongSac = new ConcurrentSkipListSet<>();
        Set<String> lstJackTaiNghe = new ConcurrentSkipListSet<>();
        Set<String> lstDungLuongPin = new ConcurrentSkipListSet<>();
        Set<String> lstLoaiPin = new ConcurrentSkipListSet<>();
        Set<String> lstHoTroSacToiDa = new ConcurrentSkipListSet<>();
        Set<String> lstThietKe = new ConcurrentSkipListSet<>();
        Set<String> lstChatLieu = new ConcurrentSkipListSet<>();
        Set<String> lstKichThuocKhoiLuong = new ConcurrentSkipListSet<>();
        Set<String> lstFlash = new ConcurrentSkipListSet<>();
        Set<String> lstThoiGianBaoHanh = new ConcurrentSkipListSet<>();

//        var lstSanPham =sanPhamRepository.findAllByDeletedIsFalseOrderByIdDesc();
        var lstSanPham = sanPhamRepository.findAll();
        System.out.println(lstSanPham);
        lstSanPham.parallelStream().forEach(sp -> {
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
            lstFlash.add(sp.getCameraSau().getDenFlash());
            lstThietKe.add(sp.getThongTinChung().getThietKe());
            lstChatLieu.add(sp.getThongTinChung().getChatLieu());
            lstKichThuocKhoiLuong.add(sp.getThongTinChung().getKichThuocKhoiLuong());
            lstThoiGianBaoHanh.add(sp.getThoiGianBaoHanh());
        });

        var responseDataList = ResponseDataList.builder()
                .sanPham(lstSanPhamDataList)
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
                .denFlash(lstFlash)

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
                .thoiGianBaoHanh(lstThoiGianBaoHanh)
                .build();
        return responseDataList;

    }


    @GetMapping("mau-sac")
    public DataSourcesSelect2 getMauSac() {
        var dataList = mauSacRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));

    }

    @GetMapping("he-dieu-hanh")
    public DataSourcesSelect2 getHeDieuHanh() {
        var dataList = heDieuHanhRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));

    }

    @GetMapping("cong-nghe-man-hinh")
    public DataSourcesSelect2 getCongNgheManHinh() {
        var dataList = congNgheManHinhRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("bluetooth")
    public DataSourcesSelect2 getBluetooth() {
        var dataList = bluetoothRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("cong-nghe-pin")
    public DataSourcesSelect2 getCongNghePin() {
        var dataList = congNghePinRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("cpu")
    public DataSourcesSelect2 getCPU() {
        var dataList = cpuRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("gps")
    public DataSourcesSelect2 getGPS() {
        var dataList = gpsRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("mat-kinh-cam-ung")
    public DataSourcesSelect2 getMatKinhCamUng() {
        var dataList = matKinhCamUngRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("series")
    public DataSourcesSelect2 getSeries() {
        var dataList = seriesRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("tinh-nang-camera")
    public DataSourcesSelect2 getTinhNangCamera() {
        var dataList = tinhNangCameraRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("tinh-nang-dac-biet")
    public DataSourcesSelect2 getTinhNagDacBiet() {
        var dataList = tinhNangDacBietRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());
        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("wifi")
    public DataSourcesSelect2 getWifi() {
        var dataList = wifiRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTen()))
                .collect(Collectors.toList());

        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }

    @GetMapping("san-pham")
    public DataSourcesSelect2 getSanPham() {
        var dataList = sanPhamRepository.findAllByOrderByIdDesc()
                .stream()
                .map(element -> new DataList(element.getId(), element.getTenSanPham()))
                .collect(Collectors.toList());

        return new DataSourcesSelect2(dataList, new DataSourcesSelect2.Pagination(false));
    }


}

