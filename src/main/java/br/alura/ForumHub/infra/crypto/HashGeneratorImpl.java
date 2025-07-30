package br.alura.ForumHub.infra.crypto;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.alura.ForumHub.application.cryptography.HashGenerator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HashGeneratorImpl implements HashGenerator {
  private final PasswordEncoder encoder;

  @Override
  public String hash(String plainText) {
    return encoder.encode(plainText);
  }
}
