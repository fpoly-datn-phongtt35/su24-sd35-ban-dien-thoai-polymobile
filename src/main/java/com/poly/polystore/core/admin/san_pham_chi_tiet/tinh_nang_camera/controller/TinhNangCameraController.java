package com.poly.polystore.core.admin.san_pham_chi_tiet.tinh_nang_camera.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.tinh_nang_camera.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.tinh_nang_camera.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.tinh_nang_camera.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.tinh_nang_camera.model.response.Response;
import com.poly.polystore.entity.TinhNangCamera;
import com.poly.polystore.repository.TinhNangCameraRepository;
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
public class TinhNangCameraController {
    private static final Logger log = LoggerFactory.getLogger(TinhNangCameraController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TinhNangCameraRepository tinhNangCameraRepository;

    @GetMapping("/admin/san-pham-chi-tiet/tinh-nang-camera")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/tinh-nang-camera";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/tinh-nang-camera")
    public List<Response> getAll() {
        return modelMapper.map(tinhNangCameraRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/tinh-nang-camera")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        TinhNangCamera tnc = modelMapper.map(req, TinhNangCamera.class);
        Response tncRP = modelMapper.map(tinhNangCameraRepository.save(tnc), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return tncRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/tinh-nang-camera")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !tinhNangCameraRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        tinhNangCameraRepository.save(modelMapper.map(editReq, TinhNangCamera.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return tinhNangCameraRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/tinh-nang-camera")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !tinhNangCameraRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var tncE = tinhNangCameraRepository.findById(id).get();
        tncE.setDeleted(true);
        var tncR = modelMapper.map(tncE, Response.class);
        tinhNangCameraRepository.save(tncE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return tncR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/tinh-nang-camera/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !tinhNangCameraRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var tncE = tinhNangCameraRepository.findById(id).get();
        tncE.setDeleted(false);
        var tncR = modelMapper.map(tncE, Response.class);
        tinhNangCameraRepository.save(tncE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return tncR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/tinh-nang-camera/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstTNC, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<TinhNangCamera> lstMS = lstTNC.stream()
                    .map((element) -> modelMapper.map(element, TinhNangCamera.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = tinhNangCameraRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/tinh-nang-camera/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<TinhNangCamera> lst = tinhNangCameraRepository.findAll();
        ExportExcel<TinhNangCamera> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(lst);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=tinh_nang_camera.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
