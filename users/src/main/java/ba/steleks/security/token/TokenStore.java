package ba.steleks.security.token;

/**
 * Created by ensar on 30/05/17.
 */
public interface TokenStore {

    /**
     * @param token token
     * @return user id associated to the {@param token} or null if it does not exist
     */
    Long getTokenInfo(String token);

    boolean isValidToken(String token);

    void saveToken(Long id, String token);

    void removeToken(String token);

}
