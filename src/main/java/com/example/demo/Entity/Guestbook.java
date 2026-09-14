package com.example.demo.Entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Table(name = "Guestbook")
@Getter
@Setter
@NoArgsConstructor
public class Guestbook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int rating;

    @NotBlank(message = "이름을 입력해주세요.")
    @Column(length = 12)
    private String name;

    @NotBlank(message = "내용을 입력해주세요.")
    @Column(length = 40)
    private String content;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Column(length = 4)
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}