package com.investment.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAuthorizationFilter jwtRequestFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Spring Security has this protection enabled (“migrateSession“) – on authentication a new HTTP Session is created, the old one is invalidated and the attributes from the old session are copied over.
        //http.sessionManagement().sessionFixation().migrateSession();
        //http.sessionManagement().maximumSessions(1) // maximum one session; destroy previous if re-authenticated

        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/user/login", "/user/register").permitAll() //Allow anyone
                //.antMatchers("/img/**").permitAll() // any other allow anyone (including unauthenticated users)
                //.antMatchers("/admin/**").hasAnyRole("ADMIN") // must have role "admin"
                //.antMatchers("/user/**").hasAnyRole("USER") // must have role "user"
                .anyRequest().authenticated() // all other requests only accessible by authenticated users
            .and()
                .cors()
            .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // make sure we use stateless session; session won't be used to store user's state.
        ;

        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}