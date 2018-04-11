package com.briar.server.mapper;


import com.briar.server.model.domainmodelclasses.UserContact;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserContactMapper {

    @Insert("insert into contacts " +
            "(first_user, first_user_contact_acceptance, second_user, second_user_contact_acceptance, is_active, created, modified)" +
            "values (#{firstUserId}, #{firstUserContactAcceptance}, #{secondUserId}, #{secondUserContactAcceptance}, 1, current_timestamp(), current_timestamp());")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    void addNewUserContact(UserContact userContact);

    @Update("update contacts " +
            "set first_user = #{firstUserId}, first_user_contact_acceptance = #{firstUserContactAcceptance}, second_user = #{secondUserId}, second_user_contact_acceptance = #{secondUserContactAcceptance}, modified = current_timestamp() " +
            "where id = #{id} and is_active = 1")
    void modifyUserContact(UserContact userContact);

    @Update("update contacts " +
            "set is_active = 0, modified = current_timestamp() " +
            "where first_user = #{id} and is_active = 1; " +
            "update contacts " +
            "set is_active = 0, modified = current_timestamp() " +
            "where second_user = #{id} and is_active = 1; ")
    void removeUserContact(long userId);

    @Update("update contacts " +
            "set is_active = 0, modified = current_timestamp() " +
            "where id = #{id} and is_active = 1; ")
    void removeSpecificUserContact(UserContact userContact);

    @Select("select " +
            "c.id, " +
            "u1.phone_generated_id as 'first_user_name', " +
            "u1.id as 'first_user_id', " +
            "c.first_user_contact_acceptance, " +
            "u2.phone_generated_id as 'second_user_name', " +
            "u2.id as 'second_user_id', " +
            "c.second_user_contact_acceptance " +
            "from " +
            "contacts as c " +
            "inner join users as u1 on u1.id = c.first_user " +
            "inner join users as u2 on u2.id = c.second_user " +
            "where c.first_user = #{userId} and c.is_active = 1 " +
            "" +
            "UNION " +
            "" +
            "select " +
            "c.id, " +
            "u1.phone_generated_id as 'first_user_name', " +
            "u1.id as 'first_user_id', " +
            "c.first_user_contact_acceptance, " +
            "u2.phone_generated_id as 'second_user_name'," +
            "u2.id as 'second_user_id', " +
            "c.second_user_contact_acceptance " +
            "from contacts as c " +
            "inner join users as u1 on u1.id = c.first_user " +
            "inner join users as u2 on u2.id = c.second_user " +
            "where c.second_user = #{userId} and c.is_active = 1")
    List<UserContact> findContacts(long userId);
}
