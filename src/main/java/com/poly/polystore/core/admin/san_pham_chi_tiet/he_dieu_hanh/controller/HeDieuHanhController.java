package com.poly.polystore.core.admin.san_pham_chi_tiet.he_dieu_hanh.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.he_dieu_hanh.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.he_dieu_hanh.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.he_dieu_hanh.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.he_dieu_hanh.model.response.Response;
import com.poly.polystore.entity.HeDieuHanh;
import com.poly.polystore.repository.HeDieuHanhRepository;
import com.poly.polystore.utils.ExportExcel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HeDieuHanhController {
    private static final Logger log = LoggerFactory.getLogger(HeDieuHanhController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HeDieuHanhRepository heDieuHanhRepository;

    @GetMapping("/admin/san-pham-chi-tiet/he-dieu-hanh")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/he-dieu-hanh";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/he-dieu-hanh")
    public List<Response> getAll() {
        return modelMapper.map(heDieuHanhRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/he-dieu-hanh")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        HeDieuHanh cnmh = modelMapper.map(req, HeDieuHanh.class);
        Response cnmhRP = modelMapper.map(heDieuHanhRepository.save(cnmh), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return cnmhRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/he-dieu-hanh")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !heDieuHanhRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        heDieuHanhRepository.save(modelMapper.map(editReq, HeDieuHanh.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return heDieuHanhRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/he-dieu-hanh")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !heDieuHanhRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var cnmhE = heDieuHanhRepository.findById(id).get();
        cnmhE.setDeleted(true);
        var cnmhR = modelMapper.map(cnmhE, Response.class);
        heDieuHanhRepository.save(cnmhE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return cnmhR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/he-dieu-hanh/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !heDieuHanhRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var cnmhE = heDieuHanhRepository.findById(id).get();
        cnmhE.setDeleted(false);
        var cnmhR = modelMapper.map(cnmhE, Response.class);
        heDieuHanhRepository.save(cnmhE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return cnmhR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/he-dieu-hanh/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstHDH, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<HeDieuHanh> lstMS = lstHDH.stream()
                    .map((element) -> modelMapper.map(element, HeDieuHanh.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = heDieuHanhRepository.saveAll(lstMS).stream()
                    .map((element) -> modelMapper.map(element, Response.class))
                    .collect(Collectors.toList());
            return lstRP;
        }
        catch (Exception e) {
            log.error("Lỗi import file");
            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }

    }

    @GetMapping("/admin/san-pham-chi-tiet/he-dieu-hanh/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<HeDieuHanh> mauSacList = heDieuHanhRepository.findAll();
        ExportExcel<HeDieuHanh> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=he_dieu_hanh.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
