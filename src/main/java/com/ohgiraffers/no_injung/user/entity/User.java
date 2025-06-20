package com.ohgiraffers.no_injung.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email; //로그인 아이디로 사용

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;

    private boolean isDeleted;

    //엔티티가 처음 저장될 때 실행하는 어노테이션
    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.isDeleted = false;
    }

    @PreUpdate
    public void OnUpdate() {
        updateAt = LocalDateTime.now();
    }

    public void softDelete(){
        this.deleteAt = LocalDateTime.now();
        this.isDeleted = true;
    }
}
