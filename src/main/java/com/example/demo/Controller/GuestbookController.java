package com.example.demo.Controller;

import com.example.demo.Entity.Guestbook;
import com.example.demo.Service.GuestbookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guestbook")
@CrossOrigin(origins = "http://localhost:5173")
public class GuestbookController {

    private final GuestbookService guestbookService;

    public GuestbookController(GuestbookService guestbookService) {
        this.guestbookService = guestbookService;
    }

    // 전체 방명록 조회
    @GetMapping("/get")
    public ResponseEntity<List<Guestbook>> findAll() {

        List<Guestbook> guestbooks = guestbookService.findAll();

        return ResponseEntity.ok(guestbooks);
    }

    // 평점 + 방명록 저장
    @PostMapping("/post")
    public ResponseEntity<Guestbook> save(
            @RequestBody Guestbook guestbook
    ) {

        Guestbook savedGuestbook = guestbookService.save(guestbook);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedGuestbook);
    }

    // 방명록 수정
    @PutMapping("/put")
    public ResponseEntity<?> update(
            @RequestBody Guestbook guestbook
    ) {

        try {

            Guestbook updatedGuestbook =
                    guestbookService.update(guestbook);

            return ResponseEntity.ok(updatedGuestbook);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // 일반 사용자 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(
            @RequestParam Integer id,
            @RequestParam String password
    ) {

        try {

            // 전달받은 ID와 비밀번호로 삭제
            guestbookService.delete(id, password);

            return ResponseEntity.ok("글이 삭제되었습니다.");

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    // 관리자 삭제
    @DeleteMapping("/admin-delete")
    public ResponseEntity<String> adminDelete(
            @RequestParam Integer id,
            HttpSession session
    ) {

        // 세션에 저장된 관리자 로그인 여부 확인
        Boolean isAdmin =
                (Boolean) session.getAttribute("admin");

        // 관리자 로그인 여부 확인
        if (!Boolean.TRUE.equals(isAdmin)) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("관리자 로그인이 필요합니다.");
        }

        try {

            // 관리자 권한으로 비밀번호 없이 삭제
            guestbookService.adminDelete(id);

            return ResponseEntity.ok(
                    "관리자 권한으로 글이 삭제되었습니다."
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}