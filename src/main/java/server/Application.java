package server;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import config.ConnectionConfig;

@EnableWebMvcSecurity
@SpringBootApplication
@ComponentScan({"security","server","services","contentprovider","config"})
public class Application {

    public static void main(String[] args) {
        try {
            ConnectionConfig.init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SpringApplication.run(Application.class, args);    
    }
}
