package com.springboot.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter { //Extend OncePerRequestFilter: This class is a filter,
                                                // meaning it's used to process incoming HTTP requests.
                                                // The OncePerRequestFilter ensures that the filter logic runs only once for each request.
    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        /*
        Constructor: When a new JwtAuthenticationFilter is created, it needs two things: a JwtTokenProvider (for handling JWTs)
        and a CustomUserDetailsService (for fetching user details). These are provided via the constructor.
         */
    }


    /*
    This method gets executed for every incoming HTTP request.
    It tries to find a JWT in the request.
    If it finds a valid JWT, it extracts the username associated with it.
    It then loads user details (like roles and authorities) based on that username.
    Next, it creates an "authentication token" that includes the user's details.
    Finally, it tells the application that the user is authenticated and can proceed with the request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // get JWT token from http request
        String token = getTokenFromRequest(request);

        //validate token
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            // get username from token
            String username = jwtTokenProvider.getUsername(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //add additional authentication details from the HTTP request to the authenticationToken.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            //It puts the token into the SecurityContextHolder to mark the user as authenticated.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // it calls filterChain.doFilter to allow the request to proceed to the secured resource since user is now authenticated.
        filterChain.doFilter(request, response);

    }

    /*
    This method looks in the "Authorization" header of the incoming request to find a JWT.
If it finds one, it returns the JWT (excluding the "Bearer " prefix); otherwise, it returns nothing (null).
     */
    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}


/*
 The reason we need to create the authenticationToken even though we already have a JWT token is because of how Spring Security works:

Spring Security expects the authentication information to be in an Authentication object stored in the SecurityContextHolder.
The JWT token itself does not get automatically converted to an Authentication object.
The JWT contains user details like username, but not Spring specific info like authorities.
So in the filter, after we have validated the JWT and extracted the username, we need to load the full UserDetails for that user from our UserDetailsService.
We then use those UserDetails to construct an UsernamePasswordAuthenticationToken, which is the required Authentication object.
This token contains the important user details like username, password, authorities etc.
We put this authenticationToken into the SecurityContextHolder.
Now Spring Security can use the details in the Authentication object for authorization.

It no longer needs the JWT. The JWT was only used to identify the user.
 */
