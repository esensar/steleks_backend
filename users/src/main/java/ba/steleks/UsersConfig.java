package ba.steleks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by admin on 13/05/2017.
 */
@Configuration
public class UsersConfig {
    @Bean
    public static PasswordEncoder providePasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
