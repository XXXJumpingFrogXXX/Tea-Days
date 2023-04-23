package top.nkchayuan.teadaysbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import top.nkchayuan.teadaysbackend.domain.ThingInfo;

import java.util.ArrayList;

@Repository
public interface ThingsMapper {
    @Select("select info from things_info where type=#{type} ")
    ArrayList <String> getInfo(@Param("type") Integer type);

    @Select("select info from things_info where type > 9")
    ArrayList<String> getTeaInfo();
    @Select("select name from things_info where type > 9")
    ArrayList<String> getTeaName();

    @Select("select name,type,info from things_info where type=#{type}")
    ThingInfo[] getFullData(@Param("type") Integer type);

    @Select("select name,type,info from things_info where type=0 or type>9")
    ThingInfo[] selectTea();

    @Select("select name,type,info from things_info where type=1")
    ThingInfo[] selectTool();

    @Select("select name,type,info from things_info where type=2")
    ThingInfo[] selectItem();

    @Select("select name,type,info from things_info where type=3")
    ThingInfo[] selectTerm();

    @Insert("insert into things_info(name,type,info) values(#{name},#{type},#{info})")
    void insertThing(@Param("name") String name, @Param("type") Integer type, @Param("info") String Info);

    @Update("delete from things_info where name=#{name}")
    void removeThing(@Param("name") String name);

    @Update("update things_info set type=#{type}, info=#{info} where name=#{name}")
    void updateThingInfo(@Param("name") String name, Integer type, String info);

    @Select("select name,type,info from things_info where name=#{name}")
    ThingInfo name_selectThing(@Param ("name")String name);
}

