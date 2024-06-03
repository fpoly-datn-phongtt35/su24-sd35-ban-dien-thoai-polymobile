package com.poly.polystore.core.admin.san_pham_chi_tiet.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.dto.MauSacDto;

import com.poly.polystore.entity.MauSac;
import com.poly.polystore.repository.MauSacRepository;
import com.poly.polystore.utils.Utils;
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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MauSacController {
    private static final Logger log = LoggerFactory.getLogger(MauSacController.class);
    @Autowired
    MauSacRepository mauSacRepository;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/admin/san-pham-chi-tiet/mau-sac")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/mau-sac";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public List<MauSacDto> getAll(Model model) {
        return modelMapper.map(mauSacRepository.findAll(), new TypeToken<List<MauSacDto>>() {
        }.getType());
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public MauSacDto update(@Valid @RequestBody MauSacDto mauSacDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        }
        if (mauSacDto.getId() == null || !mauSacRepository.existsById(mauSacDto.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        mauSacRepository.save(modelMapper.map(mauSacDto, MauSac.class));
        return mauSacRepository.findById(mauSacDto.getId()).map((element) -> modelMapper.map(element, MauSacDto.class)).get();
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public List<MauSacDto> add(@Valid @RequestBody List<MauSacDto> lstMSDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        }
        List<MauSac> lstMS = lstMSDTO.stream().map((element) -> modelMapper.map(element, MauSac.class)).collect(Collectors.toList());
        List<MauSacDto> lstRP = mauSacRepository.saveAll(lstMS).stream().map((element) -> modelMapper.map(element, MauSacDto.class)).collect(Collectors.toList());
        return lstRP;
    }

    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public MauSacDto delete(@RequestParam(name = "id") Integer id) {
        if (id == null || !mauSacRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var ms = mauSacRepository.findById(id).map((element) -> modelMapper.map(element, MauSacDto.class)).get();
        mauSacRepository.deleteById(id);
        return ms;
    }

//    @GetMapping("/api/v1/san-pham-chi-tiet/mau-sac/export-excel")
//    public ResponseEntity<byte[]> getAll(Model model) {
//        return modelMapper.map(mauSacRepository.findAll(), new TypeToken<List<MauSacDto>>() {
//        }.getType());
//    }
@GetMapping("/admin/san-pham-chi-tiet/mau-sac/export-excel")
public ResponseEntity<byte[]> exportToExcel() throws IOException {
    List<MauSac> mauSacList = mauSacRepository.findAll();
    Utils<MauSac> utils = new Utils();
    ByteArrayInputStream in = utils.printAllFields(mauSacList);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=mau_sac.xlsx");

    return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(in.readAllBytes());
}
}
