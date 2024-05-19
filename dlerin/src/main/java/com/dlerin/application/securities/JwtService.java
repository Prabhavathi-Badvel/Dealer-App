package com.dlerin.application.securities;

import com.dlerin.application.entity.AdminLogin;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.function.Function;





@Service
public class JwtService {

//    @Value("${application.security.jwt.secret-key}")
//    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    private final SecretKey secretKey;

    public JwtService(@Value("${application.security.jwt.secret-key}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String username, String userType) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userType", userType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserType(String token) {
        return extractClaim(token, claims -> claims.get("userType", String.class));
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }





























////    @Value("${application.security.jwt.secret-key}")
////    private String secretKey;
//    @Value("${application.security.jwt.expiration}")
//    private long jwtExpiration;
//    @Value("${application.security.jwt.refresh-token.expiration}")
//    private long refreshExpiration;
//
//    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//    public String generateToken(String username, String userType) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("userType", userType);
//
//        String role;
//        if ("dealer".equalsIgnoreCase(userType)) {
//            role = "ROLE_DEALER";
//        } else if ("admin".equalsIgnoreCase(userType)) {
//            role = "ROLE_ADMIN";
//        } else {
//            throw new IllegalArgumentException("Invalid user type: " + userType);
//        }
//        claims.put("role", role);
//
//        return Jwts.builder()
//                .setSubject(username)
//                .setClaims(claims)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
//                .signWith(SignatureAlgorithm.HS256, secretKey)
//                .compact();
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//        return claimsResolver.apply(claims);
//    }
//
////    private Claims extractAllClaims(String token) {
////        return Jwts.parserBuilder()
////                .setSigningKey(getSigningKey())
////                .build()
////                .parseClaimsJws(token)
////                .getBody();
////    }
//
//    public boolean isTokenExpired(String token) {
//        final Date expiration = getExpirationDateFromToken(token);
//        return expiration.before(new Date());
//    }
//
//    public Date getExpirationDateFromToken(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }
//
////    private Key getSigningKey() {
////        return Keys.hmacShaKeyFor(secretKey.getEncoded());
////    }
//
//    public String getUserTypeForUser(String username) {
//        // Implement logic to retrieve user type based on username
//        // Placeholder implementation
//        if ("admin".equalsIgnoreCase(username)) {
//            return "admin";
//        } else {
//            return "dealer";
//        }
//    }
//


}


