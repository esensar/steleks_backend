package ba.steleks.security.token;

import ba.steleks.storage.store.KeyValueStore;
import ba.steleks.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by ensar on 30/05/17.
 */

@Component
public class BasicInMemoryTokenStore implements TokenStore {

    // Default one hour ttl
    public static final long DEFAULT_TTL =
            TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);

    private static KeyValueStore<String, TokenInfo> tokenStore;
    private TokenEncoder tokenEncoder;
    private long ttl = DEFAULT_TTL;

    @Autowired
    public BasicInMemoryTokenStore(KeyValueStore<String, TokenInfo> tokenStore, TokenEncoder tokenEncoder) {
        if(BasicInMemoryTokenStore.tokenStore == null) {
            BasicInMemoryTokenStore.tokenStore = tokenStore;
        }
        this.tokenEncoder = tokenEncoder;
    }

    @Override
    public boolean isValidToken(String token) {
        return validateToken(encodeToken(token));
    }

    @Override
    public Long getTokenInfo(String token) {
        token = encodeToken(token);
        System.out.println("token = " + token);
        if (validateToken(token)) {
            // Find token in store
            TokenInfo basicToken = tokenStore.get(token);

            return basicToken.userId;
        } else {
            // No token in store
            return null;
        }
    }

    @Override
    public void saveToken(Long id, String tokenKey) {
        tokenKey = encodeToken(tokenKey);
        System.out.println("id = [" + id + "], tokenKey = [" + tokenKey + "]");
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.userId = id;
        tokenInfo.saveTime = CalendarUtils.getUTCCalendar().getTimeInMillis();
        tokenStore.save(tokenKey, tokenInfo);
    }

    @Override
    public void removeToken(String token) {
        token = encodeToken(token);
        tokenStore.remove(token);
    }

    private boolean validateToken(String token) {
        if (tokenStore.contains(token)) {
            // Find token in store
            TokenInfo basicToken = tokenStore.get(token);

            // Token is invalid, it has expired
            if(basicToken.saveTime + ttl < CalendarUtils.getUTCCalendar().getTimeInMillis()) {
                tokenStore.remove(token);
                return false;
            }

            // Token valid!
            return true;
        } else {
            // No token in store
            return false;
        }
    }

    private String encodeToken(String token) {
        return tokenEncoder.encodeToken(token);
    }

    private static class TokenInfo {
        Long userId;
        Long saveTime;
    }
}
