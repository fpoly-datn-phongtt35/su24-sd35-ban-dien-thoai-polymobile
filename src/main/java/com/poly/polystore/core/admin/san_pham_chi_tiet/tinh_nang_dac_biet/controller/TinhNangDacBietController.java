package com.poly.polystore.core.admin.san_pham_chi_tiet.tinh_nang_dac_biet.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.response.Response;
import com.poly.polystore.entity.TinhNangDacBiet;
import com.poly.polystore.repository.TinhNangDacBietRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TinhNangDacBietController {
    private static final Logger log = LoggerFactory.getLogger(TinhNangDacBietController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TinhNangDacBietRepository tinhNangDacBietRepository;

    @GetMapping("/admin/san-pham-chi-tiet/tinh-nang-dac-biet")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/tinh-nang-dac-biet";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/tinh-nang-dac-biet")
    public List<Response> getAll() {
        return modelMapper.map(tinhNangDacBietRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/tinh-nang-dac-biet")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        TinhNangDacBiet tndb = modelMapper.map(req, TinhNangDacBiet.class);
        Response tndbRP = modelMapper.map(tinhNangDacBietRepository.save(tndb), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return tndbRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/tinh-nang-dac-biet")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !tinhNangDacBietRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        tinhNangDacBietRepository.save(modelMapper.map(editReq, TinhNangDacBiet.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return tinhNangDacBietRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/tinh-nang-dac-biet")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !tinhNangDacBietRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var tndbE = tinhNangDacBietRepository.findById(id).get();
        tndbE.setDeleted(true);
        var tndbR = modelMapper.map(tndbE, Response.class);
        tinhNangDacBietRepository.save(tndbE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return tndbR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/tinh-nang-dac-biet/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !tinhNangDacBietRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var tndbE = tinhNangDacBietRepository.findById(id).get();
        tndbE.setDeleted(false);
        var tndbR = modelMapper.map(tndbE, Response.class);
        tinhNangDacBietRepository.save(tndbE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return tndbR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/tinh-nang-dac-biet/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstTNDB, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<TinhNangDacBiet> lstMS = lstTNDB.stream()
                    .map((element) -> modelMapper.map(element, TinhNangDacBiet.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = tinhNangDacBietRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/tinh-nang-dac-biet/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<TinhNangDacBiet> mauSacList = tinhNangDacBietRepository.findAll();
        ExportExcel<TinhNangDacBiet> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=tinh_nang_dac_biet.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
