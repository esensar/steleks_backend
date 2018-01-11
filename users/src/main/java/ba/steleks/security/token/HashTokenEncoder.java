package ba.steleks.security.token;

import org.apache.commons.codec.digest.DigestUtils;

public class HashTokenEncoder implements TokenEncoder {

    @Override
    public String encodeToken(String token) {
        return DigestUtils.sha256Hex(token);
    }
}
