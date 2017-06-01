package ba.steles.service;/**
 * Created by ensar on 16/04/17.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Primary
@Component
@RefreshScope
public class DefaultServiceIdProvider implements ServiceIdProvider {
    private static final Logger logger =
            Logger.getLogger(DefaultServiceIdProvider.class.getName());

    public static final String NO_SERVICE = "NO_SERVICE";

    @Value("${users.name}")
    private String usersName;
    @Value("${events.name}")
    private String eventsName;
    @Value("${teams.name}")
    private String teamsName;

    @Override
    public String getServiceId(Service service) {
        switch (service) {
            case USERS:
                return usersName;
            case TEAMS:
                return teamsName;
            case EVENTS:
                return eventsName;
            default:
                return NO_SERVICE;
        }
    }
}
