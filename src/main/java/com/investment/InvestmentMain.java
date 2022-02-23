package com.investment;

import com.investment.security.JwtTokenUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

@SpringBootApplication
public class InvestmentMain implements WebApplicationInitializer {

    private final static Logger logger = LogManager.getLogger(InvestmentMain.class);

    public static void main(String[] args) {
        logger.info("InvestmentMain service starting...");
        SpringApplication.run(InvestmentMain.class, args);
        logger.info("InvestmentMain service has started running!!!");
    }

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.tokenDuration}")
    private int jwtTokenDuration;

    @Bean
    public JwtTokenUtil getJwtTokenUtil(){
        return new JwtTokenUtil(jwtSecret, jwtTokenDuration);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //registry.addMapping("/greeting-javaconfig").allowedOrigins("http://localhost:9000"); //enable specific endpoints for specific origins
                //registry.addMapping("/**"); // enable all origins
            }
        };
    }

    @Override
    public void onStartup(ServletContext sc) throws ServletException {
        // ...
        // true = cookie not accessible by browser script (default = true)
        sc.getSessionCookieConfig().setHttpOnly(true);
        // true = cookie only attached for HTTPS (default = false)
        sc.getSessionCookieConfig().setSecure(true);

        // session to be stored only in cookie, never URL
        sc.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    }

}