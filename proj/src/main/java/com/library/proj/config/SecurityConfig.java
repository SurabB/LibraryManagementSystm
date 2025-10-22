package com.library.proj.config;

import com.library.proj.repo.LibraryDaoImpl;
import com.library.proj.service.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    LibraryDaoImpl libraryDao;
    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
  http
          .csrf(csrf -> csrf.disable())
          .authorizeHttpRequests(request->
          request.requestMatchers("/registration/**").permitAll()
                  .anyRequest().authenticated())
          .httpBasic(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public UserDetailsService userDetails(){
        return userId->{
            User user=null;

            try {
                     user = libraryDao.findUserById(userId);

                }
                catch(UsernameNotFoundException e){

                }
            return org.springframework.security.core.userdetails.User
                    .withUsername(String.valueOf(user.getUserId()))
                    .password(user.getPassword())
                    .authorities(user.getRole()).build();

        };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
