package loaSSalmuckBot.com.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaRepositories(entityManagerFactoryRef = "cmsEntityManagerFactory",
			           transactionManagerRef   = "cmsTransactionManager",
			           basePackages            = "loaSSalmuckBot.com.api.jpa"
			           )
public class CmsDataSourceConfig {
	
	private static final String serviceName = "odd";
	
	private static final String basePackages = "loaSSalmuckBot.com.api.jpa";

    @Value("${services.username}")
    private String username;
    @Value("${services.passwd_1}")
    private String passwd_1;
    @Value("${services.passwd_2}")
    private String passwd_2;
    @Value("${services.url}")
    private String url;
    
	 @Value("${isServer}")
	 private Boolean isServer;
	
    @Autowired
    private JpaProperties jpaProperties;
    
    @Autowired
    CreateHikariDataSource createHikariDataSource; 

    @Primary
    @Bean(name = "cmsDataSource")
    @ConfigurationProperties(prefix = "service")
    public DataSource dataSource() {
        return createHikariDataSource.createDataSource(url, username, isServer?passwd_1:passwd_2, serviceName);
    }


    @Bean(name = "cmsEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean cmsEntityManagerFactory(@Qualifier("cmsDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPersistenceUnitName("cmsEntityManager");
        entityManagerFactoryBean.setPackagesToScan(basePackages);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.getJpaPropertyMap().put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties.getProperties());

        return entityManagerFactoryBean;
    }    

    @Bean(name = "cmsTransactionManager")
    @Primary
    public PlatformTransactionManager cmsTransactionManager(final @Qualifier("cmsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    
}
