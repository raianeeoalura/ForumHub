package br.alura.ForumHub.infra.crypto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.alura.ForumHub.application.cryptography.HashGenerator;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CryptographyConfiguration {
  private final PasswordEncoder passwordEncoder;

  @Bean
  public HashGenerator hashGenerator() {
    return new HashGeneratorImpl(passwordEncoder);
  }
}
