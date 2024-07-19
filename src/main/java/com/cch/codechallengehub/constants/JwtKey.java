package com.cch.codechallengehub.constants;

import com.cch.codechallengehub.component.JwtKeyValue;
import lombok.Getter;

@Getter
public enum JwtKey {
    SECRET("secret", JwtKeyValue.secretKey);

    private final String keyId;
    private final String value;

    JwtKey(String keyId, String value) {
        this.keyId = keyId;
        this.value = value;
    }

    public static JwtKey getByKeyId(String keyId) {
        for (JwtKey jwtKey : JwtKey.values()) {
            if (jwtKey.getKeyId().equals(keyId)) {
                return jwtKey;
            }
        }
        return null;
    }
}
