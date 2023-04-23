package top.nkchayuan.teadaysbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import top.nkchayuan.teadaysbackend.domain.Item;

import java.util.ArrayList;

@Repository
public interface InventoryMapper {
    @Select("select * from inventory where user_id=#{user_id}")
    ArrayList<Item> selectAll(@Param("user_id") Integer user_id);

    @Select("select * from inventory where user_id=#{user_id} and item_id=#{item_id}")
    ArrayList<Item> selectItem(@Param("user_id") Integer user_id, @Param("item_id") Integer item_id);

    @Update("update inventory set count=#{count} where user_id=#{user_id} and item_id=#{item_id}")
    void modifyCount(@Param("user_id") Integer user_id, @Param("item_id") Integer item_id, @Param("count") Integer count);

    @Insert("insert into inventory (user_id, item_id, count) values(#{user_id}, #{item_id}, #{count})")
    void createItem(@Param("user_id") Integer user_id, @Param("item_id") Integer item_id, @Param("count") Integer count);

    @Insert("insert into user_sampling (user_id, item_id) values(#{user_id}, #{item_id})")
    void insertUserSampling(@Param("user_id") Integer user_id, @Param("item_id") Integer item_id);

    @Update("update user_sampling set item_id = #{item_id} where user_id = #{user_id}")
    void updateUserSampling(@Param("user_id") Integer user_id, @Param("item_id") Integer item_id);

    @Select("select item_id from user_sampling where user_id = #{user_id}")
    ArrayList<Integer> getUserSampling(@Param("user_id") Integer user_id);
}
