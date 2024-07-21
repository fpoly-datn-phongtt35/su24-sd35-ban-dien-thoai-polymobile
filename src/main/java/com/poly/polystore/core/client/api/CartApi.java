package com.poly.polystore.core.client.api;

import com.poly.polystore.core.common.login.service.JwtService;
import com.poly.polystore.entity.GioHang;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.GioHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import com.poly.polystore.repository.SanPhamChiTietRepository;
import com.poly.polystore.repository.TaiKhoanRepository;
import com.poly.polystore.utils.GetDataFromCookie;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class CartApi {
    private final JwtService jwtService;
    private final TaiKhoanRepository taiKhoanRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final KhachHangRepository khachHangRepository;
    private final GioHangRepository gioHangRepository;
    private final GetDataFromCookie getDataFromCookie;
    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestParam int idSanPhamChiTiet, @RequestParam int quantity,
                                       HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            List<GioHang> dataFromCart = getDataFromCookie.getDataFromCart(request);
            if (dataFromCart == null || dataFromCart.isEmpty()) {
                dataFromCart = new ArrayList<>();
                GioHang gioHang = new GioHang();
                gioHang.setSoLuong(quantity);
                gioHang.setIdSanPhamChiTiet(sanPhamChiTietRepository.findById(idSanPhamChiTiet).get());
                dataFromCart.add(gioHang);
            }
            else {
                boolean flag = false;
                for (GioHang gioHang : dataFromCart) {
                    if(gioHang.getIdSanPhamChiTiet().getId() == idSanPhamChiTiet){
                        gioHang.setSoLuong(gioHang.getSoLuong() + quantity);
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    GioHang gioHang = new GioHang();
                    gioHang.setSoLuong(quantity);
                    gioHang.setIdSanPhamChiTiet(sanPhamChiTietRepository.findById(idSanPhamChiTiet).get());
                    dataFromCart.add(gioHang);
                }
            }
            StringBuilder cart = new StringBuilder();
            for(GioHang gioHang : dataFromCart){
                cart.append(gioHang.getIdSanPhamChiTiet().getId());
                cart.append("/");
                cart.append(gioHang.getSoLuong());
                cart.append("/");
            }
            cart.deleteCharAt(cart.length() - 1);
            Cookie cookie = new Cookie("Cart", cart.toString());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
        }
        else {
            token = token.substring(7);
            String email = jwtService.extractEmail(token);
            Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(email);
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(optionalTaiKhoan.get().getId());
            List<GioHang> list = gioHangRepository.findByIdKhachHang(optionalTaiKhoan.get().getId());
            boolean flag = false;
            if(list != null && !list.isEmpty()) {
                for (GioHang gioHang : list) {
                    if(gioHang.getIdSanPhamChiTiet().getId() == idSanPhamChiTiet){
                        gioHang.setSoLuong(gioHang.getSoLuong() + quantity);
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    GioHang gioHang = new GioHang();
                    gioHang.setSoLuong(quantity);
                    gioHang.setIdKhachHang(optionalKhachHang.get());
                    gioHang.setIdSanPhamChiTiet(sanPhamChiTietRepository.findById(idSanPhamChiTiet).get());
                    gioHangRepository.save(gioHang);
                }
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestParam int idSanPhamChiTiet, @RequestParam(required = false) int quantity,
                                       @RequestParam String action,
                                       HttpServletRequest request, HttpServletResponse response)
    {
        String token = request.getHeader("Authorization");
        if("add".equals(action)) {

        }
        else if("remove".equals(action)) {

        }
        else if("minus".equals(action)) {

        }
        if (token == null) {
            List<GioHang> dataFromCart = getDataFromCookie.getDataFromCart(request);
            if (dataFromCart == null || dataFromCart.isEmpty()) {
                dataFromCart = new ArrayList<>();
                GioHang gioHang = new GioHang();
                gioHang.setSoLuong(quantity);
                gioHang.setIdSanPhamChiTiet(sanPhamChiTietRepository.findById(idSanPhamChiTiet).get());
                dataFromCart.add(gioHang);
            }
            else {
                boolean flag = false;
                for (GioHang gioHang : dataFromCart) {
                    if(gioHang.getIdSanPhamChiTiet().getId() == idSanPhamChiTiet){
                        gioHang.setSoLuong(gioHang.getSoLuong() + quantity);
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    GioHang gioHang = new GioHang();
                    gioHang.setSoLuong(quantity);
                    gioHang.setIdSanPhamChiTiet(sanPhamChiTietRepository.findById(idSanPhamChiTiet).get());
                    dataFromCart.add(gioHang);
                }
            }
            StringBuilder cart = new StringBuilder();
            for(GioHang gioHang : dataFromCart){
                cart.append(gioHang.getIdSanPhamChiTiet().getId());
                cart.append("/");
                cart.append(gioHang.getSoLuong());
                cart.append("/");
            }
            cart.deleteCharAt(cart.length() - 1);
            Cookie cookie = new Cookie("Cart", cart.toString());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
        }
        else {
            token = token.substring(7);
            String email = jwtService.extractEmail(token);
            Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(email);
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(optionalTaiKhoan.get().getId());
            List<GioHang> list = gioHangRepository.findByIdKhachHang(optionalTaiKhoan.get().getId());
            boolean flag = false;
            if(list != null && !list.isEmpty()) {
                for (GioHang gioHang : list) {
                    if(gioHang.getIdSanPhamChiTiet().getId() == idSanPhamChiTiet){
                        gioHang.setSoLuong(gioHang.getSoLuong() + quantity);
                        flag = true;
                        break;
                    }
                }
                if(!flag) {
                    GioHang gioHang = new GioHang();
                    gioHang.setSoLuong(quantity);
                    gioHang.setIdKhachHang(optionalKhachHang.get());
                    gioHang.setIdSanPhamChiTiet(sanPhamChiTietRepository.findById(idSanPhamChiTiet).get());
                    gioHangRepository.save(gioHang);
                }
            }
        }
        return ResponseEntity.ok().build();
    }
}
