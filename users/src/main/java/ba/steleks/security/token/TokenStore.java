package ba.steleks.security.token;

/**
 * Created by ensar on 30/05/17.
 */
public interface TokenStore {

    boolean isValidToken(Long id, String token);

    void saveToken(Long id, String token);

    void removeToken(Long id, String token);

}
