package com.poly.polystore.core.admin.san_pham.controller;

import com.poly.polystore.core.admin.san_pham.model.reponse.AllSanPhamResponse;
import com.poly.polystore.core.admin.san_pham_chi_tiet.cong_nghe_man_hinh.model.response.Response;
import com.poly.polystore.repository.SanPhamRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Controller
public class SanPhamController {
    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @GetMapping("/admin/san-pham")
    public String ui(Model model) {
        return "/admin/san-pham/san-pham";
    }
    @GetMapping("/admin/san-pham/add")
    public String add(Model model) {
        return "/admin/san-pham/add";
    }

    @ResponseBody
    @GetMapping("/api/v1/san-pham")
    public List<Response> getAll() {
        return modelMapper.map(sanPhamRepository.findAll(), new TypeToken<List<AllSanPhamResponse>>() {
        }.getType());
    }

    public Collection<Object> findAllByDeletedIsFalse() {
        return null;
    }
//
//    @ResponseBody
//    @PutMapping("/api/v1/san-pham")
//    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        SanPham cnmh = modelMapper.map(req, SanPham.class);
//        Response cnmhRP = modelMapper.map(sanPhamRepository.save(cnmh), Response.class);
//        response.setStatus(HttpStatus.CREATED.value());
//        return cnmhRP;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham")
//    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        if (editReq.getId() == null || !sanPhamRepository.existsById(editReq.getId())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        sanPhamRepository.save(modelMapper.map(editReq, SanPham.class));
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return sanPhamRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
//    }
//
//
//    @ResponseBody
//    @DeleteMapping("/api/v1/san-pham")
//    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !sanPhamRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = sanPhamRepository.findById(id).get();
//        cnmhE.setDeleted(true);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        sanPhamRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham/revert")
//    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !sanPhamRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = sanPhamRepository.findById(id).get();
//        cnmhE.setDeleted(false);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        sanPhamRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham/import-excel")
//    @Transactional
//    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstCNMH, Errors errors) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//        try{
//            List<SanPham> lstMS = lstCNMH.stream()
//                    .map((element) -> modelMapper.map(element, SanPham.class))
//                    .collect(Collectors.toList());
//            List<Response> lstRP = sanPhamRepository.saveAll(lstMS).stream()
//                    .map((element) -> modelMapper.map(element, Response.class))
//                    .collect(Collectors.toList());
//            return lstRP;
//        }
//        catch (Exception e) {
//            log.error("Lỗi import file");
//            TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//
//    }
//
//    @GetMapping("/admin/san-pham/export-excel")
//    public ResponseEntity<byte[]> exportToExcel() throws IOException {
//        List<SanPham> mauSacList = sanPhamRepository.findAll();
//        ExportExcel<SanPham> exportExcel = new ExportExcel();
//        ByteArrayInputStream in = exportExcel.export(mauSacList);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=cong_nghe_man_hinh.xlsx");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(in.readAllBytes());
//    }
}
