package ba.steleks.service;

/**
 * Created by ensar on 16/04/17.
 */

import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Logger;

public class ServiceConstants {
    private static final Logger logger =
            Logger.getLogger(ServiceConstants.class.getName());

    static {
    }

    @Value("${users.name}")
    public static String USERS_SERVICE_NAME;
    @Value("${events.name}")
    public static String EVENTS_SERVICE_NAME;
    @Value("${teams.name}")
    public static String TEAMS_SERVICE_NAME;
}
