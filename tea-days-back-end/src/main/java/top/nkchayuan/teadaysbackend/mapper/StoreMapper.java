package top.nkchayuan.teadaysbackend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreMapper {
    @Select("select money from usr_info where user_id = #{user_id}")
    Integer getMoney(@Param("user_id") Integer user_id);

    @Update("update usr_info set money = #{money} where user_id = #{user_id}")
    void setMoney(@Param("user_id") Integer user_id, @Param("money") Integer money);
}

