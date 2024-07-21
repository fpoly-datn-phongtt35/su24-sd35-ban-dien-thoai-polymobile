package com.poly.polystore.core.admin.san_pham.controller;

import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamDataTable;
import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamDto;
import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamTemplate;
import com.poly.polystore.core.admin.san_pham.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham.model.request.SanPhamEditRequest;
import com.poly.polystore.core.common.image.service.ImageService;
import com.poly.polystore.entity.*;
import com.poly.polystore.repository.AnhRepository;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class SanPhamController {
    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);
    private final ModelMapper modelMapper;
    private final SanPhamRepository sanPhamRepository;
    private final EntityManager entityManager;
    private final ImageService imageService;
    private final AnhRepository anhRepository;

    @GetMapping("/admin/san-pham")
    public String ui(Model model) {
        return "/admin/san-pham/san-pham";
    }

    @GetMapping("/admin/san-pham/add")
    public String add(Model model) {
        return "/admin/san-pham/add";
    }
    @GetMapping("/admin/san-pham/{id}")
    public String edit(Model model,@PathVariable(name = "id") SanPham sp) {
        model.addAttribute("moTa",sp.getMoTa());

        return "/admin/san-pham/edit-san-pham";
    }

    @ResponseBody
    @GetMapping("/api/v1/admin/san-pham/{id}")
    public ResponseEntity<?> getSanPhamEdit(@PathVariable(name = "id") SanPham sp) {
        var spEditResp = modelMapper.map(sp, SanPhamDto.class);
        spEditResp.setCameraSauTinhNangCameraIds(sp.getCameraTruoc()
                .getTinhNangCameras()
                .stream()
                .map(TinhNangCamera::getId)
                .collect(Collectors.toSet()));
        spEditResp.setCameraTruocTinhNangCameraIds(sp.getCameraSau()
                .getTinhNangCameras()
                .stream()
                .map(TinhNangCamera::getId)
                .collect(Collectors.toSet()));
        spEditResp.setKetNoiWifiIds(sp
                .getKetNoi()
                .getWifi()
                .stream()
                .map(Wifi::getId)
                .collect(Collectors.toSet()));
        spEditResp.setKetNoiGpsIds(sp
                .getKetNoi()
                .getGps()
                .stream()
                .map(Gps::getId)
                .collect(Collectors.toSet()));
        spEditResp.setKetNoiBluetoothIds(sp
                .getKetNoi()
                .getBluetooth()
                .stream()
                .map(Bluetooth::getId)
                .collect(Collectors.toSet()));
        spEditResp.setPinVaSacCongNghePinIds(sp
                .getPinVaSac()
                .getCongNghePin()
                .stream()
                .map(CongNghePin::getId)
                .collect(Collectors.toSet()));
        spEditResp.setKhuyenMaiIds(sp
                .getKhuyenMai()
                .stream()
                .map(KhuyenMai::getId)
                .collect(Collectors.toList()));
        spEditResp.setSanPhamChiTietRoms(sp
                .getSanPhamChiTiet()
                .stream()
                .map(SanPhamChiTiet::getRom)
                .collect(Collectors.toSet())
        );
        spEditResp.setSanPhamChiTietMauSacIds(sp
                .getSanPhamChiTiet()
                .stream()
                .map(SanPhamChiTiet::getMauSac)
                .map(MauSac::getId)
                .collect(Collectors.toSet())
        );
        spEditResp.setThongTinChungTinhNangDacBietIds(sp
                .getThongTinChung()
                .getTinhNangDacBiet()
                .stream()
                .map(tndb->tndb.getId())
                .collect(Collectors.toSet())
        );
        spEditResp.setRam(sp.getRam());
        spEditResp.setThoiGianBaoHanh(sp.getThoiGianBaoHanh());




        return ResponseEntity.ok(spEditResp);
    }

    @Transactional
    @ResponseBody
    @PutMapping("/api/v1/san-pham")
    public ResponseEntity<?> addNew(@RequestBody AddRequest addRequest) {
        var sp= modelMapper.map(addRequest, SanPham.class);

        sp.getCameraTruoc().setTinhNangCameras(addRequest
                .getCameraTruocTinhNangCameraIds()
                .stream().map(id-> {
                    var tinhNangCamera=new TinhNangCamera();
                    tinhNangCamera.setId(id);
                    return tinhNangCamera;
                })
                .collect(Collectors.toSet())
        );

        sp.getCameraSau().setTinhNangCameras(addRequest
                .getCameraSauTinhNangCameraIds()
                .stream().map(id-> {
                    var tinhNangCamera=new TinhNangCamera();
                    tinhNangCamera.setId(id);
                    return tinhNangCamera;
                })
                .collect(Collectors.toSet())
        );
        sp.getKetNoi().setBluetooth(addRequest
                .getKetNoiBluetoothIds()
                .stream().map(id-> {
                    var bluetooth=new Bluetooth();
                    bluetooth.setId(id);
                    return bluetooth;
                })
                .collect(Collectors.toSet())

        );
        sp.getKetNoi().setWifi(addRequest
                .getKetNoiWifiIds()
                .stream().map(id-> {
                    var wifi=new Wifi();
                    wifi.setId(id);
                    return wifi;
                })
                .collect(Collectors.toSet())

        );
        sp.getKetNoi().setGps(addRequest
                .getKetNoiGpsIds()
                .stream().map(id-> {
                    var gps=new Gps();
                    gps.setId(id);
                    return gps;
                })
                .collect(Collectors.toSet())

        );
        sp.getPinVaSac().setCongNghePin(addRequest
                .getPinVaSacCongNghePinIds()
                .stream().map(id-> {
                    var congNghePin=new CongNghePin();
                    congNghePin.setId(id);
                    return congNghePin;
                })
                .collect(Collectors.toSet())

        );
        sp.getThongTinChung().setTinhNangDacBiet(addRequest
                .getThongTinChungTinhNangDacBietIds()
                .stream().map(id-> {
                    var tinhNangDacBiet=new TinhNangDacBiet();
                    tinhNangDacBiet.setId(id);
                    return tinhNangDacBiet;
                })
                .collect(Collectors.toSet())

        );

        sp.getCameraSau().setDenFlash(addRequest.getCameraSauDenFlash());

        //Đưa ảnh về container chính
        var anhSanPhamAddRquest=new Anh();
        anhSanPhamAddRquest.setName(addRequest.getAnhName());
        anhSanPhamAddRquest.setUrl(imageService.moveImageToPermanent(addRequest.getAnhName()));
        var anhSanPham = anhRepository.save(anhSanPhamAddRquest);

        sp.setAnh(anhSanPham);


        var lstSPCT=addRequest.getSanPhamChiTiet().stream().map(spctAddRequest->{
            //Lưu ảnh trả về list Ảnh
            var lstAnh= spctAddRequest.getAnh().stream().map(imageName->{
                var newImage=new Anh();
                newImage.setName(imageName);
                newImage.setUrl(imageService.moveImageToPermanent(imageName));
                return anhRepository.save(newImage);
            }).collect(Collectors.toList());

            //Chuyển lai list khuyến mãi
            var lstKhuyenMai=spctAddRequest.getKhuyenMaiIds().stream().map(khuyenMaiId->{
                var newKhuyenMai=new KhuyenMai();
                newKhuyenMai.setId(khuyenMaiId);
                return newKhuyenMai;
            }).collect(Collectors.toList());

            var spct=modelMapper.map(spctAddRequest, SanPhamChiTiet.class);
            spct.setKhuyenMai(lstKhuyenMai);
            spct.setAnh(lstAnh);
            spct.setSanPham(sp);
            return spct;
        }).collect(Collectors.toSet());


        sp.setSanPhamChiTiet(lstSPCT);
        var spResponse=sanPhamRepository.save(sp);
        return ResponseEntity.ok(spResponse);
    }

    @Transactional
    @ResponseBody
    @PostMapping("/api/v1/san-pham/{id}")
    public ResponseEntity<?> edit(@RequestBody AddRequest addRequest,@PathVariable(name="id") SanPham sp) {
        var spe= modelMapper.map(sp, SanPhamEditRequest.class);
//
//        sp.getCameraTruoc().setTinhNangCameras(addRequest
//                .getCameraTruocTinhNangCameraIds()
//                .stream().map(id-> {
//                    var tinhNangCamera=new TinhNangCamera();
//                    tinhNangCamera.setId(id);
//                    return tinhNangCamera;
//                })
//                .collect(Collectors.toSet())
//        );
//
//        sp.getCameraSau().setTinhNangCameras(addRequest
//                .getCameraSauTinhNangCameraIds()
//                .stream().map(id-> {
//                    var tinhNangCamera=new TinhNangCamera();
//                    tinhNangCamera.setId(id);
//                    return tinhNangCamera;
//                })
//                .collect(Collectors.toSet())
//        );
//        sp.getKetNoi().setBluetooth(addRequest
//                .getKetNoiBluetoothIds()
//                .stream().map(id-> {
//                    var bluetooth=new Bluetooth();
//                    bluetooth.setId(id);
//                    return bluetooth;
//                })
//                .collect(Collectors.toSet())
//
//        );
//        sp.getKetNoi().setWifi(addRequest
//                .getKetNoiWifiIds()
//                .stream().map(id-> {
//                    var wifi=new Wifi();
//                    wifi.setId(id);
//                    return wifi;
//                })
//                .collect(Collectors.toSet())
//
//        );
//        sp.getKetNoi().setGps(addRequest
//                .getKetNoiGpsIds()
//                .stream().map(id-> {
//                    var gps=new Gps();
//                    gps.setId(id);
//                    return gps;
//                })
//                .collect(Collectors.toSet())
//
//        );
//        sp.getPinVaSac().setCongNghePin(addRequest
//                .getPinVaSacCongNghePinIds()
//                .stream().map(id-> {
//                    var congNghePin=new CongNghePin();
//                    congNghePin.setId(id);
//                    return congNghePin;
//                })
//                .collect(Collectors.toSet())
//
//        );
//        sp.getThongTinChung().setTinhNangDacBiet(addRequest
//                .getThongTinChungTinhNangDacBietIds()
//                .stream().map(id-> {
//                    var tinhNangDacBiet=new TinhNangDacBiet();
//                    tinhNangDacBiet.setId(id);
//                    return tinhNangDacBiet;
//                })
//                .collect(Collectors.toSet())
//
//        );
//
//        sp.getCameraSau().setDenFlash(addRequest.getCameraSauDenFlash());
//
//        //Đưa ảnh về container chính
//        var anhSanPhamAddRquest=new Anh();
//        anhSanPhamAddRquest.setName(addRequest.getAnhName());
//        anhSanPhamAddRquest.setUrl(imageService.moveImageToPermanent(addRequest.getAnhName()));
//        var anhSanPham = anhRepository.save(anhSanPhamAddRquest);
//
//        sp.setAnh(anhSanPham);
//
//
//        var lstSPCT=addRequest.getSanPhamChiTiet().stream().map(spctAddRequest->{
//            //Lưu ảnh trả về list Ảnh
//            var lstAnh= spctAddRequest.getAnh().stream().map(imageName->{
//                var newImage=new Anh();
//                newImage.setName(imageName);
//                newImage.setUrl(imageService.moveImageToPermanent(imageName));
//                return anhRepository.save(newImage);
//            }).collect(Collectors.toList());
//
//            //Chuyển lai list khuyến mãi
//            var lstKhuyenMai=spctAddRequest.getKhuyenMaiIds().stream().map(khuyenMaiId->{
//                var newKhuyenMai=new KhuyenMai();
//                newKhuyenMai.setId(khuyenMaiId);
//                return newKhuyenMai;
//            }).collect(Collectors.toList());
//
//            var spct=modelMapper.map(spctAddRequest, SanPhamChiTiet.class);
//            spct.setKhuyenMai(lstKhuyenMai);
//            spct.setAnh(lstAnh);
//            spct.setSanPham(sp);
//            return spct;
//        }).collect(Collectors.toSet());
//
//
//        sp.setSanPhamChiTiet(lstSPCT);
//        var spResponse=sanPhamRepository.save(sp);
        return ResponseEntity.ok(spe);
    }

    private void optimizeField(Object source, Object target) {
        if(target==null) {}
    }


    @ResponseBody
    @GetMapping("/api/v1/san-pham-data-table")
    public List<SanPhamDataTable> getAll(
//            @RequestParam Map<String, String> params
    ) {
//        System.out.println(params);
//        var searchKey=params.get("search[value]");
//        var draw=Integer.parseInt(params.get("draw"));
//        var start=Integer.parseInt(params.get("start"));
//        var lenght=Integer.parseInt(params.get("length"));
//        var orderBy=params.get("columns["+params.get("order[0][column]")+"][data]");
//        var orderDir=params.get("order[0][dir]").toUpperCase();
//        Pageable pageable = PageRequest.of(start,  lenght);
        var spdt=sanPhamRepository.findAllSanPhamDataTable();
        return spdt;
    }

    public Collection<SanPham> findAllByDeletedIsFalseOrderByIdDesc() {
        return null;
    }
