package com.poly.polystore.core.admin.san_pham_chi_tiet.gps.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.gps.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.gps.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.gps.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.gps.model.response.Response;
import com.poly.polystore.entity.Gps;
import com.poly.polystore.repository.GpsRepository;
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
public class GpsController {
    private static final Logger log = LoggerFactory.getLogger(GpsController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private GpsRepository gpsRepository;

    @GetMapping("/admin/san-pham-chi-tiet/gps")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/gps";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/gps")
    public List<Response> getAll() {
        return modelMapper.map(gpsRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/gps")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        Gps gps = modelMapper.map(req, Gps.class);
        Response gpsRP = modelMapper.map(gpsRepository.save(gps), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return gpsRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/gps")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !gpsRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        gpsRepository.save(modelMapper.map(editReq, Gps.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return gpsRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/gps")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !gpsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var gpsE = gpsRepository.findById(id).get();
        gpsE.setDeleted(true);
        var gpsR = modelMapper.map(gpsE, Response.class);
        gpsRepository.save(gpsE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return gpsR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/gps/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !gpsRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var gpsE = gpsRepository.findById(id).get();
        gpsE.setDeleted(false);
        var gpsR = modelMapper.map(gpsE, Response.class);
        gpsRepository.save(gpsE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return gpsR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/gps/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstGPS, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<Gps> lstMS = lstGPS.stream()
                    .map((element) -> modelMapper.map(element, Gps.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = gpsRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/gps/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Gps> mauSacList = gpsRepository.findAll();
        ExportExcel<Gps> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=gps.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
