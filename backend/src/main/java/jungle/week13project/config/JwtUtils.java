package jungle.week13project.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    private long expirationMs;

    private String SECRET_KEY;

    public JwtUtils() {
        Dotenv dotenv = Dotenv.load();
        this.expirationMs = Long.parseLong(dotenv.get("JWT_EXPIRATION_MS"));
        this.SECRET_KEY = dotenv.get("JWT_SECRET");
    }

    private Key getSignKey() {
        byte[] ketBytes = Decoders.BASE64.decode(SECRET_KEY);
        return new SecretKeySpec(ketBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    public String generateToken(String userName, String userId ,String userEmail) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userName", userName)
                .claim("userEmail", userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUserEmail(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.error("JWT 토큰이 null이거나 비어 있습니다.");
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            return claims.get("userEmail", String.class); // 이메일 추출
        } catch (ExpiredJwtException e) {
            log.error("JWT 토큰이 만료되었습니다: {}", e.getMessage());
            throw new RuntimeException("Token has expired", e);
        } catch (JwtException e) {
            log.error("유효하지 않은 JWT 토큰: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String extractUserName(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            return claims.get("userName", String.class);
        } catch (Exception e) {
            log.error("토큰에서 사용자 이름 추출 에러: {}", e.getMessage());
            return null;
        }
    }

    public String extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
        return claims.getSubject();  // userId가 subject에 들어가 있음
    }

    public boolean validateToken(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            log.error("토큰이 null이거나 Bearer 접두사가 없습니다.");
            throw new IllegalArgumentException("유효하지 않은 인증 정보입니다.");
        }

        try {
            String jwt = token.replace("Bearer ", "");
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(jwt);
            return true; // 유효한 토큰
        } catch (MalformedJwtException ex) {
            log.error("유효하지 않은 JWT 토큰: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("만료된 JWT 토큰: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("지원되지 않는 JWT 토큰: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT 클레임 문자열이 비어 있음: {}", ex.getMessage());
        } catch (Exception ex) {
            log.error("JWT 처리 중 알 수 없는 오류: {}", ex.getMessage());
        }
        return false; // 유효하지 않은 토큰
    }
}
