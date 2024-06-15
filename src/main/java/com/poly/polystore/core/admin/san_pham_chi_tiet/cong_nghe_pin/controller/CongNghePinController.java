package com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_pin.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_pin.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_pin.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_pin.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_pin.model.response.Response;
import com.poly.polystore.entity.CongNghePin;
import com.poly.polystore.repository.CongNghePinRepository;
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
public class CongNghePinController {
    private static final Logger log = LoggerFactory.getLogger(CongNghePinController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CongNghePinRepository congNghePinRepository;

    @GetMapping("/admin/san-pham-chi-tiet/cong-nghe-pin")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/cong-nghe-pin";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/cong-nghe-pin")
    public List<Response> getAll() {
        return modelMapper.map(congNghePinRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/cong-nghe-pin")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        CongNghePin pin = modelMapper.map(req, CongNghePin.class);
        Response pinRP = modelMapper.map(congNghePinRepository.save(pin), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return pinRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-pin")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !congNghePinRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        congNghePinRepository.save(modelMapper.map(editReq, CongNghePin.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return congNghePinRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/cong-nghe-pin")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !congNghePinRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var pinE = congNghePinRepository.findById(id).get();
        pinE.setDeleted(true);
        var pinR = modelMapper.map(pinE, Response.class);
        congNghePinRepository.save(pinE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return pinR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-pin/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !congNghePinRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var pinE = congNghePinRepository.findById(id).get();
        pinE.setDeleted(false);
        var pinR = modelMapper.map(pinE, Response.class);
        congNghePinRepository.save(pinE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return pinR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-pin/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstPin, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<CongNghePin> lstMS = lstPin.stream()
                    .map((element) -> modelMapper.map(element, CongNghePin.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = congNghePinRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/cong-nghe-pin/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<CongNghePin> mauSacList = congNghePinRepository.findAll();
        ExportExcel<CongNghePin> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=cong_nghe_pin.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
