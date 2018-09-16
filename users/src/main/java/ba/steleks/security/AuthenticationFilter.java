package ba.steleks.security;/**
 * Created by ensar on 28/05/17.
 */

import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.security.token.TokenStore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class AuthenticationFilter extends GenericFilterBean {

    private TokenStore tokenStore;
    private UsersJpaRepository usersJpaRepository;

    public AuthenticationFilter(TokenStore tokenStore, UsersJpaRepository usersJpaRepository) {
        this.tokenStore = tokenStore;
        this.usersJpaRepository = usersJpaRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("Received a request which requires auth!");

        Authentication authentication = TokenAuthenticationService.getAuthentication(
                (HttpServletRequest) request,
                tokenStore,
                usersJpaRepository
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}