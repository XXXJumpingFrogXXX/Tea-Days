package top.nkchayuan.teadaysbackend.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import top.nkchayuan.teadaysbackend.domain.BulletinInfo;

import java.util.ArrayList;

@Repository
public interface BulletinMapper {
    @Select("select title, bulletin_id, img_url from bulletin")
    ArrayList<BulletinInfo> getAllBulletin();

    @Select("select title, content, bulletin_id, img_url from bulletin where bulletin_id = #{id}")
    BulletinInfo getBulletin(@Param("id") Integer id);

    @Delete("delete from bulletin where bulletin_id=#{bulletin_id}")
    void remove(@Param("bulletin_id") Integer bulletin_id);

    @Update("update bulletin set title=#{title}, content=#{content}, img_url=#{img_url} where bulletin_id=#{bulletin_id}")
    void update(@Param("bulletin_id") Integer bulletin_id, @Param("title") String title, @Param("content") String content, @Param("img_url") String img_url);

    @Insert("Insert into bulletin (title, content, img_url) values (#{title}, #{content}, #{img_url})")
    void insert(@Param("title") String title, @Param("content") String content, @Param("img_url") String img_url);
}
