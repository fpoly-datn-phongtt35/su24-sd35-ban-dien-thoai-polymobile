package com.poly.polystore.core.admin.san_pham_chi_tiet.khuyen_mai.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.khuyen_mai.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.khuyen_mai.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.khuyen_mai.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.khuyen_mai.model.response.Response;
import com.poly.polystore.entity.KhuyenMai;
import com.poly.polystore.repository.KhuyenMaiRepository;
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
public class KhuyenMaiController {
    private static final Logger log = LoggerFactory.getLogger(KhuyenMaiController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @GetMapping("/admin/san-pham-chi-tiet/khuyen-mai")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/khuyen-mai";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/khuyen-mai")
    public List<Response> getAll() {
        return modelMapper.map(khuyenMaiRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/khuyen-mai")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        KhuyenMai km = modelMapper.map(req, KhuyenMai.class);
        Response kmRP = modelMapper.map(khuyenMaiRepository.save(km), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return kmRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/khuyen-mai")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !khuyenMaiRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        khuyenMaiRepository.save(modelMapper.map(editReq, KhuyenMai.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return khuyenMaiRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/khuyen-mai")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !khuyenMaiRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var kmE = khuyenMaiRepository.findById(id).get();
        kmE.setDeleted(true);
        var kmR = modelMapper.map(kmE, Response.class);
        khuyenMaiRepository.save(kmE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return kmR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/khuyen-mai/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !khuyenMaiRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var kmE = khuyenMaiRepository.findById(id).get();
        kmE.setDeleted(false);
        var kmR = modelMapper.map(kmE, Response.class);
        khuyenMaiRepository.save(kmE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return kmR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/khuyen-mai/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstKM, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<KhuyenMai> lstMS = lstKM.stream()
                    .map((element) -> modelMapper.map(element, KhuyenMai.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = khuyenMaiRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/khuyen-mai/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<KhuyenMai> mauSacList = khuyenMaiRepository.findAll();
        ExportExcel<KhuyenMai> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=khuyen_mai.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
