package br.alura.ForumHub.domain.valueobject;

import java.util.List;

import br.alura.ForumHub.domain.entity.Answer;
import br.alura.ForumHub.domain.entity.Topic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TopicWithAnswers {
  private Topic topic;
  private List<Answer> answers;
}
