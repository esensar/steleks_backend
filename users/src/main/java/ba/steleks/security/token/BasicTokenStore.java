package ba.steleks.security.token;

import ba.steleks.model.UserRole;
import ba.steleks.storage.store.KeyValueStore;
import ba.steleks.util.CalendarUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by ensar on 30/05/17.
 */

@Component
public class BasicTokenStore implements TokenStore {

    // Default one hour ttl
    public static final long DEFAULT_TTL =
            TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);

    private KeyValueStore<String, TokenInfo> tokenStore;
    private long ttl = DEFAULT_TTL;

    @Autowired
    public BasicTokenStore(KeyValueStore<String, TokenInfo> tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean isValidToken(String token) {
        if (tokenStore.contains(token)) {
            // Find token in store
            TokenInfo basicToken = tokenStore.get(token);

            // Token is invalid, it has expired
            if(basicToken.saveTime + ttl < CalendarUtils.getUTCCalendar().getTimeInMillis()) {
                return false;
            }

            // Token valid!
            return true;
        } else {
            // No token in store
            return false;
        }
    }

    @Override
    public Long getTokenInfo(String token) {
        if (isValidToken(token)) {
            // Find token in store
            TokenInfo basicToken = tokenStore.get(token);

            return basicToken.userId;
        } else {
            // No token in store
            return null;
        }
    }

    @Override
    public void saveToken(Long id, String token) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.userId = id;
        tokenInfo.saveTime = CalendarUtils.getUTCCalendar().getTimeInMillis();
        tokenStore.save(token, tokenInfo);
    }

    @Override
    public void removeToken(String token) {
        tokenStore.remove(token);
    }

    private static class TokenInfo {
        Long userId;
        Long saveTime;
    }
}
