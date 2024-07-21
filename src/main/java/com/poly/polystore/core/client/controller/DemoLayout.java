package com.poly.polystore.core.client.controller;

import com.poly.polystore.core.client.models.request.RequestAddKhachHang;
import com.poly.polystore.core.client.models.response.KhachHangRepose;
import com.poly.polystore.entity.DiaChiGiaoHang;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.repository.DiaChiGiaoHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DemoLayout {


    @GetMapping("/demo-client")
    public String demoClient(Model model) {

        return "client/page/demo";
    }


}
