package com.poly.polystore.config;

import com.poly.polystore.core.admin.san_pham.model.reponse.SanPhamDataTable;
import com.poly.polystore.core.common.login.service.TaiKhoanService;
import com.poly.polystore.entity.SanPham;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
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
        var mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);


//        mapper.typeMap(SanPham.class, SanPhamDataTable.class).addMappings(
//          mp-> {
//              //map(getterSrc,setterDes)
//              //Map từ list Imeis sang tổng sản phẩm của SanPhamDataTable
//              mp.map(src ->
//                      src.getSanPhamChiTiet().stream()
//                              .map(spct->spct.getImeis().size())
//                              .reduce(0,Integer::sum),
//                      SanPhamDataTable::setSoLuong);
//              //Map từ list SanPhamChiTiet sang list mã màu của SanPhamDataTable
//              mp.map(src ->
//                              src.getSanPhamChiTiet().stream()
//                                      .map(spct->spct.getMauSac())
//                                      .filter(mauSac -> !mauSac.getDeleted())
//                                      .map(ms->ms.getMa()),
//                      SanPhamDataTable::setMauSac);
//
//              // Todo Thêm các customs mapper khác
//          }
//        );
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
