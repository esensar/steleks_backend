package ba.steleks.security;

import ba.steleks.model.User;
import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.security.token.TokenStore;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by ensar on 28/05/17.
 */

public class TokenAuthenticationService {


    static final String HEADER_STRING   = "Authorization";

    public static Authentication getAuthentication(HttpServletRequest request,
                                                   TokenStore tokenStore,
                                                   UsersJpaRepository usersJpaRepository) {
        String token = request.getHeader(HEADER_STRING);

        if (token != null && tokenStore.isValidToken(token)) {
            Long userId = tokenStore.getTokenInfo(token);

            User user = usersJpaRepository.findOne(userId);
            if(user != null) {
                return new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                        UserRoleFactory.toGrantedAuthorities(user.getUserRoles()));
            } else {
                return null;
            }
        }
        return null;
    }
}