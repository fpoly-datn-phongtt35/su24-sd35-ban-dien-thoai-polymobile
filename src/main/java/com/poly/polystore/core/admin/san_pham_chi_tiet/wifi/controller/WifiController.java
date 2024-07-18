package com.poly.polystore.core.admin.san_pham_chi_tiet.wifi.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.wifi.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.wifi.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.wifi.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.wifi.model.response.Response;
import com.poly.polystore.entity.Wifi;
import com.poly.polystore.repository.WifiRepository;
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
public class WifiController {
    private static final Logger log = LoggerFactory.getLogger(WifiController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private WifiRepository wifiRepository;

    @GetMapping("/admin/san-pham-chi-tiet/wifi")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/wifi";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/wifi")
    public List<Response> getAll() {
        return modelMapper.map(wifiRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/wifi")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        Wifi wifi = modelMapper.map(req, Wifi.class);
        Response wifiRP = modelMapper.map(wifiRepository.save(wifi), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return wifiRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/wifi")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !wifiRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        wifiRepository.save(modelMapper.map(editReq, Wifi.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return wifiRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/wifi")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !wifiRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var wifiE = wifiRepository.findById(id).get();
        wifiE.setDeleted(true);
        var wifiR = modelMapper.map(wifiE, Response.class);
        wifiRepository.save(wifiE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return wifiR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/wifi/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !wifiRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var wifiE = wifiRepository.findById(id).get();
        wifiE.setDeleted(false);
        var wifiR = modelMapper.map(wifiE, Response.class);
        wifiRepository.save(wifiE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return wifiR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/wifi/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstWifi, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<Wifi> lstMS = lstWifi.stream()
                    .map((element) -> modelMapper.map(element, Wifi.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = wifiRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/wifi/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Wifi> mauSacList = wifiRepository.findAll();
        ExportExcel<Wifi> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=wifi.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
