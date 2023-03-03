package loaSSalmuckBot.com.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Component
public class CreateHikariDataSource {
	
	
	@Autowired
	private HikariProperties hikariProperties;

    
    public DataSource createDataSource(String url ,String userName, String password, String name) {

    	final HikariConfig hikariConfig = new HikariConfig();
    	System.out.println(url);
    	hikariConfig.setJdbcUrl(url + "?characterEncoding=UTF-8&serverTimezone=UTC");
    	hikariConfig.setUsername(userName);
    	hikariConfig.setPassword(password);
    	hikariConfig.setPoolName("pool-"+name);
    	hikariConfig.setMaximumPoolSize(hikariProperties.getMaximumPoolSize());
    	hikariConfig.setMinimumIdle(hikariProperties.getMinimumIdle());
        hikariConfig.setMaxLifetime(hikariProperties.getMaxLifetime());
        if (hikariProperties.getMaximumPoolSize() > hikariProperties.getMinimumIdle()) {
            hikariConfig.setIdleTimeout(hikariProperties.getIdleTimeout());
        }
        hikariConfig.setValidationTimeout(hikariProperties.getValidationTimeout());
        hikariConfig.setConnectionTimeout(hikariProperties.getConnectionTimeout());    
    	return new HikariDataSource(hikariConfig);
    }

}
