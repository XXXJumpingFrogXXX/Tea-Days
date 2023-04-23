package top.nkchayuan.teadaysbackend.domain;

import lombok.Data;

@Data
public class LogInUser {
    private String mailbox;
    private String password;
}