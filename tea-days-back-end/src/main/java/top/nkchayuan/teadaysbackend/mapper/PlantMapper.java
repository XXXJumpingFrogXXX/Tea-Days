package top.nkchayuan.teadaysbackend.mapper;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import top.nkchayuan.teadaysbackend.domain.Soil;

import java.util.ArrayList;

@Repository
public interface PlantMapper {
    @Update("update soil_info set plant_species=#{plant_species},update_time=#{update_time},progress=#{progress} where user_id=#{user_id} and soil_id=#{soil_id}")
    void update(@Param ("plant_species")Integer plant_species, @Param("update_time") Integer plant_time, @Param ("user_id")Integer user_id, @Param ("soil_id")Integer soil_id, @Param("progress") Integer progress);
    @Select("select soil_id,user_id,update_time,plant_species,progress from soil_info where soil_info.user_id=#{user_id} order by soil_id")
    ArrayList<Soil> readAll(@Param("user_id") Integer user_id);

    @Select("select soil_id,user_id,update_time,plant_species,progress from soil_info where soil_info.user_id=#{user_id} and soil_id=#{soil_id}")
    ArrayList<Soil> readOne(@Param("user_id") Integer user_id , @Param ("soil_id")Integer soil_id);

    @Insert("INSERT INTO soil_info(user_id,soil_id,plant_species,update_time, progress) VALUES(#{user_id},#{soil_id},#{plant_species},#{plant_time}, 0)")
    void insert(@Param ("user_id")Integer user_id, @Param ("soil_id")Integer soil_id, @Param ("plant_species")Integer plant_species, @Param("plant_time") Integer plant_time);
}