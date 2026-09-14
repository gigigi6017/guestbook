package com.example.demo.Service;

import com.example.demo.Entity.Guestbook;
import com.example.demo.Repository.GuestbookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuestbookService {

    private final GuestbookRepository guestbookRepository;

    public GuestbookService(GuestbookRepository guestbookRepository) {
        this.guestbookRepository = guestbookRepository;
    }

    // 댓글 저장
    public Guestbook save(Guestbook guestbook) {
        return guestbookRepository.save(guestbook);
    }

    // 전체 댓글 조회
    public List<Guestbook> findAll() {
        return guestbookRepository.findAll();
    }

    // 댓글 수정 ?? 이였나
    public Guestbook update(Guestbook guestbook) {

        // 수정할 기존 방명록 찾기
        Guestbook existingGuestbook = guestbookRepository.findById(guestbook.getId())
                .orElseThrow(() -> new RuntimeException("방명록을 찾을 수 없습니다."));

        // 기존 비밀번호와 입력한 비밀번호 비교


        if (!java.util.Objects.equals(
                existingGuestbook.getPassword(),
                guestbook.getPassword()
        )) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 수정할 내용만 기존 데이터에 반영
        existingGuestbook.setName(guestbook.getName());
        existingGuestbook.setRating(guestbook.getRating());
        existingGuestbook.setContent(guestbook.getContent());

        // 수정된 데이터 저장
        return guestbookRepository.save(existingGuestbook);
    }


    //방문록 삭제 기능
    public void delete(Integer id, String password) {

        Guestbook guestbook = guestbookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("방명록을 찾을 수 없습니다."));

        if (!guestbook.getPassword().equals(password)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        guestbookRepository.delete(guestbook);
    }





    // 여기서 오류 찾고 컨트롤러에서 주고    if 문으로 오류을 찾고 트라이 캣취


    // null 공백 예외 처리 1          자기가 쓴거 삭제 수정 기능 추가  2      관리자 페이지 만들어서 방문록 수정 삭제 추가 후순위 3       ai도움

}