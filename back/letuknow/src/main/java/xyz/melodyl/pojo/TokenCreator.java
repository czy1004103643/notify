package xyz.melodyl.pojo;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public final class TokenCreator {

    public String createToken() {
        return UUID.randomUUID().toString();
    }
}
