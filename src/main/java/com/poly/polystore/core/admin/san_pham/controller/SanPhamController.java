package com.poly.polystore.core.admin.san_pham.controller;

import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamDataTable;
import com.poly.polystore.core.admin.san_pham.model.request.AddRequest;
import com.poly.polystore.entity.SanPham;
import com.poly.polystore.repository.SanPhamRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequiredArgsConstructor
@Controller
public class SanPhamController {
    private static final Logger log = LoggerFactory.getLogger(SanPhamController.class);
    private final ModelMapper modelMapper;
    private final SanPhamRepository sanPhamRepository;
    private final EntityManager entityManager;

    @GetMapping("/admin/san-pham")
    public String ui(Model model) {
        return "/admin/san-pham/san-pham";
    }

    @GetMapping("/admin/san-pham/add")
    public String add(Model model) {
        return "/admin/san-pham/add";
    }

    @PostMapping("/api/v1/san-pham")
    public ResponseEntity<?> addNew(Model model) {
        var rp=new AddRequest();
        Set<AddRequest.SanPhamChiTiet> sanPhamChiTietSet=new HashSet<>();
        sanPhamChiTietSet.add(new AddRequest.SanPhamChiTiet());
        rp.setSanPhamChiTiet(sanPhamChiTietSet);
        return ResponseEntity.ok(rp);
    }



    @ResponseBody
    @GetMapping("/api/v1/san-pham-data-table")
    public List<SanPhamDataTable> getAll(
//            @RequestParam Map<String, String> params
    ) {
//        System.out.println(params);
//        var searchKey=params.get("search[value]");
//        var draw=Integer.parseInt(params.get("draw"));
//        var start=Integer.parseInt(params.get("start"));
//        var lenght=Integer.parseInt(params.get("length"));
//        var orderBy=params.get("columns["+params.get("order[0][column]")+"][data]");
//        var orderDir=params.get("order[0][dir]").toUpperCase();
//        Pageable pageable = PageRequest.of(start,  lenght);
        var spdt=sanPhamRepository.findAllSanPhamDataTable();
        return spdt;
    }

    public Collection<SanPham> findAllByDeletedIsFalseOrderByIdDesc() {
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
