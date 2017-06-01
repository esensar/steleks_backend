package ba.steles.security;/**
 * Created by ensar on 01/06/17.
 */

import java.util.Set;

public class AuthResponse {

    private Long userId;
    private Set<String> userRoles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<String> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", userRoles=" + userRoles +
                '}';
    }
}
