package com.example.demo.Controller;

import com.example.demo.Entity.Guestbook;
import com.example.demo.Service.GuestbookService;
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

    // [추가] 사이트 맨 처음 주소("/")로 접속했을 때 text.html을 띄워주는 매핑
//    @GetMapping("/")
//    public String index() {
//        return "text";
//    }

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

    // 방문록 수정 기능
    @PutMapping("/put")
    public Guestbook update(@RequestBody Guestbook guestbook) {
        return guestbookService.update(guestbook);
    }

    // 방문록 삭제 기능
    @DeleteMapping("/delete")
    public String delete(
            @RequestParam Integer id,
            @RequestParam String password
    ) {
        guestbookService.delete(id, password);
        return "글이 삭제 되었습니다.";
    }
}