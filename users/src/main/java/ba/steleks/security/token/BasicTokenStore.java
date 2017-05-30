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
public class BasicTokenStore implements TokenStore {

    // Default one hour ttl
    public static final long DEFAULT_TTL =
            TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS);

    private KeyValueStore<Long, BasicToken> tokenStore;
    private long ttl = DEFAULT_TTL;

    @Autowired
    public BasicTokenStore(KeyValueStore<Long, BasicToken> tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean isValidToken(Long id, String token) {
        if (tokenStore.contains(id)) {
            // Find token in store
            BasicToken basicToken = tokenStore.get(id);

            // Token is invalid, there is different token saved in store
            if (!basicToken.token.equals(token)) {
                return false;
            }

            // Token is invalid, it has expired
            if(basicToken.saveTime + ttl < CalendarUtils.getUTCCalendar().getTimeInMillis()) {
                return false;
            }

            // Token valid!
            return true;
        } else {
            // No id in store, there is no token
            return false;
        }
    }

    @Override
    public void saveToken(Long id, String token) {
        BasicToken basicToken = new BasicToken();
        basicToken.token = token;
        basicToken.saveTime = CalendarUtils.getUTCCalendar().getTimeInMillis();
        tokenStore.save(id, basicToken);
    }

    @Override
    public void removeToken(Long id, String token) {
        tokenStore.remove(id);
    }

    private static class BasicToken {
        String token;
        Long saveTime;
    }
}