//
//    @ResponseBody
//    @PutMapping("/api/v1/san-pham")
//    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        SanPham cnmh = modelMapper.map(req, SanPham.class);
//        Response cnmhRP = modelMapper.map(sanPhamRepository.save(cnmh), Response.class);
//        response.setStatus(HttpStatus.CREATED.value());
//        return cnmhRP;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham")
//    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        if (editReq.getId() == null || !sanPhamRepository.existsById(editReq.getId())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        sanPhamRepository.save(modelMapper.map(editReq, SanPham.class));
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return sanPhamRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
//    }
//
//
//    @ResponseBody
//    @DeleteMapping("/api/v1/san-pham")
//    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !sanPhamRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = sanPhamRepository.findById(id).get();
//        cnmhE.setDeleted(true);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        sanPhamRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham/revert")
//    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !sanPhamRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = sanPhamRepository.findById(id).get();
//        cnmhE.setDeleted(false);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        sanPhamRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham/import-excel")
//    @Transactional
//    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstCNMH, Errors errors) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//        try{
//            List<SanPham> lstMS = lstCNMH.stream()
//                    .map((element) -> modelMapper.map(element, SanPham.class))
//                    .collect(Collectors.toList());
//            List<Response> lstRP = sanPhamRepository.saveAll(lstMS).stream()
//                    .map((element) -> modelMapper.map(element, Response.class))
//                    .collect(Collectors.toList());
//            return lstRP;
//        }
//        catch (Exception e) {
//            log.error("Lỗi import file");
//            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//
//    }
//
//    @GetMapping("/admin/san-pham/export-excel")
//    public ResponseEntity<byte[]> exportToExcel() throws IOException {
//        List<SanPham> mauSacList = sanPhamRepository.findAll();
//        ExportExcel<SanPham> exportExcel = new ExportExcel();
//        ByteArrayInputStream in = exportExcel.export(mauSacList);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=cong_nghe_man_hinh.xlsx");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(in.readAllBytes());
//    }
}
