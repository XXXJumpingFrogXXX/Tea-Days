package top.nkchayuan.teadaysbackend.domain;

import lombok.Data;

@Data
public class RegisterUser {
    private String mailbox;
    private String nickname;
    private String password;
}