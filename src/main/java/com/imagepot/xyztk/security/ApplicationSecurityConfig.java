package com.imagepot.xyztk.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfig jwtConfig;

    public ApplicationSecurityConfig(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig), JwtAuthenticationFilter.class)
                .authorizeRequests()
                    .antMatchers("/", "/login", "/signup", "/logout", "/css/**", "/dist/**", "/icomoon/**", "/images/**").permitAll()
                    .antMatchers("/admin").hasRole(ApplicationUserRole.ADMIN.name())
                    .anyRequest()
                    .authenticated();
//                .and()
//                .formLogin()
//                    .loginPage("/")
//                    .loginProcessingUrl("/home").permitAll()
//                        .usernameParameter("email")
//                        .passwordParameter("password")
//                    .defaultSuccessUrl("/home")
//                .failureUrl("/login?error").permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // TokenAuthenticationFilter will ignore the below paths
        web.ignoring().antMatchers(
                HttpMethod.GET,
                "/"
        );
    }
}
