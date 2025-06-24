package com.ohgiraffers.no_injung.auth.Request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequest { //email, password, nickname api 전송

    private String email;


    private String password;


    private String nickname;

}
