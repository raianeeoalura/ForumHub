package br.alura.ForumHub.domain.valueobject;

import java.text.Normalizer;

public class Slug {
  private final String value;

  public Slug(String value) {
    this.value = value;
  }

  public static Slug createFromText(String text) {
    if (text == null || text.trim().isBlank()) {
      throw new IllegalArgumentException("Text cannot be null or blank");
    }

    String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
    String value = normalized.replaceAll("[^\\p{ASCII}]", "") // Remove non-ASCII characters
        .toLowerCase()
        .trim()
        .replaceAll("\\s+", "-") // Replace spaces with hyphens
        .replaceAll("[^a-z0-9\\s-]", "-") // Replace special characters
        .replaceAll("-+", "-") // Replace multiple hyphens with a single hyphen
        .replaceAll("^-|-$", ""); // Remove leading/trailing hyphens

    return new Slug(value);
  }

  public String getValue() {
    return value;
  }

  public static Slug createFromTextWithSuffix(String text, String suffix) {
    if (suffix == null || suffix.trim().isBlank()) {
      throw new IllegalArgumentException("Suffix cannot be null or blank");
    }

    String newValue = text + "-" + suffix;
    return Slug.createFromText(newValue);
  }
}
