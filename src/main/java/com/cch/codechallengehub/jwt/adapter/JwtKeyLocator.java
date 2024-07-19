package com.cch.codechallengehub.jwt.adapter;

import com.cch.codechallengehub.constants.JwtKey;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JwtKeyLocator extends LocatorAdapter<Key>  {
    @Override
    public Key locate(JwsHeader header) {
        String keyId = header.getKeyId();
        return getKey(keyId);
    }

    public static Key getKey(String keyId) {
        JwtKey key = JwtKey.getByKeyId(keyId);
        if(key == null) {
            return null;
        }
        byte[] keyBytes = Decoders.BASE64.decode(key.getValue());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}