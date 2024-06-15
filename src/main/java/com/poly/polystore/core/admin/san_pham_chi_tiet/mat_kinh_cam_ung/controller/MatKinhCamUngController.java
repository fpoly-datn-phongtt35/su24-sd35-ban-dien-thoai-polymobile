package com.poly.polystore.core.admin.san_pham_chi_tiet.mat_kinh_cam_ung.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.mat_kinh_cam_ung.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.mat_kinh_cam_ung.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.mat_kinh_cam_ung.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.mat_kinh_cam_ung.model.response.Response;
import com.poly.polystore.entity.MatKinhCamUng;
import com.poly.polystore.entity.MatKinhCamUng;
import com.poly.polystore.repository.MatKinhCamUngRepository;
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
public class MatKinhCamUngController {
    private static final Logger log = LoggerFactory.getLogger(MatKinhCamUngController.class);

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MatKinhCamUngRepository matKinhCamUngRepository;

    @GetMapping("/admin/san-pham-chi-tiet/mat-kinh-cam-ung")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/mat-kinh-cam-ung";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung")
    public List<Response> getAll() {
        return modelMapper.map(matKinhCamUngRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        MatKinhCamUng cnmh = modelMapper.map(req, MatKinhCamUng.class);
        Response cnmhRP = modelMapper.map(matKinhCamUngRepository.save(cnmh), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return cnmhRP;
    }
    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> mkcusIR, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<MatKinhCamUng> lstMkcuR = mkcusIR.stream()
                    .map((element) -> modelMapper.map(element, MatKinhCamUng.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = matKinhCamUngRepository.saveAll(lstMkcuR).stream()
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


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !matKinhCamUngRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        matKinhCamUngRepository.save(modelMapper.map(editReq, MatKinhCamUng.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return matKinhCamUngRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !matKinhCamUngRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var mkcuE= matKinhCamUngRepository.findById(id).get();
        mkcuE.setDeleted(true);
        var mkcuR = modelMapper.map(mkcuE, Response.class);
        matKinhCamUngRepository.save(mkcuE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return mkcuR;
    }
    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mat-kinh-cam-ung/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !matKinhCamUngRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var mkcuE= matKinhCamUngRepository.findById(id).get();
        mkcuE.setDeleted(false);
        var mkcuR = modelMapper.map(mkcuE, Response.class);
        matKinhCamUngRepository.save(mkcuE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return mkcuR;
    }


    @GetMapping("/admin/san-pham-chi-tiet/mat-kinh-cam-ung/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<MatKinhCamUng> mauSacList = matKinhCamUngRepository.findAll();
        ExportExcel<MatKinhCamUng> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=mat_kinh_cam_.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .body(in.readAllBytes());
    }
}
