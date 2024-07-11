package com.sakthi.auth.service.jwt;


import com.sakthi.auth.customException.BannedUserException;
import com.sakthi.auth.customException.UserNotActivatedException;
import com.sakthi.auth.model.token.TokenInfo;
import com.sakthi.auth.repository.TokenValidationRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.token.jwtSecret}")
    private String jwtSecret;
    @Autowired
    TokenValidationRepository tokenValidationRepository;

    public String generateJwtToken(Authentication authentication, long expiryTime) throws UserNotActivatedException, BannedUserException {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        if (!userPrincipal.getAccountSettings().isActive()) {
            throw new UserNotActivatedException("User Is Not Activated Yet");
        } else if (userPrincipal.getAccountSettings().isBanned()) {
            throw new BannedUserException("User Is Banned");
        }

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
//                .claim("authorities", "test") add additional data to jwt token
                .setExpiration(new Date((new Date()).getTime() + expiryTime))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            TokenInfo tokenInfo=tokenValidationRepository.findByToken(authToken);
            if(tokenInfo==null){
                throw new MalformedJwtException("Token Not Found");
            } else if (!tokenInfo.isActive()) {
                throw new MalformedJwtException("Token Is Deactivated");
            }
            logger.debug("token is"+Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken).toString());
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
