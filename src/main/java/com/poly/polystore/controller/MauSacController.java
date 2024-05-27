package com.poly.polystore.controller;

import com.poly.polystore.dto.MauSacDto;

import com.poly.polystore.repository.MauSacRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.internal.TypeResolvingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.rmi.server.LogStream.log;

@Controller
public class MauSacController {
    private static final Logger log = LoggerFactory.getLogger(MauSacController.class);
    @Autowired
    MauSacRepository mauSacRepository;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/admin/san-pham-chi-tiet/mau-sac")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/mau-sac/mau-sac";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public List<MauSacDto> getAll(Model model) {
        return modelMapper.map(mauSacRepository.findAll(),new TypeToken<List<MauSacDto>>(){}.getType());
    }
    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public MauSacDto update(@Valid @RequestBody MauSacDto mauSacDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        }
        System.out.println(mauSacDto);
        System.out.println("OK");
        return mauSacDto;
    }

}
