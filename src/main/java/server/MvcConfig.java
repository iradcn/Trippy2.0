package server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import services.ConnectionConfig;

@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    driverManagerDataSource.setUrl(ConnectionConfig.getConnectionURL());
	    driverManagerDataSource.setUsername(ConnectionConfig.getUserName());
	    driverManagerDataSource.setPassword(ConnectionConfig.getPassword());
	    return driverManagerDataSource;
	}
	

}
