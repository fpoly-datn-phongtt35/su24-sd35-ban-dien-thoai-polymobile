package com.poly.polystore.core.admin.kho.controller;

import com.poly.polystore.core.admin.ban_hang.repository.impl.SanPhamRepositoryImpl;
import com.poly.polystore.core.admin.kho.model.response.LichSuKhoResponse;
import com.poly.polystore.core.admin.kho.repository.LichSuKhoRepositoryImpl;
import com.poly.polystore.dto.Select2Response;
import com.poly.polystore.entity.LichSuKho;
import com.poly.polystore.repository.SanPhamRepository;
import com.poly.polystore.repository.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class KhoController {
    private final LichSuKhoRepositoryImpl lichSuKhoRepositoryImpl;
    private final ModelMapper modelMapper;
    private final TaiKhoanRepository taiKhoanRepository;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamRepositoryImpl sanPhamRepositoryImpl;

    @GetMapping("/admin/kho")
    public String kho(Model model) {
        return "/admin/kho/kho";
    }
    @GetMapping("/admin/kho/nhap-hang")
    public String nhapHang(Model model) {
        return "/admin/kho/nhap-hang";
    }

    @ResponseBody
    @GetMapping("/api/v1/kho")
    public ResponseEntity<?> getAll(
           @RequestParam Map<String,String> params
            ) {
        return ResponseEntity.ok(lichSuKhoRepositoryImpl.findAll(params));
    }
    @ResponseBody
    @GetMapping("/api/v1/kho/{id}")
    public ResponseEntity<?> getById(
            @PathVariable(name="id") LichSuKho lichSuKho
    ) {
        return ResponseEntity.ok(lichSuKhoRepositoryImpl.findById(lichSuKho.getId()));
    }

    @GetMapping("/api/v1/select2/tai-khoan")
    @ResponseBody
    public ResponseEntity<?> getAllPromotion(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "code", required = false) Optional<String> code
    ) {
        Pageable pageAble = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<LichSuKhoResponse.TaiKhoan> taiKhoans;
        if (code.isPresent()) {

            taiKhoans = taiKhoanRepository.findTaiKhoanNhanVienByCodeLike(code.get(), pageAble);
        } else {
            taiKhoans = taiKhoanRepository.findAllTaiKhoanRessp  (pageAble);
        }
        Select2Response response = new Select2Response();
        response.setPagination(new Select2Response.Pagination(!taiKhoans.isLast()));
        response.setResults(taiKhoans.getContent().stream().map(tk->new Select2Response.Result(tk.getIdTaiKhoan(),tk.getTen())).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }
    @GetMapping("/api/v1/select2/san-pham")
    @ResponseBody
    public ResponseEntity<?> getAllEml(
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "searchKey", required = false) Optional<String> searchKey
    ) {
        Pageable pageAble = PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Select2Response.Result> sanPhams;
        if (searchKey.isPresent()) {
            sanPhams = sanPhamRepository.findAllSanPhamLike(searchKey.get(), pageAble);
        } else {
            sanPhams = sanPhamRepository.findAllSanPham(pageAble);
        }
        Select2Response response = new Select2Response();
        response.setPagination(new Select2Response.Pagination(!sanPhams.isLast()));
        response.setResults(sanPhams.getContent());
        return ResponseEntity.ok(response);
    }

//     private static final Logger log = LoggerFactory.getLogger(KhoController.class);
//    @Autowired
//    private ModelMapper modelMapper;
//    @Autowired
//    private CongNgheManHinhRepository congNgheManHinhRepository;
//
//    @GetMapping("/admin/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public String ui(Model model) {
//        return "/admin/san-pham-chi-tiet/cong-nghe-man-hinh";
//    }
//
//    @ResponseBody
//    @GetMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public List<Response> getAll() {
//        return modelMapper.map(congNgheManHinhRepository.findAll(), new TypeToken<List<Response>>() {
//        }.getType());
//    }
//
//    @ResponseBody
//    @PutMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public Response add(@Valid @RequestBody AddRequest req, Errors errors, HttpServletResponse response) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        CongNgheManHinh cnmh = modelMapper.map(req, CongNgheManHinh.class);
//        Response cnmhRP = modelMapper.map(congNgheManHinhRepository.save(cnmh), Response.class);
//        response.setStatus(HttpStatus.CREATED.value());
//        return cnmhRP;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public Response update(@Valid @RequestBody EditReq editReq, Errors errors, HttpServletResponse resp) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors.getFieldError().getDefaultMessage());
//        }
//        if (editReq.getId() == null || !congNgheManHinhRepository.existsById(editReq.getId())) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        congNgheManHinhRepository.save(modelMapper.map(editReq, CongNgheManHinh.class));
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return congNgheManHinhRepository.findById(editReq.getId()).map((element) -> modelMapper.map(element, Response.class)).get();
//    }
//
//
//    @ResponseBody
//    @DeleteMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh")
//    public Response delete(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !congNgheManHinhRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = congNgheManHinhRepository.findById(id).get();
//        cnmhE.setDeleted(true);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        congNgheManHinhRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh/revert")
//    public Response revert(@RequestParam(name = "id") Integer id, HttpServletResponse resp) {
//        if (id == null || !congNgheManHinhRepository.existsById(id)) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
//        }
//        var cnmhE = congNgheManHinhRepository.findById(id).get();
//        cnmhE.setDeleted(false);
//        var cnmhR = modelMapper.map(cnmhE, Response.class);
//        congNgheManHinhRepository.save(cnmhE);
//        resp.setStatus(HttpStatus.ACCEPTED.value());
//        return cnmhR;
//    }
//
//
//    @ResponseBody
//    @PostMapping("/api/v1/san-pham-chi-tiet/cong-nghe-man-hinh/import-excel")
//    @Transactional
//    public List<Response> importExcel(@Valid @RequestBody List<@Valid ImportReq> lstCNMH, Errors errors) {
//        if (errors.hasErrors()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lỗi: Vui lòng kiểm tra lại các trường trong file excel !");
//        }
//        try{
//            List<CongNgheManHinh> lstMS = lstCNMH.stream()
//                    .map((element) -> modelMapper.map(element, CongNgheManHinh.class))
//                    .collect(Collectors.toList());
//            List<Response> lstRP = congNgheManHinhRepository.saveAll(lstMS).stream()
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
//    @GetMapping("/admin/san-pham-chi-tiet/cong-nghe-man-hinh/export-excel")
//    public ResponseEntity<byte[]> exportToExcel() throws IOException {
//        List<CongNgheManHinh> mauSacList = congNgheManHinhRepository.findAll();
//        ExportExcel<CongNgheManHinh> exportExcel = new ExportExcel();
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
