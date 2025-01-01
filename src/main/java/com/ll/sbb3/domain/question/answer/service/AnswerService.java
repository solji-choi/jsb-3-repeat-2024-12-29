package com.ll.sbb3.domain.question.answer.service;

import com.ll.sbb3.domain.question.answer.entity.Answer;
import com.ll.sbb3.domain.question.answer.repository.AnswerRepository;
import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.global.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Answer create(Question question, String content, SiteUser author) {
        Answer answer = Answer
                .builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .question(question)
                .author(author)
                .build();

        return this.answerRepository.save(answer);
    }

    public Answer findById(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);

        if(answer.isPresent()) return answer.get();
        else throw new DataNotFoundException("answer not found");
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());

        this.answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        this.answerRepository.delete(answer);
    }

    public void vote(Answer answer, SiteUser voter) {
        answer.getVoter().add(voter);

        this.answerRepository.save(answer);
    }
}
