package ba.steleks.security;

import ba.steleks.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by ensar on 28/05/17.
 */

class TokenAuthenticationService {

    static final long EXPIRATION_TIME   = 864_000_000; // 10 days
    static final String SECRET          = "ASteleksSecret";
    static final String TOKEN_PREFIX    = "Bearer";
    static final String HEADER_STRING   = "Authorization";
    static final String ROLES           = "roles";

    static void addAuthenticationHeader(HttpServletResponse res, String username, Set<UserRole> userRoleSet) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .claim(ROLES, UserRoleFactory.toString(userRoleSet))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
    }

    static Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null) {
            // parse the token.
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token.replaceFirst(TOKEN_PREFIX, ""))
                    .getBody();

            String userName = claims.getSubject();
            Set<UserRole> userRoles = UserRoleFactory.fromString(claims.get(ROLES, String.class));

            if (userName != null) {
                boolean access = false;

                UserRole theUserRole = new UserRole("theUser");

                if(userRoles.contains(theUserRole)) {
                    access = true;
                }

                if (access) {
                    return new UsernamePasswordAuthenticationToken(userName,
                            null,
                            UserRoleFactory.toGrantedAuthorities(userRoles));
                }
                else {
                    return new UsernamePasswordAuthenticationToken(userName,
                            null);
                }
            }
        }
        return null;
    }
}