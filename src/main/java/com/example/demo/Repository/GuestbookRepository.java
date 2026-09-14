package com.example.demo.Repository;

import com.example.demo.Entity.Guestbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestbookRepository extends JpaRepository<Guestbook, Integer> {

}

// 데이터 베이스 접근 정보 가져오기