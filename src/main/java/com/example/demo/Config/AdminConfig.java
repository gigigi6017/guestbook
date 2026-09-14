package com.example.demo.Config;

import com.example.demo.Entity.Admin;
import com.example.demo.Repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminConfig {

    // BCrypt 비밀번호 암호화 기능
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 프로그램이 시작될 때 관리자 계정을 확인하고 없으면 생성
    @Bean
    public CommandLineRunner createAdmin(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // 이미 관리자 계정이 있으면 아무것도 하지 않음
            if (adminRepository.count() > 0) {
                return;
            }

            // 관리자 비밀번호
            String adminPassword = "zomboid";

            // 비밀번호를 BCrypt로 암호화
            String encryptedPassword =
                    passwordEncoder.encode(adminPassword);

            // 관리자 계정 생성
            Admin admin = new Admin(encryptedPassword);

            // DB에 저장
            adminRepository.save(admin);

            System.out.println("관리자 계정이 생성되었습니다.");
        };
    }
}