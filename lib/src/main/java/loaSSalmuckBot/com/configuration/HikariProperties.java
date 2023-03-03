package loaSSalmuckBot.com.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariProperties {
	
	private int maximumPoolSize;    
    private int minimumIdle;    
    private long idleTimeout;    
    private long maxLifetime;    
    private long connectionTimeout;    
    private long validationTimeout;

}
