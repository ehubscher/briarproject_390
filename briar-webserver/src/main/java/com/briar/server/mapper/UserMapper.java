package com.briar.server.mapper;


import com.briar.server.model.domainmodelclasses.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select u.id, u.phone_generated_id, u.password, u.ip, u.port, u" +
            ".status_id, u.avatar_id" +
            " from users as u where phone_generated_id = #{phoneGeneratedId} AND is_active = 1")
    User findUser(String phoneGeneratedId);

    @Insert("insert into users " +
            "(phone_generated_id, password, ip, port, status_id, avatar_id, " +
            "is_active, created, modified) " +
            "values (#{phoneGeneratedId}, #{password}, #{ip}, #{port}, #" +
            "{statusId}, #{avatarId}," +
            " 1, " +
            "current_timestamp(), current_timestamp());")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    void addNewUser(User user);

    @Update("update users " +
            "set phone_generated_id = #{phoneGeneratedId}, " +
            "password = #{password}, ip = #{ip}, port = #{port}, " +
            "status_id = #{statusId}, avatar_id = #{avatarId}, " +
            "modified = current_timestamp() " +
            "where id = #{id} and is_active = 1")
    void modifyUser(User user);

    @Update("update users " +
            "set is_active = 0, modified = current_timestamp() " +
            "where phone_generated_id = #{phoneGeneratedId} and is_active = 1; " +
            "update contacts " +
            "set is_active = 0, modified = current_timestamp() " +
            "where first_user = #{id} and is_active = 1; " +
            "update contacts " +
            "set is_active = 0, modified = current_timestamp() " +
            "where second_user = #{id} and is_active = 1; ")
    void removeUser(User user);
}
