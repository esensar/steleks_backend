package ba.steleks.security;/**
 * Created by ensar on 30/05/17.
 */

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class CustomUrlUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomUrlUsernamePasswordAuthenticationFilter() {
        super();
        setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/accesstoken", "POST")
        );
    }
}
