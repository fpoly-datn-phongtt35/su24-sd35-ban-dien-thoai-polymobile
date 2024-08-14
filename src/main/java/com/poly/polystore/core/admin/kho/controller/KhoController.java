package com.poly.polystore.core.admin.kho.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_man_hinh.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_man_hinh.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_man_hinh.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_man_hinh.model.response.Response;
import com.poly.polystore.dto.DataTableResponse;
import com.poly.polystore.entity.CongNgheManHinh;
import com.poly.polystore.repository.CongNgheManHinhRepository;
import com.poly.polystore.utils.ExportExcel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class KhoController {
    @GetMapping("/admin/kho")
    public String kho(Model model) {
        return "/admin/kho/kho";
    }

    @ResponseBody
    @GetMapping("/api/v1/kho")
    public ResponseEntity<?> getAll(
           @RequestParam Map<String,String> params
            ) {
        var resp=new DataTableResponse();
        resp.setDraw(Integer.valueOf(params.get("draw")));
        resp.setRecordsTotal(10);
        resp.setData(null);
        return ResponseEntity.ok(resp);
    }

//     private static final Logger log = LoggerFactory.getLogger(KhoController.class);
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private CongNgheManHinhRepository congNgheManHinhRepository;
//
//    @GetMapping("/admin/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public String ui(Model model) {
//        return "/admin/san-pham-chi-tiet/cong-nghe-man-hinh";
//    }
//
//    @ResponseBody
//    @GetMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public List<Response> getAll() {
//        return modelMapper.map(congNgheManHinhRepository.findAll(), new TypeToken<List<Response>>() {
//        }.getType());
//    }
//
//    @ResponseBody
//    @PutMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        CongNgheManHinh cnmh = modelMapper.map(req, CongNgheManHinh.class);
//        Response cnmhRP = modelMapper.map(congNgheManHinhRepository.save(cnmh), Response.class);
//        response.setStatus(HttpStatus.CREATED.value());
//        return cnmhRP;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        if (editReq.getId() == null || !congNgheManHinhRepository.existsById(editReq.getId())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        congNgheManHinhRepository.save(modelMapper.map(editReq, CongNgheManHinh.class));
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return congNgheManHinhRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
//    }
//
//
//    @ResponseBody
//    @DeleteMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !congNgheManHinhRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = congNgheManHinhRepository.findById(id).get();
//        cnmhE.setDeleted(true);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        congNgheManHinhRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh/revert")
//    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !congNgheManHinhRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = congNgheManHinhRepository.findById(id).get();
//        cnmhE.setDeleted(false);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        congNgheManHinhRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh/import-excel")
//    @Transactional
//    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstCNMH, Errors errors) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//        try{
//            List<CongNgheManHinh> lstMS = lstCNMH.stream()
//                    .map((element) -> modelMapper.map(element, CongNgheManHinh.class))
//                    .collect(Collectors.toList());
//            List<Response> lstRP = congNgheManHinhRepository.saveAll(lstMS).stream()
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
//    @GetMapping("/admin/san-pham-chi-tiet/cong-nghe-man-hinh/export-excel")
//    public ResponseEntity<byte[]> exportToExcel() throws IOException {
//        List<CongNgheManHinh> mauSacList = congNgheManHinhRepository.findAll();
//        ExportExcel<CongNgheManHinh> exportExcel = new ExportExcel();
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
