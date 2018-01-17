package ba.steleks;

import ba.steleks.security.SteleksUsersDetailsService;
import ba.steleks.security.token.HashTokenEncoder;
import ba.steleks.security.token.TokenEncoder;
import ba.steleks.security.AutowireHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by admin on 13/05/2017.
 */
@Configuration
public class UsersConfig {
    @Bean
    public static PasswordEncoder providePasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public static TokenEncoder provideTokenEncoder() {
        return new HashTokenEncoder();
    }

    @Bean
    public static AutowireHelper autowireHelper() {
        return AutowireHelper.getInstance();
    }

    @Bean
    public UserDetailsService provideUserDetailsService() {
        return new SteleksUsersDetailsService();
    }
}
