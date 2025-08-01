package br.alura.ForumHub.domain.valueobject;

import java.util.List;

import br.alura.ForumHub.domain.entity.Answer;
import br.alura.ForumHub.domain.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopicWithAnswers {
  private final Topic topic;
  private final List<Answer> answers;
}
