package com.poly.polystore.filter;

import com.poly.polystore.service.TaiKhoanService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final TaiKhoanService taiKhoanService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String sdtOrEmail;
        if (authHeader.isEmpty() || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);//
        }
        jwt = authHeader.substring(7);
        log.debug("JWT {}", jwt);
        sdtOrEmail = jwtService.extractUserName(jwt);// Trích xuất dữ liệu
        if (!sdtOrEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {// Kiểm tra có null không ? Có được cấp quyền trước đó hay chưa
            UserDetails userDetail = taiKhoanService.getUserDetailsService().loadUserByUsername(sdtOrEmail);// Kiểm tra tồn tại trong db
            if (jwtService.isTokenValid(jwt, userDetail)) {
                log.debug("User - {}", userDetail);
                SecurityContext context = SecurityContextHolder.createEmptyContext();//Tạo context chứa thông tin xác thực
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(// Đối tượng chứa thông tin xác thực (userdetail,pass,role)
                        userDetail, null, userDetail.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                Dòng mã authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                không bắt buộc nhưng có thể hữu ích trong một số trường hợp cụ thể.
//                Nó được sử dụng để bổ sung thông tin chi tiết về việc xác thực của người dùng vào đối tượng UsernamePasswordAuthenticationToken. T
//                hông tin này có thể hữu ích cho việc ghi nhận và kiểm tra lại các hoạt động xác thực của người dùng,
//                đặc biệt là khi bạn muốn biết các thông tin như địa chỉ IP của máy khách, trình duyệt mà họ sử dụng, hoặc các thông tin khác về request.
//                Tuy nhiên, nếu ứng dụng của bạn không cần thông tin chi tiết này, hoặc nếu bạn không quan tâm đến nó trong quá trình xác thực,
//                bạn có thể bỏ qua dòng mã này mà không gây ra vấn đề gì. Nó chỉ là một phần của việc tùy chỉnh và mở rộng chức năng của xác thực trong ứng dụng của bạn.
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }

        }
        filterChain.doFilter(request, response);
    }
}
