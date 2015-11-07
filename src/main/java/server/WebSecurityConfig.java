package server;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
                .permitAll();
        
        
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
      web.ignoring().antMatchers("/common/javascripts/**", "/app/javascripts/**", "/public/javascripts/**");
    }
/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
  */  

}