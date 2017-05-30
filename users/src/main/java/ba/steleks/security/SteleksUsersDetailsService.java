package ba.steleks.security;/**
 * Created by ensar on 16/05/17.
 */

import ba.steleks.model.User;
import ba.steleks.repository.UsersJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class SteleksUsersDetailsService implements UserDetailsService {

    @Autowired
    private UsersJpaRepository usersJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersJpaRepository.findByUsername(username);

        if(user == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities =
                UserRoleFactory.toGrantedAuthorities(user.getUserRoles());


        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPasswordHash(),
                authorities);
    }
}
