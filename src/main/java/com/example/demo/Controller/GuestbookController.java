package com.example.demo.Controller;

import com.example.demo.Entity.Guestbook;
import com.example.demo.Service.GuestbookService;
import jakarta.servlet.http.HttpSession;
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

    // 전체 댓글 조회
    @GetMapping("/get")
    public List<Guestbook> findAll() {
        return guestbookService.findAll();
    }

    // 평점 + 댓글 저장
    @PostMapping("/post")
    public Guestbook save(@RequestBody Guestbook guestbook) {
        return guestbookService.save(guestbook);
    }

    // 방명록 수정
    @PutMapping("/put")
    public Guestbook update(@RequestBody Guestbook guestbook) {
        return guestbookService.update(guestbook);
    }

    // 일반 사용자 삭제
    @DeleteMapping("/delete")
    public String delete(
            @RequestParam Integer id,
            @RequestParam String password
    ) {
        guestbookService.delete(id, password);
        return "글이 삭제 되었습니다.";
    }

    // 관리자 삭제
    @DeleteMapping("/admin-delete")
    public String adminDelete(
            @RequestParam Integer id,
            HttpSession session
    ) {

        // 관리자 로그인 여부 확인
        Boolean isAdmin = (Boolean) session.getAttribute("admin");

        if (!Boolean.TRUE.equals(isAdmin)) {
            throw new RuntimeException("관리자 로그인이 필요합니다.");
        }

        // 관리자이므로 비밀번호 없이 삭제
        guestbookService.adminDelete(id);

        return "관리자 권한으로 글이 삭제되었습니다.";
    }
}