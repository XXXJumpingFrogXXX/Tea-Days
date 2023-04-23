package top.nkchayuan.teadaysbackend.domain;

import lombok.Data;

@Data
public class BulletinInfo {
    public String title;
    public String img_url;
    public String content;
    public Integer bulletin_id;
}
