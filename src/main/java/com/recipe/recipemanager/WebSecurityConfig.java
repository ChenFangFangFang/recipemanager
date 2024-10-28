package com.recipe.recipemanager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.recipe.recipemanager.web.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

        private final UserDetailsServiceImpl userDetailsService;

        public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl) {
                this.userDetailsService = userDetailsServiceImpl;
        }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain configure(HttpSecurity http) throws Exception {
                http
                                .csrf().disable()
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/", "/home", "signup", "/saveuser",
                                                                "/api/users/register",
                                                                "/login",
                                                                "/error", "/favicon.ico")
                                                // can access without auth
                                                .permitAll()// all users
                                                .anyRequest().authenticated())// all other pages should auth
                                .formLogin(formlogin -> formlogin
                                                .loginPage("/login")
                                                .usernameParameter("email")
                                                .defaultSuccessUrl("/recipelist", true)
                                                // True: This means that no matter what page the user visited before,
                                                // after a successful login, they will be redirected to /recipelist
                                                // instead of the page they visited before.
                                                .permitAll())

                                .logout(logout -> logout.permitAll());

                return http.build();
        }

}
