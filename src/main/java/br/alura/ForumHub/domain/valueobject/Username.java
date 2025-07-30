package br.alura.ForumHub.domain.valueobject;

import java.util.regex.Pattern;

public class Username {
  private static final int MIN_LENGTH = 3;
  private static final int MAX_LENGTH = 20;
  private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z0-9_]+$");
  private static final Pattern RESERVED_WORDS = Pattern
      .compile("^(admin|user|guest|root|moderator|system|null|undefined)$", Pattern.CASE_INSENSITIVE);

  private final String value;

  public Username(String value) {
    if (value == null) {
      throw new IllegalArgumentException("Username cannot be null or blank");
    }

    String trimmed = value.toLowerCase().trim();

    if (trimmed.length() < MIN_LENGTH || trimmed.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("Username must be between 3 and 20 characters long");
    }

    if (!VALID_PATTERN.matcher(trimmed).matches()) {
      throw new IllegalArgumentException("Username can only contain alphanumeric characters and underscores");
    }

    if (RESERVED_WORDS.matcher(trimmed).matches()) {
      throw new IllegalArgumentException("Username cannot be a reserved word");
    }

    this.value = trimmed;
  }

  public String getValue() {
    return value;
  }
}
