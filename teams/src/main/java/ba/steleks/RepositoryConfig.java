package ba.steleks;/**
 * Created by ensar on 13/06/17.
 */

import ba.steleks.model.Participant;
import ba.steleks.model.Team;
import ba.steleks.model.TeamCategory;
import ba.steleks.model.TeamMedia;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Participant.class);
		config.exposeIdsFor(Team.class);
		config.exposeIdsFor(TeamCategory.class);
		config.exposeIdsFor(TeamMedia.class);
	}
}
