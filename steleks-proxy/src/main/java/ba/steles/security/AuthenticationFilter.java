package ba.steles.security;/**
 * Created by ensar on 28/05/17.
 */

import ba.steleks.util.ProxyHeaders;
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
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.*;
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
                ExtraHeadersRequest extraHeadersRequest = new ExtraHeadersRequest((HttpServletRequest) request);
                extraHeadersRequest.addExtraHeader(ProxyHeaders.USER_ID, usersResponse.getUserId().toString());
                extraHeadersRequest.addExtraHeader(ProxyHeaders.USER_ROLES, userRoleSet);
                request = extraHeadersRequest;
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

    private class ExtraHeadersRequest extends HttpServletRequestWrapper implements HttpServletRequest {

        private Map<String, Set<String>> extraHeaders = new HashMap<>();

        public ExtraHeadersRequest(HttpServletRequest request) {
            super(request);
        }

        public void addExtraHeader(String name, String value) {
            if (!extraHeaders.containsKey(name)) {
                extraHeaders.put(name, new HashSet<>());
            }
            extraHeaders.get(name).add(value);
        }

        public void addExtraHeader(String name, Collection<String> values) {
            if (!extraHeaders.containsKey(name)) {
                extraHeaders.put(name, new HashSet<>());
            }
            extraHeaders.get(name).addAll(values);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> allHeaders = new ArrayList<>(Collections.list(super.getHeaders(name)));
            if (extraHeaders.containsKey(name)) {
                allHeaders.addAll(extraHeaders.get(name));
            }
            return Collections.enumeration(allHeaders);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> allHeaders = new ArrayList<>(Collections.list(super.getHeaderNames()));
            allHeaders.addAll(extraHeaders.keySet());
            return Collections.enumeration(allHeaders);
        }

        @Override
        public String getHeader(String name) {
            String header = super.getHeader(name);
            if (header == null) {
                Set<String> headers = extraHeaders.get(name);
                if (headers == null || headers.isEmpty()) {
                    return null;
                } else {
                    return Collections.enumeration(headers).nextElement();
                }
            } else {
                return header;
            }
        }
    }
}