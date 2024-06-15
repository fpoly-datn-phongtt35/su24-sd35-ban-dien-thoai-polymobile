package com.poly.polystore.core.admin.san_pham_chi_tiet.bluetooth.controller;

import com.poly.polystore.core.admin.san_pham_chi_tiet.bluetooth.model.request.AddRequest;
import com.poly.polystore.core.admin.san_pham_chi_tiet.bluetooth.model.request.EditReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.bluetooth.model.request.ImportReq;
import com.poly.polystore.core.admin.san_pham_chi_tiet.bluetooth.model.response.Response;
import com.poly.polystore.entity.Bluetooth;
import com.poly.polystore.repository.BluetoothRepository;
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
public class BluetoothController {
    private static final Logger log = LoggerFactory.getLogger(BluetoothController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BluetoothRepository bluetoothRepository;

    @GetMapping("/admin/san-pham-chi-tiet/bluetooth")
    public String ui(Model model) {
        return "/admin/san-pham-chi-tiet/bluetooth";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham-chi-tiet/bluetooth")
    public List<Response> getAll() {
        return modelMapper.map(bluetoothRepository.findAll(), new TypeToken<List<Response>>() {
        }.getType());
    }

    @ResponseBody
    @PutMapping("/api/v1/san-pham-chi-tiet/bluetooth")
    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        Bluetooth bluetooth = modelMapper.map(req, Bluetooth.class);
        Response bluetoothRP = modelMapper.map(bluetoothRepository.save(bluetooth), Response.class);
        response.setStatus(HttpStatus.CREATED.value());
        return bluetoothRP;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/bluetooth")
    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
        }
        if (editReq.getId() == null || !bluetoothRepository.existsById(editReq.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        bluetoothRepository.save(modelMapper.map(editReq, Bluetooth.class));
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return bluetoothRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
    }


    @ResponseBody
    @DeleteMapping("/api/v1/san-pham-chi-tiet/bluetooth")
    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !bluetoothRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var bluetoothE = bluetoothRepository.findById(id).get();
        bluetoothE.setDeleted(true);
        var bluetoothR = modelMapper.map(bluetoothE, Response.class);
        bluetoothRepository.save(bluetoothE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return bluetoothR;
    }

    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/bluetooth/revert")
    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
        if (id == null || !bluetoothRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
        var bluetoothE = bluetoothRepository.findById(id).get();
        bluetoothE.setDeleted(false);
        var bluetoothR = modelMapper.map(bluetoothE, Response.class);
        bluetoothRepository.save(bluetoothE);
        resp.setStatus(HttpStatus.ACCEPTED.value());
        return bluetoothR;
    }


    @ResponseBody
    @PostMapping("/api/v1/san-pham-chi-tiet/bluetooth/import-excel")
    @Transactional
    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstBluetooth, Errors errors) {
        if (errors.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
        }
        try{
            List<Bluetooth> lstMS = lstBluetooth.stream()
                    .map((element) -> modelMapper.map(element, Bluetooth.class))
                    .collect(Collectors.toList());
            List<Response> lstRP = bluetoothRepository.saveAll(lstMS).stream()
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

    @GetMapping("/admin/san-pham-chi-tiet/bluetooth/export-excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<Bluetooth> mauSacList = bluetoothRepository.findAll();
        ExportExcel<Bluetooth> exportExcel = new ExportExcel();
        ByteArrayInputStream in = exportExcel.export(mauSacList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=bluetooth.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
}
