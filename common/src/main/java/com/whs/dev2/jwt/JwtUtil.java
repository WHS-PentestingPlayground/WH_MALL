package com.whs.dev2.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;   // ⭐ 로그용 import
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    /* ---------- 고정 HMAC 키 ---------- */
    private static final String RAW_KEY =
            "this_is_a_very_long_secret_key_for_hmac_sha512_that_is_at_least_64_bytes_long!!";
    private static final byte[]  KEY_BYTES = RAW_KEY.getBytes(StandardCharsets.UTF_8);
    private static final Key     HMAC_KEY  = Keys.hmacShaKeyFor(KEY_BYTES);
    private static final String  JWK_B64   =
            Base64.getUrlEncoder().withoutPadding().encodeToString(KEY_BYTES);
    private static final long    EXPIRATION = 3_600_000;   // 1h

    /* ---------- Logger ---------- */
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);  // ⭐

    /* ---------- 토큰 발급 ---------- */
    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("alg", "HS512")
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("jwk", JWK_B64)
                .setSubject(username)
                .claim("role", "user")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + EXPIRATION))
                .signWith(HMAC_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    /* ---------- 토큰 검증 ---------- */
    public String validateAndExtractUsername(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims) {
                            Object jwk = header.get("jwk");

                            /* ⭐ ① JWK 값/길이 로그 */
                            log.info("[JWT] header.jwk = {}, decodedLen = {}",
                                    jwk, jwk == null ? 0
                                            : Base64.getUrlDecoder().decode(jwk.toString()).length);

                            if (jwk == null)
                                throw new JwtException("missing jwk header");

                            byte[] decoded = Base64.getUrlDecoder().decode(jwk.toString());
                            return Keys.hmacShaKeyFor(decoded);
                        }
                    })
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

        } catch (Exception ex) {
            /* ⭐ ② 검증 실패 사유 로그 */
            log.warn("[JWT] validation failed → {} : {}", ex.getClass().getSimpleName(),
                    ex.getMessage());
            return null;   // → 컨트롤러/필터에서 401 처리
        }
    }
}
