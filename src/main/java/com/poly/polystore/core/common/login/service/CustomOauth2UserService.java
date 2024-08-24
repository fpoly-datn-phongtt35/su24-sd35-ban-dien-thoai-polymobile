package com.poly.polystore.core.common.login.service;

import com.poly.polystore.Constant.ROLE;
import com.poly.polystore.entity.Oauth;
import com.poly.polystore.entity.TaiKhoan;
import com.poly.polystore.repository.TaiKhoanRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {
    private final TaiKhoanRepository taiKhoanRepository;
    private final JwtService jwtService;
    private final HttpServletResponse httpServletResponse;
    private final HttpSession httpSession;
    @Value("${jwt.expiration}") Integer expiration;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub"); // Google ID
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Kiểm tra xem người dùng đã tồn tại chưa
        Optional<TaiKhoan> userOptional = taiKhoanRepository.findByEmail(email);
        TaiKhoan user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            var jwtToken= jwtService.generateToken(user);
            Cookie cookie = new Cookie("Authorization", jwtToken);
//        cookie.setSecure(true); // Đảm bảo cookie chỉ được gửi qua HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(expiration); // 10 giờ
            httpServletResponse.addCookie(cookie);
            httpSession.removeAttribute("newTaiKhoan");
        } else {
            httpServletResponse.addCookie(new Cookie("Authorization", ""));
            TaiKhoan tk=new TaiKhoan();
            List<Oauth> oauth=new ArrayList<>();
            oauth.add(new Oauth(providerId, Oauth.Provider.valueOf(provider.toUpperCase()) ,tk));
            tk.setOauths(oauth);
            tk.setEmail(oAuth2User.getAttribute("email"));
            tk.setMatKhau(UUID.randomUUID().toString());
            tk.setTrangthai(true);
            tk.setAnh(oAuth2User.getAttribute("picture"));
            tk.setTen(oAuth2User.getAttribute("name"));
            tk.setRole(ROLE.ROLE_CUSTOMER);
              httpSession.setAttribute("newTaiKhoan", tk);
            try {
                httpServletResponse.sendRedirect("/verify-account");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return oAuth2User;
    }
}
