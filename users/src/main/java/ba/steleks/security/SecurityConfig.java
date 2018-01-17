package ba.steleks.security;

/**
 * Created by ensar on 16/05/17.
 */

import ba.steleks.repository.UsersJpaRepository;
import ba.steleks.security.token.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan("org.baeldung.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    private final TokenStore tokenStore;

    private final UsersJpaRepository usersJpaRepository;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, TokenStore tokenStore, UsersJpaRepository usersJpaRepository) {
        this.userDetailsService = userDetailsService;
        this.tokenStore = tokenStore;
        this.usersJpaRepository = usersJpaRepository;
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests()
                .antMatchers("/accesstoken", "/accesstoken/**", "/").permitAll()
                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.GET, "/users/**/userRoles").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/users/**/userRoles").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/**/userRoles").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**/userRoles/*").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new AuthenticationFilter(tokenStore, usersJpaRepository), CustomUrlUsernamePasswordAuthenticationFilter.class);
    }

}