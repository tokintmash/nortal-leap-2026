package com.nortal.library.api.config;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Value("${library.security.enforce:true}")
  private boolean enforceSecurity;

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

    if (enforceSecurity) {
      http.authorizeHttpRequests(
              auth ->
                  auth.requestMatchers("/api/health")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/api/**")
                      .permitAll()
                      .anyRequest()
                      .authenticated())
          .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    } else {
      http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    }
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(
        List.of("http://localhost:4200", "http://localhost:8080", "*"));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(false);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  JwtDecoder jwtDecoder() throws Exception {
    RSAPublicKey publicKey = loadPublicKey(PUBLIC_KEY_PEM);
    return NimbusJwtDecoder.withPublicKey(publicKey).build();
  }

  private RSAPublicKey loadPublicKey(String pem) throws Exception {
    String normalized =
        pem.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
    byte[] decoded = Base64.getDecoder().decode(normalized);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
    return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
  }

  private static final String PUBLIC_KEY_PEM =
      """
            -----BEGIN PUBLIC KEY-----
            MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAvcnu34w+EqyoQCaZV31j
            jZfI9o1QhggGEkyi6bdhchRsWt8qJ1D7SKxDy8Iq+xA5ZP1/uXntGYiVkIY4nGeP
            ka2RMAxNhAyxz5VyjusgdCKkAjg5PDO/6pFWVuNIdFwWJKXhcOnII+Zz/C2/STZW
            etmBjUMhMs9NqtUVOiYo+PPIoBHCCTFuWQSVbwaDP1QTB/TJM6/I6eX9mQWLSSAQ
            U7TWgCbx7a7T3PPRjsB2du8cS0nNaTzFjA89MxOEN6u8v9WatIFxRkKRC88cDzTO
            NZMpuIiqhsEzEE+P1XnPn3km1kFmwBV02eEpbOXGmWPfZcWTDoWA2dvTVDyajkSF
            uNd+OGTDAjOcaA1dt+K7YAAgCV0BU/1smsqcwcf8zERhmRiC6nMezD9AQPtzQDGV
            oxy6bGgvBBi+z8JgOBo2HTkXgKZcWCbaNLZ8KGZeysrmVqeumeQNCdvmDaqacgQW
            uq/XjDipraX0459gUfokWSkamHqufKW+2c7uzhtJ8Rtl9Gi5MfusDmgAe7wC1yU7
            AKUfJQOcfoeI79PlgDue845FYStIygrGSAcLYJbLERVELhxXKBp0EjwpuIo3tHEr
            mUnNLF2/tNb1vyHq8xcw632XkEx+BWfuxAhYnxPt6Ry1+ft1sscO+Zzu+YIK48DT
            rAcTck17OXADUxkcFjx6HYECAwEAAQ==
            -----END PUBLIC KEY-----
            """;
}
