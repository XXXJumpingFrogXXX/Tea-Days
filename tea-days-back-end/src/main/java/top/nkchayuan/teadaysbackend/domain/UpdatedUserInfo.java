package top.nkchayuan.teadaysbackend.domain;

import lombok.Data;

@Data
public class UpdatedUserInfo {
    private Integer user_id;
    private String nickname;
    private Integer money;
    private Integer role;
}
