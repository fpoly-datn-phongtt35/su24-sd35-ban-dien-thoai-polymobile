package com.poly.polystore.controller;

import com.poly.polystore.dto.MauSacDto;

import com.poly.polystore.entity.MauSac;
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
import java.util.Optional;

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
        if(mauSacDto.getId() == null||!mauSacRepository.existsById(mauSacDto.getId())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        mauSacRepository.save(modelMapper.map(mauSacDto, MauSac.class));
        return mauSacRepository.findById(mauSacDto.getId()).map((element) -> modelMapper.map(element, MauSacDto.class)).get();
    }
    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public MauSacDto add(@Valid @RequestBody MauSacDto mauSacDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        }

        mauSacRepository.save(modelMapper.map(mauSacDto, MauSac.class));
        return mauSacRepository.findByMa(mauSacDto.getMa()).map((element) -> modelMapper.map(element, MauSacDto.class)).get();
    }
    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/mau-sac")
    public MauSacDto delete(@RequestParam(name = "id") Integer id) {
        if (id==null||!mauSacRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var ms= mauSacRepository.findById(id).map((element) -> modelMapper.map(element, MauSacDto.class)).get();
        mauSacRepository.deleteById(id);
        return ms;
    }

}
