package com.poly.polystore.core.admin.san_pham_chi_tiet.mau_sac.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.mau_sac.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.mau_sac.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.mau_sac.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.mau_sac.model.response.Response;
import com.poly.polystore.entity.MauSac;
import com.poly.polystore.repository.MauSacRepository;
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
public class MauSacController {
    private static final Logger log = LoggerFactory.getLogger(MauSacController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MauSacRepository mauSacRepository;

    @GetMapping("/admin/san-pham-chi-tiet/mau-sac")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/mau-sac";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public List<Response> getAll() {
        return modelMapper.map(mauSacRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        MauSac ms = modelMapper.map(req, MauSac.class);
        Response msRP = modelMapper.map(mauSacRepository.save(ms), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return msRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !mauSacRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        mauSacRepository.save(modelMapper.map(editReq, MauSac.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return mauSacRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !mauSacRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var msE = mauSacRepository.findById(id).get();
        msE.setDeleted(true);
        var msR = modelMapper.map(msE, Response.class);
        mauSacRepository.save(msE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return msR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !mauSacRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var msE = mauSacRepository.findById(id).get();
        msE.setDeleted(false);
        var msR = modelMapper.map(msE, Response.class);
        mauSacRepository.save(msE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return msR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstMSi, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<MauSac> lstMS = lstMSi.stream()
                    .map((element) -> modelMapper.map(element, MauSac.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = mauSacRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/mau-sac/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<MauSac> mauSacList = mauSacRepository.findAll();
        ExportExcel<MauSac> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=mau_sac.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
