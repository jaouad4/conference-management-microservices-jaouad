package ma.jaouad.gatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers("/eureka/**").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtDecoder(reactiveJwtDecoder())));
        
        return http.build();
    }
    
    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        // Use the external URL for fetching JWK keys (accessible from container)
        String jwkSetUri = "http://keycloak:8080/realms/conference-realm/protocol/openid-connect/certs";
        
        NimbusReactiveJwtDecoder jwtDecoder = NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
        
        // Create a custom validator that accepts multiple issuers
        List<String> validIssuers = Arrays.asList(
            "http://localhost:8090/realms/conference-realm",
            "http://keycloak:8080/realms/conference-realm"
        );
        
        OAuth2TokenValidator<Jwt> multiIssuerValidator = token -> {
            String issuer = token.getIssuer().toString();
            if (validIssuers.contains(issuer)) {
                return OAuth2TokenValidatorResult.success();
            }
            return OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Invalid issuer: " + issuer, null));
        };
        
        OAuth2TokenValidator<Jwt> timestampValidator = new JwtTimestampValidator();
        OAuth2TokenValidator<Jwt> validators = new DelegatingOAuth2TokenValidator<>(multiIssuerValidator, timestampValidator);
        
        jwtDecoder.setJwtValidator(validators);
        
        return jwtDecoder;
    }
}
