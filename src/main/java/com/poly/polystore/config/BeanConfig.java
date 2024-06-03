package com.poly.polystore.config;

import com.poly.polystore.core.common.login.service.TaiKhoanService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@RequiredArgsConstructor
@Configuration
public class BeanConfig {
    private final TaiKhoanService taiKhoanService;

    @Bean
    public ModelMapper modelMapper() {
        var mapper= new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

                return taiKhoanService.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));
            }
        };
    }


}
