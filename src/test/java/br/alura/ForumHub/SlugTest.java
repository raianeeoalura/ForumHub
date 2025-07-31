package br.alura.ForumHub;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.alura.ForumHub.domain.valueobject.Slug;

@SpringBootTest
public class SlugTest {

  @Test
  @DisplayName("Should create a slug successfully")
  void testCreateSlug() {
    var slug1 = Slug.createFromText("Hello World! This is a test.");
    assertThat(slug1).isNotNull();
    assertThat(slug1.getValue()).isEqualTo("hello-world-this-is-a-test");

    var slug2 = Slug.createFromText("Hello World! This is a test 2-.");
    assertThat(slug2).isNotNull();
    assertThat(slug2.getValue()).isEqualTo("hello-world-this-is-a-test-2");
  }

  @Test
  @DisplayName("Shlould create a slug with a suffix")
  void testCreateSlugWithSuffix() {
    String text = "Hello World! This is a test.";
    String suffix = "suffix-1-!";

    var slug = Slug.createFromTextWithSuffix(text, suffix);
    assertThat(slug.getValue()).isEqualTo("hello-world-this-is-a-test-suffix-1");
  }
}
