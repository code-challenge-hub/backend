package com.cch.codechallengehub.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtKeyValue {

    public static String secretKey;

    @Value(value = "${jwt.secret-key}")
    public void setSecretKey(String secretKey) {
       this.secretKey = secretKey;
    }
}
