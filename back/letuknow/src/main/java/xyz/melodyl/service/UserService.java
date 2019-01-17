package xyz.melodyl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import xyz.melodyl.mapper.UserMapper;
import xyz.melodyl.pojo.TokenCreator;
import xyz.melodyl.pojo.User;
import xyz.melodyl.pojo.UserCache;


@Service("UserService")
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public static final String TOKEN_ATTRIBUTE_NAME = "Token";
    public static final String GUEST_TOKEN = "WELCOME-GUEST";
    private static final String GUEST_USER_NAME = "guest";

    @Autowired
    @Qualifier("UserMapper")
    private UserMapper userMapper;

    @Autowired
    private UserCache userCache;

    @Autowired
    private TokenCreator tokenCreator;

    //I do not check repeated login
    public String getTokenIfUserExist(User user) {
        String token = null;

        User realUser = userMapper.selectDecryptionUser(user.getUserName(), user.getPassword());
        if (realUser != null) {
            if (GUEST_USER_NAME.equals(user.getUserName())) {
                token = GUEST_TOKEN;
            } else {
                token = tokenCreator.createToken();
            }

            cacheUser(token, realUser);
        }

        LOGGER.info("Login.userName-{}. Token:{}", user.getUserName(), token);
        return token;
    }

    public String getUserNameFromToken(String token) {
        User user = getUserFromTokenIfExist(token);
        return user != null ? user.getUserName() : null;
    }

    public Integer getUserIDFromToken(String token) {
        User user = getUserFromTokenIfExist(token);
        return user != null ? user.getUserID() : null;
    }

    private User getUserFromTokenIfExist(String token) {
        return userCache.getUserIfExist(token);
    }

    private void cacheUser(String token, User user) {
        if (user.getPassword() != null) {
            user.setPassword(null);
        }

        userCache.putUser(token, user);
    }
}
