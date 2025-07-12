package com.whs.dev2.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final long EXPIRATION = 3_600_000; // 1h
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final PrivateKey privateKey;   // 서버 비밀키
    private final PublicKey  publicKey;    // 서버 공개키

    /**
     * 생성자: RSA 키 로드, 예외 발생 시 로깅만 수행하여 빈 등록이 실패하지 않도록 처리
     */
    public JwtUtil() {
        PrivateKey tempPriv = null;
        PublicKey  tempPub  = null;
        try {
            String privPem = loadPem("/keys/rsa_private.pem", "PRIVATE KEY");
            String pubPem  = loadPem("/keys/rsa_public.pem",  "PUBLIC KEY");

            tempPriv = loadPrivateKey(privPem);
            tempPub  = loadPublicKey(pubPem);
        } catch (Exception e) {
            log.error("[JWT] RSA key load failed, JWT 기능 제한: {}", e.getMessage(), e);
        }
        this.privateKey = tempPriv;
        this.publicKey  = tempPub;
    }

    private String loadPem(String path, String header) throws Exception {
        try (InputStream in = getClass().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("PEM file not found: " + path);
            }
            String pem = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            return pem.replace("-----BEGIN " + header + "-----", "")
                    .replace("-----END "   + header + "-----", "")
                    .replaceAll("\\s", "");
        }
    }

    private PrivateKey loadPrivateKey(String base64Pem) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Pem);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String base64Pem) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(base64Pem);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    /**
     * 토큰 발급 (RS256)
     */
    public String generateToken(String username) {
        if (privateKey == null) {
            throw new IllegalStateException("JWT signing key not initialized");
        }
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("alg", "RS256")
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .claim("role", "user")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    /**
     * 토큰 검증 (RSA + JWK injection 경로 지원)
     */
    public String validateAndExtractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            Object jwkObj = header.get("jwk");
                            if (jwkObj != null) {
                                try {
                                    byte[] der = Base64.getUrlDecoder().decode(jwkObj.toString());
                                    X509EncodedKeySpec spec = new X509EncodedKeySpec(der);
                                    return KeyFactory.getInstance("RSA").generatePublic(spec);
                                } catch (Exception e) {
                                    throw new JwtException("invalid jwk public key");
                                }
                            }
                            if (publicKey == null) {
                                throw new JwtException("JWT verification key not initialized");
                            }
                            return publicKey;
                        }
                    })
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (JwtException ex) {
            log.warn("[JWT] validation failed → {} : {}",
                    ex.getClass().getSimpleName(), ex.getMessage());
            return null;
        }
    }
}
