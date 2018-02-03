package ba.steleks;/**
 * Created by ensar on 13/06/17.
 */

import ba.steleks.model.Event;
import ba.steleks.model.EventTeam;
import ba.steleks.model.EventType;
import ba.steleks.model.Media;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Event.class);
		config.exposeIdsFor(EventTeam.class);
		config.exposeIdsFor(EventType.class);
		config.exposeIdsFor(Media.class);
	}
}
