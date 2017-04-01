package ba.steleks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class EventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsApplication.class, args);
	}
}

@RefreshScope
@RestController
class MessageRestController {

	@Value("${message:Hello default}")
	private String message;

	@Value("${user.password}")
	private String password;

	@RequestMapping("/message")
	String getMessage() {
		return this.message;
	}

	//@RequestMapping(path ="/temp", value = "/{id}", method = RequestMethod.GET)
	//String getTemp(@PathVariable("id") Long id){
	//	return "temp " + Long.toString(id);
	//}

	@RequestMapping(value = "/whoami/{user}", method = RequestMethod.GET)
	public String whoami(@PathVariable("username") String username) {
		return String.format("Hello! You're %s and you'll become a(n) root, " +
		"but only if your password is '%s'!\n",
				username, password);
	}
}

