package com.nortal.library.api.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class DevAuthConfig {
  private static final Logger log = LoggerFactory.getLogger(DevAuthConfig.class);

  @Value("${library.security.print-demo-token:true}")
  private boolean printDemoToken;

  @Bean
  CommandLineRunner demoTokenPrinter() {
    return args -> {
      if (!printDemoToken) {
        return;
      }
      try {
        RSAPublicKey publicKey = toPublicKey(PUBLIC_KEY_PEM);
        RSAPrivateKey privateKey = toPrivateKey(PRIVATE_KEY_PEM);
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
        Instant now = Instant.now();
        JwtClaimsSet claims =
            JwtClaimsSet.builder()
                .subject("m1")
                .issuer("dev-local")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .build();
        String token = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        log.info("Demo JWT (Bearer): {}", token);
      } catch (Exception e) {
        log.warn("Could not generate demo token: {}", e.getMessage());
      }
    };
  }

  private RSAPublicKey toPublicKey(String pem) throws Exception {
    String normalized =
        pem.replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
    byte[] decoded = Base64.getDecoder().decode(normalized);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
    return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(keySpec);
  }

  private RSAPrivateKey toPrivateKey(String pem) throws Exception {
    String normalized =
        pem.replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replaceAll("\\s", "");
    byte[] decoded = Base64.getDecoder().decode(normalized);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
    return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(keySpec);
  }

  // Fresh generated keys

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

  private static final String PRIVATE_KEY_PEM =
      """
              -----BEGIN PRIVATE KEY-----
              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
              Hwz99f7U9rL0lJ2mdIue45UPwz69Whw=
              -----END PRIVATE KEY-----
              """;
}
