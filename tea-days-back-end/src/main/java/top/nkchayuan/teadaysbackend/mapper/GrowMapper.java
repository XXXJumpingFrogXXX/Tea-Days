package top.nkchayuan.teadaysbackend.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface GrowMapper {
//    TODO:数据库soil_info表添加plant_time记录播种，以便计算生长时长
    @Select("select plant_time from soil_info where user_id=#{user_id} and soil_id=#{soil_id}")
    int register_time(@Param("user_id")Integer user_id);
}
