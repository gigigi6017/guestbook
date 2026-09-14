package com.example.demo.Controller;

import com.example.demo.Service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request
    ) {

        String password = request.get("password");

        // 비밀번호가 입력되지 않은 경우
        if (password == null || password.isBlank()) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "비밀번호를 입력해주세요.")
            );
        }

        // 비밀번호 확인
        boolean success = adminService.checkPassword(password);

        // 성공
        if (success) {
            return ResponseEntity.ok(
                    Map.of("message", "관리자 로그인 성공")
            );
        }

        // 실패
        return ResponseEntity.status(401).body(
                Map.of("message", "비밀번호가 올바르지 않습니다.")
        );
    }
}