package com.poly.polystore;

import com.poly.polystore.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.stereotype.Component;

@SpringBootApplication()
public class PolyStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PolyStoreApplication.class, args);
    }
    @Component
    @RequiredArgsConstructor
    public class TEST implements ApplicationRunner {
        private final SanPhamRepository sanPhamRepository;

        @Override
        public void run(ApplicationArguments args) throws Exception {
//            System.out.println(sanPhamRepository.findAll().get(0));
        }
    }

}
