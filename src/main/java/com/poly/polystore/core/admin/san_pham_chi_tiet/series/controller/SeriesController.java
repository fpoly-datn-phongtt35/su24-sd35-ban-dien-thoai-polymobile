package com.poly.polystore.core.admin.san_pham_chi_tiet.series.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.series.model.response.Response;
import com.poly.polystore.entity.Series;
import com.poly.polystore.repository.SeriesRepository;
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
public class SeriesController {
    private static final Logger log = LoggerFactory.getLogger(SeriesController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SeriesRepository seriesRepository;

    @GetMapping("/admin/san-pham-chi-tiet/series")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/series";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/series")
    public List<Response> getAll() {
        return modelMapper.map(seriesRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/series")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        Series series = modelMapper.map(req, Series.class);
        Response seriesRP = modelMapper.map(seriesRepository.save(series), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return seriesRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/series")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !seriesRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        seriesRepository.save(modelMapper.map(editReq, Series.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return seriesRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/series")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !seriesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var seriesE = seriesRepository.findById(id).get();
        seriesE.setDeleted(true);
        var seriesR = modelMapper.map(seriesE, Response.class);
        seriesRepository.save(seriesE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return seriesR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/series/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !seriesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var seriesE = seriesRepository.findById(id).get();
        seriesE.setDeleted(false);
        var seriesR = modelMapper.map(seriesE, Response.class);
        seriesRepository.save(seriesE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return seriesR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/series/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstSERIES, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<Series> lstMS = lstSERIES.stream()
                    .map((element) -> modelMapper.map(element, Series.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = seriesRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/series/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Series> mauSacList = seriesRepository.findAll();
        ExportExcel<Series> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=series.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
