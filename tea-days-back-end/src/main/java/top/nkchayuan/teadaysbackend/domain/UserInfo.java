package top.nkchayuan.teadaysbackend.domain;


import lombok.Data;

//with no password
@Data
public class UserInfo {
    private Integer user_id;
    private String mailbox;
    private String nickname;
    private Integer money;
    private Integer role;
}

