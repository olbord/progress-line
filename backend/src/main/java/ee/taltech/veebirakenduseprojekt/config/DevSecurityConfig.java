package ee.taltech.veebirakenduseprojekt.config;

import jakarta.servlet.Filter;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.FilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

/**
 * Use for development only.
 */
@Configuration
//@Profile("local")
@EnableWebSecurity
public class DevSecurityConfig {

    // this will be used as the user's email to check DB, whether
    // they are a teacher for queried courses and course-related info

    // this specific user is a teacher for the Python course (courseId = 35065)
    private static final String MOCK_TEACHER_EMAIL = "kadi.jairus@taltech.ee";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(fakeAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public Filter fakeAuthFilter() {
        return (ServletRequest request, ServletResponse response, FilterChain chain) -> {
            var auth = new UsernamePasswordAuthenticationToken(
                    new MockUser(MOCK_TEACHER_EMAIL),
                    null,
                    List.of() // optionally add roles here
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            chain.doFilter(request, response);
        };
    }

    public record MockUser(String email) {}
}
