package xyz.melodyl.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import xyz.melodyl.pojo.ResponseBuilder;
import xyz.melodyl.pojo.User;
import xyz.melodyl.service.UserService;
import xyz.melodyl.annotation.JsonController;

import java.util.HashMap;
import java.util.Map;

import static xyz.melodyl.service.UserService.TOKEN_ATTRIBUTE_NAME;


@RestController
@JsonController("/user")
public class UserController {
    @Autowired
    @Qualifier("UserService")
    UserService userService;

    @PostMapping(value = "/authorize")
    public ResponseEntity checkUserValidity(@RequestBody User user) {
        String token = userService.getTokenIfUserExist(user);
        if (token != null) {

            return ResponseBuilder.newBuilder(HttpStatus.NO_CONTENT)
                    .header(TOKEN_ATTRIBUTE_NAME, token)
                    .build();
        } else {
            return ResponseBuilder.unAuthorized();
        }
    }

    @GetMapping(value = "/name")
    public ResponseEntity getUserName(@RequestHeader(value = TOKEN_ATTRIBUTE_NAME) String token) {
        String name = userService.getUserNameFromToken(token);
        if (name != null) {
            Map<String, String> temp = new HashMap<>(1);
            temp.put("name", name);
            return ResponseBuilder.ok(temp);
        } else {
            return ResponseBuilder.unAuthorized();
        }
    }
}
