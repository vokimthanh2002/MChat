package org.example.mchatbackend.security;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class SecurityConstants {
    public static final long JWT_EXPIRATION = 2000000;
    public static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
