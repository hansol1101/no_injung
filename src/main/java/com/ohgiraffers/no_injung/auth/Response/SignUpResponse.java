package com.ohgiraffers.no_injung.auth.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpResponse {
    private Long id;
    private String email;
    private String nickname;
}
