package ru.job4j.urlshortcut.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ru.job4j.urlshortcut.security.JWTAuthenticationFilter;
import ru.job4j.urlshortcut.security.JWTAuthorizationFilter;
import ru.job4j.urlshortcut.service.SiteService;

import static ru.job4j.urlshortcut.security.JWTAuthenticationFilter.SIGN_UP_URL;

/**
 * @author Svistunov Mikhail
 * @version 1.0
 */
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private SiteService siteDetailsService;
    private BCryptPasswordEncoder encoder;

    public WebSecurity(SiteService siteDetailsService, BCryptPasswordEncoder encoder) {
        this.siteDetailsService = siteDetailsService;
        this.encoder = encoder;
    }

    /**
     * Настройка аутентификации
     *
     * @param auth AuthenticationManagerBuilder
     * @throws Exception исключение
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(siteDetailsService).passwordEncoder(encoder);
    }

    /**
     * Настройка авторизации и Spring Security
     *
     * @param http HttpSecurity
     * @throws Exception исключение
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.GET, "/redirect/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
