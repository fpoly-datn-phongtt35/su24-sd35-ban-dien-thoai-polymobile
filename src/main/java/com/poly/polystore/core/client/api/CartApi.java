package com.poly.polystore.core.client.api;

import com.poly.polystore.core.client.api.request.CartRequest;
import com.poly.polystore.core.client.api.request.ChangeSPCartRequest;
import com.poly.polystore.core.common.login.service.JwtService;
import com.poly.polystore.entity.GioHang;
import com.poly.polystore.entity.KhachHang;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.GioHangRepository;
import com.poly.polystore.repository.KhachHangRepository;
import com.poly.polystore.repository.SanPhamChiTietRepository;
import com.poly.polystore.repository.TaiKhoanRepository;
import com.poly.polystore.utils.CookieUlti;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final CookieUlti cookieUlti;
    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestParam int idSanPhamChiTiet, @RequestParam int quantity,
                                       HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            List<GioHang> dataFromCart = cookieUlti.getDataFromCart(request);
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
            Cookie[] cookies = request.getCookies();
            if(cookies != null){
                for(Cookie i : cookies){
                    if(i.getName().equals("Cart")){
                        i.setMaxAge(0);
                        response.addCookie(i);
                        break;
                    }
                }
            }
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
                        gioHangRepository.save(gioHang);
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

    @PostMapping("/changesp")
    public ResponseEntity<?> changeSP(@RequestBody ChangeSPCartRequest cartRequest, HttpServletRequest request, HttpServletResponse response){
        TaiKhoan taiKhoan = cookieUlti.getTaiKhoan(request);
        List<GioHang> gioHangs = new ArrayList<>();
        if(taiKhoan != null) {
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(taiKhoan.getId());
            if(optionalKhachHang.isPresent()) {
                KhachHang khachHang = optionalKhachHang.get();
                gioHangs = gioHangRepository.findByIdKhachHang(khachHang.getId());
            }
        }
        else {
            gioHangs = cookieUlti.getDataFromCart(request);
        }
        GioHang oldGioHang = gioHangs.stream().filter(n -> n.getIdSanPhamChiTiet().getId() == cartRequest.getOldID()).findFirst().get();
        oldGioHang.setIdSanPhamChiTiet(sanPhamChiTietRepository.findById(cartRequest.getNewID()).get());
        if (taiKhoan == null){
            StringBuilder cart = new StringBuilder();
            for(GioHang gioHang : gioHangs){
                cart.append(gioHang.getIdSanPhamChiTiet().getId());
                cart.append("/");
                cart.append(gioHang.getSoLuong());
                cart.append("/");
            }
            cart.deleteCharAt(cart.length() - 1);
            Cookie[] cookies = request.getCookies();
            for(Cookie i : cookies){
                if(i.getName().equals("Cart")){
                    i.setMaxAge(0);
                    response.addCookie(i);
                    break;
                }
            }
            Cookie cookie = new Cookie("Cart", cart.toString());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
        }
        else {
            gioHangRepository.saveAll(gioHangs);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateCart")
    public ResponseEntity<?> updateCart(@RequestBody CartRequest cartRequest,
                                        HttpServletRequest request, HttpServletResponse response)
    {
        String token = request.getHeader("Authorization");
        List<GioHang> dataFromCart = new ArrayList<>();
        List<GioHang> temp = new ArrayList<>();
        if (token == null) {
            dataFromCart = cookieUlti.getDataFromCart(request);
        }
        else {
            token = token.substring(7);
            String email = jwtService.extractEmail(token);
            Optional<TaiKhoan> optionalTaiKhoan = taiKhoanRepository.findByEmail(email);
            Optional<KhachHang> optionalKhachHang = khachHangRepository.findByIdTaiKhoan(optionalTaiKhoan.get().getId());
            dataFromCart = gioHangRepository.findByIdKhachHang(optionalTaiKhoan.get().getId());
            temp = gioHangRepository.findByIdKhachHang(optionalKhachHang.get().getId());
        }
        if("update".equals(cartRequest.getAction())) {
            for (GioHang gioHang : dataFromCart) {
                if(gioHang.getIdSanPhamChiTiet().getId() == cartRequest.getIdSanPhamChiTiet()){
                    gioHang.setSoLuong(cartRequest.getQuantity());
                    break;
                }
            }
        }
        else if("remove".equals(cartRequest.getAction())) {
            for (int i = 0;i < dataFromCart.size(); i++) {
                if(dataFromCart.get(i).getIdSanPhamChiTiet().getId() == cartRequest.getIdSanPhamChiTiet()){
                    dataFromCart.remove(i);
                    i--;
                }
            }
        }
        if (token == null){
            StringBuilder cart = new StringBuilder();
            for(GioHang gioHang : dataFromCart){
                cart.append(gioHang.getIdSanPhamChiTiet().getId());
                cart.append("/");
                cart.append(gioHang.getSoLuong());
                cart.append("/");
            }
            cart.deleteCharAt(cart.length() - 1);
            Cookie[] cookies = request.getCookies();
            for(Cookie i : cookies){
                if(i.getName().equals("Cart")){
                    i.setMaxAge(0);
                    response.addCookie(i);
                    break;
                }
            }
            Cookie cookie = new Cookie("Cart", cart.toString());
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
        }
        else {
            gioHangRepository.deleteAll(temp);
            gioHangRepository.saveAll(dataFromCart);
        }
        return ResponseEntity.ok().build();
    }
}
