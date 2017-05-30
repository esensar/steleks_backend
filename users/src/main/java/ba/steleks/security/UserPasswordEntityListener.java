package ba.steleks.security;

import ba.steleks.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by ensar on 30/05/17.
 */

public class UserPasswordEntityListener {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PrePersist
    @PreUpdate
    public void onUserUpdate(User user) {
        if(user.getPassword() != null) {
            user.setPasswordHash(passwordEncoder.encode(user.getPassword()));
        }
    }

}
