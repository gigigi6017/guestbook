package com.example.demo.Service;

import com.example.demo.Entity.Admin;
import com.example.demo.Repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 관리자 비밀번호 확인
    public boolean checkPassword(String password) {

        // 관리자 계정이 없는 경우
        Admin admin = adminRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        if (admin == null) {
            return false;
        }

        // 입력한 비밀번호와 DB의 암호화된 비밀번호 비교
        return passwordEncoder.matches(
                password,
                admin.getPassword()
        );
    }
}