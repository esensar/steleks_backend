package ba.steleks.security;

import ba.steleks.AutowireHelper;
import ba.steleks.model.User;
import ba.steleks.repository.UserRolesJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.HashSet;

/**
 * Created by ensar on 30/05/17.
 */

@Component
public class UserPasswordEntityListener {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRolesJpaRepository userRolesJpaRepository;

    @PrePersist
    @PreUpdate
    public void onUserUpdate(User user) {
        AutowireHelper.autowire(this, passwordEncoder);
        if(user.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        }
    }

    @PostPersist
    public void postUserCreated(User user) {
        AutowireHelper.autowire(this, userRolesJpaRepository);
        if (user.getUserRoles() == null) {
            user.setUserRoles(new HashSet<>());
        }
        user.getUserRoles().add(userRolesJpaRepository.findOne(1L));
    }

}
