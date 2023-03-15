package loaSSalmuckBot.com.Main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
//@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan("loaSSalmuckBot.com")
@ComponentScan("loaSSalmuckBot.com")
public class LoaSSalmuckBotApplication {
	
	
	public static void main(String[] args) throws Exception {

		if (System.getProperty("spring.profiles.active") == null)
			System.setProperty("spring.profiles.active", "local");
		String profile = "local";

		new SpringApplicationBuilder(LoaSSalmuckBotApplication.class)
				.properties("spring.config.location=classpath:/properties/" + profile + "/api-" + profile + ".yml"
						+ ", classpath:/properties/" + profile + "/db-" + profile + ".yml"
						+ ", classpath:/application.yml")
				.run(args);
		

	}
	
//	@PersistenceContext(unitName = "cmsEntityManager") 
	
	
	
//	private EntityManager cmsEntityManger;
//	


}
