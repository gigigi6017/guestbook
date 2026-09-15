package com.example.demo.Service;

import com.example.demo.Entity.Guestbook;
import com.example.demo.Repository.GuestbookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class GuestbookService {

    private final GuestbookRepository guestbookRepository;

    public GuestbookService(GuestbookRepository guestbookRepository) {
        this.guestbookRepository = guestbookRepository;
    }

    // 방명록 저장
    public Guestbook save(Guestbook guestbook) {

        // 이름이 비어 있는지 확인
        if (guestbook.getName() == null ||
                guestbook.getName().trim().isEmpty()) {

            throw new RuntimeException("이름을 입력해주세요.");
        }

        // 내용이 비어 있는지 확인
        if (guestbook.getContent() == null ||
                guestbook.getContent().trim().isEmpty()) {

            throw new RuntimeException("내용을 입력해주세요.");
        }

        // 비밀번호가 비어 있는지 확인
        if (guestbook.getPassword() == null ||
                guestbook.getPassword().trim().isEmpty()) {

            throw new RuntimeException("비밀번호를 입력해주세요.");
        }

        // 비밀번호 앞뒤 공백 제거
        guestbook.setPassword(guestbook.getPassword().trim());

        // 방명록 저장
        return guestbookRepository.save(guestbook);
    }

    // 전체 방명록 조회
    public List<Guestbook> findAll() {

        return guestbookRepository.findAll();
    }

    // 방명록 수정
    public Guestbook update(Guestbook guestbook) {

        // ID가 없는 경우
        if (guestbook.getId() <= 0) {
            throw new RuntimeException("잘못된 방명록 ID입니다.");
        }

        // 입력한 ID로 기존 방명록 찾기
        Guestbook existingGuestbook =
                guestbookRepository.findById(guestbook.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "방명록을 찾을 수 없습니다."
                                )
                        );

        // 입력한 비밀번호 확인
        checkPassword(
                existingGuestbook.getPassword(),
                guestbook.getPassword()
        );

        // 이름 확인
        if (guestbook.getName() == null ||
                guestbook.getName().trim().isEmpty()) {

            throw new RuntimeException("이름을 입력해주세요.");
        }

        // 내용 확인
        if (guestbook.getContent() == null ||
                guestbook.getContent().trim().isEmpty()) {

            throw new RuntimeException("내용을 입력해주세요.");
        }

        // 수정할 데이터만 변경
        existingGuestbook.setName(
                guestbook.getName().trim()
        );

        existingGuestbook.setRating(
                guestbook.getRating()
        );

        existingGuestbook.setContent(
                guestbook.getContent().trim()
        );

        // 기존 비밀번호는 변경하지 않음
        // existingGuestbook의 비밀번호를 그대로 유지

        // 수정된 방명록 저장
        return guestbookRepository.save(existingGuestbook);
    }

    // 일반 사용자 방명록 삭제
    public void delete(Integer id, String password) {

        // ID가 없는 경우
        if (id == null || id <= 0) {
            throw new RuntimeException("잘못된 방명록 ID입니다.");
        }

        // 비밀번호가 없는 경우
        if (password == null ||
                password.trim().isEmpty()) {

            throw new RuntimeException("비밀번호를 입력해주세요.");
        }

        // 입력한 ID로 기존 방명록 찾기
        Guestbook guestbook =
                guestbookRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "방명록을 찾을 수 없습니다."
                                )
                        );

        // DB 비밀번호와 입력 비밀번호 비교
        checkPassword(
                guestbook.getPassword(),
                password
        );

        // 비밀번호가 일치하면 삭제
        guestbookRepository.delete(guestbook);
    }

    // 관리자 방명록 삭제
    public void adminDelete(Integer id) {

        // ID가 없는 경우
        if (id == null || id <= 0) {
            throw new RuntimeException("잘못된 방명록 ID입니다.");
        }

        // 삭제할 방명록 찾기
        Guestbook guestbook =
                guestbookRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "방명록을 찾을 수 없습니다."
                                )
                        );

        // 관리자 권한이므로 비밀번호 확인 없이 삭제
        guestbookRepository.delete(guestbook);
    }

    // 비밀번호 확인 공통 메서드
    private void checkPassword(
            String savedPassword,
            String inputPassword
    ) {

        // DB 비밀번호가 없는 경우
        if (savedPassword == null ||
                savedPassword.trim().isEmpty()) {

            throw new RuntimeException(
                    "저장된 비밀번호가 없습니다."
            );
        }

        // 입력 비밀번호가 없는 경우
        if (inputPassword == null ||
                inputPassword.trim().isEmpty()) {

            throw new RuntimeException(
                    "비밀번호를 입력해주세요."
            );
        }

        // 앞뒤 공백 제거 후 비교
        String saved = savedPassword.trim();
        String input = inputPassword.trim();

        // 비밀번호가 다르면 오류 발생
        if (!Objects.equals(saved, input)) {

            throw new RuntimeException(
                    "비밀번호가 일치하지 않습니다."
            );
        }
    }
}