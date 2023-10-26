package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milliseconds}")
    private Long jwtExpirationDate;

    // Method to generate a JWT token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        //Build the JWT token
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key()) // Sign the token with a secret key
                .compact();// Compact the token into a string

        return token;
    }

    // Method to retrieve the signing key
    private Key key(){
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    // Method to get the username from a JWT token
    public String getUsername(String token){
        // Step 1: Parse the JWT token and verify its signature
        Claims claims = Jwts.parser() // Create a JWT parser
                .setSigningKey(key())// Set the signing key to verify the token's signature
                .build()
                .parseClaimsJws(token)// : takes the JWT token and parses it while also verifying its signature. It ensures that the token hasn't been altered since it was created.
                                        // The result of this line is a Jws<Claims> object. "Jws" stands for "JSON Web Signature," which is a format for securely transmitting information between parties.
                .getBody();// Get the claims (payload) from the token
        // Step 2: Retrieve the subject (usually the username) from the claims
        String username = claims.getSubject();
        return username;
    }

    // Method to validate a JWT token
    public boolean validateToken(String token) {
        try {
            // Step 1: Parse and validate the JWT token
            Jwts.parser()// Create a JWT parser
                    .setSigningKey(key())// Set the signing key to verify the token's signature
                    .build()
                    .parse(token); // Parse and validate the token
            // Step 2: If parsing and validation succeed, return true
            return true;
        }catch (MalformedJwtException ex){
            // Step 3: Handle exceptions for different JWT validation failures
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        }catch (ExpiredJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        }catch (UnsupportedJwtException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        }catch (IllegalArgumentException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");
        }

    }
}
