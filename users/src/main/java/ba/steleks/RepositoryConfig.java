package ba.steleks;/**
 * Created by ensar on 13/06/17.
 */

import ba.steleks.model.Course;
import ba.steleks.model.MembershipType;
import ba.steleks.model.User;
import ba.steleks.model.UserRole;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Course.class);
		config.exposeIdsFor(MembershipType.class);
		config.exposeIdsFor(User.class);
		config.exposeIdsFor(UserRole.class);
	}
}
