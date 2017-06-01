package ba.steles.security;/**
 * Created by ensar on 28/05/17.
 */

import ba.steles.service.Service;
import ba.steles.service.discovery.ServiceDiscoveryClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class AuthenticationFilter extends GenericFilterBean {

    private RestTemplate restTemplate;

    private ServiceDiscoveryClient discoveryClient;

    public AuthenticationFilter(RestTemplate restTemplate, ServiceDiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        Authentication authentication;

        String token = ((HttpServletRequest)request).getHeader("Authorization");

        if(token != null) {
            try {
                System.out.println("token: " + token);
                String usersServiceBase = discoveryClient.getServiceUrl(Service.USERS);
                AuthResponse usersResponse = restTemplate.getForObject(usersServiceBase + "/accesstoken/{token}", AuthResponse.class, token);
                System.out.println("the response= " + usersResponse);
                Set<String> userRoleSet = usersResponse
                        .getUserRoles();
                if (userRoleSet == null) {
                    userRoleSet = new HashSet<>();
                    userRoleSet.add("NO_ROLES");
                }
                Set<SimpleGrantedAuthority> roleSet =
                        userRoleSet
                                .stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet());
                authentication = new UsernamePasswordAuthenticationToken("a name", null,
                        roleSet);
            } catch (Exception ex) {
                ex.printStackTrace();
                authentication = null;
            }
        } else {
            authentication = null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}