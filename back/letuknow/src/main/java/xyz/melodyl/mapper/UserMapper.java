package xyz.melodyl.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.melodyl.pojo.User;

@Mapper
@Repository("UserMapper")
public interface UserMapper {

    @Select("SELECT id, user_name FROM login_user WHERE user_name = #{name} AND user_password = #{password}")
    @Results({
            @Result(id = true, column = "id", property = "userID"),
            @Result(column = "user_name", property = "userName"),
            @Result(column = "user_password", property = "password"),

    })
    User selectDecryptionUser(@Param("name") String userName, @Param("password") String password);
}
