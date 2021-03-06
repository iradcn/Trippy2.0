package config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.jdbcAuthentication().dataSource(dataSource)
			.usersByUsernameQuery("select user_id, password,enabled from users where user_id=?")
			.authoritiesByUsernameQuery("select user_id, role from user_roles where user_id=?");

	}	
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
            .authorizeRequests()
                .antMatchers("/public/**","/common/**","/login/**").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/").usernameParameter("user_id").passwordParameter("password")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/app/index.html")
                .permitAll()
                .and()
            .logout()
                .permitAll()
                .and()
                .headers().frameOptions().disable(); 
        
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/common/javascripts/**", "/app/javascripts/**", "/public/javascripts/**");
    }

}