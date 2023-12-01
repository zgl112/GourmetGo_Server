package org.gg.config;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.gg.repository.UserRepository;
import org.gg.security.AuthorisationFilter;
import org.gg.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@CrossOrigin
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private UserRepository userRepository;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .csrf().disable()
          .authorizeRequests()
          .antMatchers("/authentication/login", "/authentication/register").permitAll()
          .anyRequest().authenticated()
          .and()
          .formLogin() // Enable form-based authentication
          .and()
          .httpBasic(); // Enable basic authentication

        http.addFilterAfter(new JwtAuthenticationFilter("/**", authenticationManager(), userRepository), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new AuthorisationFilter(), JwtAuthenticationFilter.class);
    }

    // Create a PasswordEncoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Use DelegatingPasswordEncoder to support both BCrypt and your custom hashing algorithm
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());

        // Update the idForEncode to match the key used in the map
        return new DelegatingPasswordEncoder("bcrypt", encoders);
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}