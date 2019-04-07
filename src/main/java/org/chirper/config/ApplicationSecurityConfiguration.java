package org.chirper.config;

import org.chirper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Autowired
    public ApplicationSecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
                .csrf().disable()
//                .csrfTokenRepository(csrfTokenRepository())
                .authorizeRequests()
                    .antMatchers("/", "/login", "/register").anonymous()
                    .antMatchers("/css/**", "/js/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/feed")
                .and()
                    .rememberMe()
                    .rememberMeParameter("rememberMe")
                    .key("PLYOK")
                    .userDetailsService(this.userService)
                    .rememberMeCookieName("KLYOK")
                    .tokenValiditySeconds(1200);
    }
}