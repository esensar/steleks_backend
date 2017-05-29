package ba.steleks.security;/**
 * Created by ensar on 28/05/17.
 */

import ba.steleks.model.UserRole;
import com.google.common.collect.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class UserRoleFactory {

    public static String toString(Collection<UserRole> userRoleSet) {
        // Transform to list
        List<UserRole> userRoleList = Lists.newArrayList(userRoleSet);
        // Sort by role name - to make different sets look the same in the end
        userRoleList.sort(Comparator.comparing(UserRole::getRoleName));
        // Transform to string
        return userRoleList.stream().map(UserRole::getRoleName).collect(Collectors.joining(","));
    }

    public static Set<UserRole> fromString(String userRoleString) {
        Set<UserRole> userRoles = new HashSet<>();
        Arrays.stream(userRoleString.split(",")).map(UserRole::new).forEach(userRoles::add);
        return userRoles;
    }

    public static List<GrantedAuthority> toGrantedAuthorities(Collection<UserRole> userRoleSet) {
        return userRoleSet
                .stream()
                // get role name
                .map(UserRole::getRoleName)
                // create authority
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public static Set<UserRole> fromGrantedAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
        return grantedAuthorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::new)
                .collect(Collectors.toSet());
    }

}
