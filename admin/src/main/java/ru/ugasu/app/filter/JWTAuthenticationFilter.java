package ru.ugasu.app.filter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ru.ugasu.app.model.dto.UserDTO;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; /* 10 days */
//    public static final String TOKEN_PREFIX = "Bearer ";
//    public static final String HEADER_STRING = "Authorization";


    private final AuthenticationManager auth;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JWTAuthenticationFilter(String defaultFilterProcessesUrl,
                                   AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl, authenticationManager);
        this.auth = authenticationManager;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            UserDTO creds = objectMapper.readValue(req.getInputStream(), UserDTO.class);
            return auth.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getLogin(),
                            creds.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {
        String token = JWT.create()
                .withSubject(((User) auth.getPrincipal()).getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(SECRET.getBytes()));
        //res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.setContentType("application/json");
        res.getWriter().println(objectMapper.writeValueAsString(Map.of("token", token)));
    }


}
