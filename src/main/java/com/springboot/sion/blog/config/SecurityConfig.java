package com.springboot.sion.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This method automatically will use UserDetailsService to get credentials from the database and
     * will use the passwordEncoder() to encode and decode the password.
     * This will do the database authentication
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //Enabled http basic security configuration

        httpSecurity.csrf()
                .disable()
                .authorizeHttpRequests(authorize ->
                        //authorize.anyRequest().authenticated()
                        authorize.requestMatchers(HttpMethod.GET, "/api/**")
                                .permitAll() //ALL USERS ARE ALLOWED TO ACCESS GET ENDPOINTS
                                .anyRequest()
                                .authenticated())
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails loren = User.builder()
//                .username("loren")
//                .password(passwordEncoder().encode("loren"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(loren, admin);
//    }
}
