package ba.steleks.security;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by ensar on 30/05/17.
 */


public final class SessionIdentifierGenerator {
    private SecureRandom random = new SecureRandom();

    public String nextSessionId() {
        return new BigInteger(130, random).toString(32);
    }
}