package com.poly.polystore;

import com.poly.polystore.repository.SanPhamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PolyStoreApplicationTests {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Test
    void contextLoads() {
        sanPhamRepository.deleteById(1006);
    }

}
