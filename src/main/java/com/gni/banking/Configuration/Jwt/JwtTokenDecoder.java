package com.gni.banking.Configuration.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtTokenDecoder {

    @Autowired
    private JwtKeyProvider jwtKeyProvider;

    private String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Return token, excluding "Bearer " prefix
        }
        return null;  // Return null if there's no Authorization header or it's not a Bearer token
    }


    public long getIdInToken(HttpServletRequest request) throws Exception {
       //get token from header
        String token = getTokenFromHeader(request);

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(jwtKeyProvider.getPrivateKey())
                    .build()
                    .parseClaimsJws(token);

            // Extract user ID from 'id' claim
            Double userId = (Double) claimsJws.getBody().get("id");
        return userId.longValue();
    }



}
