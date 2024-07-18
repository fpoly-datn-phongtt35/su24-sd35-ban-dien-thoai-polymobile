package com.poly.polystore.core.admin.san_pham_chi_tiet.cpu.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.cpu.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cpu.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cpu.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cpu.model.response.Response;
import com.poly.polystore.entity.Cpu;
import com.poly.polystore.repository.CpuRepository;
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
public class CpuController {
    private static final Logger log = LoggerFactory.getLogger(CpuController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CpuRepository cpuRepository;

    @GetMapping("/admin/san-pham-chi-tiet/cpu")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/cpu";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/cpu")
    public List<Response> getAll() {
        return modelMapper.map(cpuRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/cpu")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        Cpu cpu = modelMapper.map(req, Cpu.class);
        Response cpuRP = modelMapper.map(cpuRepository.save(cpu), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return cpuRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/cpu")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !cpuRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        cpuRepository.save(modelMapper.map(editReq, Cpu.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return cpuRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/cpu")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !cpuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var cpuE = cpuRepository.findById(id).get();
        cpuE.setDeleted(true);
        var cpuR = modelMapper.map(cpuE, Response.class);
        cpuRepository.save(cpuE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return cpuR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/cpu/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !cpuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var cpuE = cpuRepository.findById(id).get();
        cpuE.setDeleted(false);
        var cpuR = modelMapper.map(cpuE, Response.class);
        cpuRepository.save(cpuE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return cpuR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/cpu/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstCPU, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<Cpu> lstMS = lstCPU.stream()
                    .map((element) -> modelMapper.map(element, Cpu.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = cpuRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/cpu/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Cpu> mauSacList = cpuRepository.findAll();
        ExportExcel<Cpu> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=cpu.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
