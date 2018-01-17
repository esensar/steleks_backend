package ba.steleks.security;

import ba.steleks.model.User;
import ba.steleks.model.UserRole;
import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.security.token.TokenStore;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by ensar on 28/05/17.
 */

public class TokenAuthenticationService {


    static final String HEADER_STRING   = "Authorization";

    public static Authentication getAuthentication(HttpServletRequest request,
                                                   TokenStore tokenStore,
                                                   UsersJpaRepository usersJpaRepository) {
        String token = request.getHeader(HEADER_STRING);
        System.out.println("token: " + token);
        System.out.println("headers: " + request.getHeaderNames());

        if (token != null && tokenStore.isValidToken(token)) {
            Long userId = tokenStore.getTokenInfo(token);

            User user = usersJpaRepository.findOne(userId);
            if(user != null) {
                System.out.println("Found token... userId: " + userId);
                List<GrantedAuthority> userRole=UserRoleFactory.toGrantedAuthorities(user.getUserRoles());
                return new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                        UserRoleFactory.toGrantedAuthorities(user.getUserRoles()));
            } else {
                System.out.println("Cant find token");
                return null;
            }
        }
        System.out.println("Cant find token");
        return null;
    }
}