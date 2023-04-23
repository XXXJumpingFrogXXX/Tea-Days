package top.nkchayuan.teadaysbackend.domain;

import lombok.Data;

@Data
public class User {
    private Integer user_id;
    private String mailbox;
    private String nickname;
    private String password;
    private Integer money;
    private Integer role;
}