package top.nkchayuan.teadaysbackend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.sql.Time;
@Repository
public interface CalendarMapper {
    @Select("select register_time from usr_info where user_id=#{user_id}")
    int register_time(@Param("user_id")Integer user_id);
}
