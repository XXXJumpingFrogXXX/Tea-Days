package top.nkchayuan.teadaysbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import top.nkchayuan.teadaysbackend.domain.User;
import top.nkchayuan.teadaysbackend.domain.UserInfo;

@Repository
public interface UserMapper {
    @Insert("INSERT INTO usr_info(mailbox,nickname,PASSWORD,register_time) VALUES(#{mailbox},#{nickname},#{password},#{register_time})")
    int saveUser(@Param ("mailbox")String mailbox, @Param ("nickname")String nickname, @Param ("password")String password,@Param("register_time")Integer register_time);

    @Select("select user_id,mailbox,nickname,password,role from usr_info where mailbox=#{mailbox}")
    User mail_selectUser(@Param ("mailbox")String mailbox);

    @Select("select user_id,mailbox,nickname,money,role from usr_info where user_id=#{user_id}")
    UserInfo id_selectUser(@Param ("user_id")Integer user_id);

    @Select("select user_id,mailbox,nickname,money,role from usr_info")
    UserInfo[] selectAll();

    @Select("select role from usr_info where user_id=#{user_id}")
    int queryRole(@Param("user_id") Integer user_id);

    @Update("update usr_info set money=#{money} where user_id=#{user_id}")
    void updatemoney(@Param("money")Integer money,@Param("user_id")Integer user_id);

    @Update("update usr_info set nickname=#{nickname}, money=#{money}, role=#{role} where user_id=#{user_id}")
    void updateUserInfo(@Param("user_id") Integer user_id, String nickname, Integer money, Integer role);

    @Update("delete from usr_info where user_id=#{user_id}")
    void removeUser(@Param("user_id") Integer user_id);
}