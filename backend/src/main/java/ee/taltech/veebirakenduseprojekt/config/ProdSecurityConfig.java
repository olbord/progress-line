package ee.taltech.veebirakenduseprojekt.config;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class ProdSecurityConfig {

    @Value("${azure.tenant-id}")
    private String tenantId;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/public/**", "/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return jwt -> {
            String oid = jwt.getClaimAsString("oid");
            if (oid == null || oid.isBlank()) {
                throw new IllegalArgumentException("JWT must contain 'oid' claim");
            }

            String email = extractEmail(jwt);
            AzureUser principal = new AzureUser(oid, email);

            return new UsernamePasswordAuthenticationToken(principal, jwt, Collections.emptyList());
        };
    }

    /**
     * Extracts email from JWT claims with multiple fallbacks.
     * Priority: upn > unique_name
     */
    private String extractEmail(Jwt jwt) {
        String upn = jwt.getClaimAsString("upn");
        if (isValidEmail(upn)) {
            return upn;
        }

        String uniqueName = jwt.getClaimAsString("unique_name");
        if (isValidEmail(uniqueName)) {
            return uniqueName;
        }

        return null;
    }

    private boolean isValidEmail(String value) {
        return value != null && !value.isBlank() && value.contains("@");
    }

    /**
     * Configures JWT decoder with Azure AD's JWKS endpoint for token signature verification.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = String.format(
                "https://login.microsoftonline.com/%s/discovery/v2.0/keys",
                tenantId
        );
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    /**
     * Custom principal representing an Azure AD authenticated user.
     * Accessible in controllers via @AuthenticationPrincipal AzureUser.
     */
    @Getter
    @AllArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @ToString
    public static final class AzureUser {
        @EqualsAndHashCode.Include
        private final String oid;
        private final String email;
    }
}
