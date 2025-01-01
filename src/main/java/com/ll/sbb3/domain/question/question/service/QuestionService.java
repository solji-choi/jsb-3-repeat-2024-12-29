package com.ll.sbb3.domain.question.question.service;

import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.question.question.repository.QuestionRepository;
import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.global.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> findAll() {
        return this.questionRepository.findAll();
    }

    public Page<Question> findAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        return this.questionRepository.findAll(pageable);
    }

    public Question findById(Integer id) {
        Optional<Question> opQuestion = this.questionRepository.findById(id);

        if(opQuestion.isPresent()) {
            return opQuestion.get();
        } else {
            throw new DataNotFoundException("존재하지 않는 id입니다.");
        }
    }

    public void create(String subject, String content, SiteUser author) {
        Question question = Question
                .builder()
                .subject(subject)
                .content(content)
                .createDate(LocalDateTime.now())
                .author(author)
                .build();

        this.questionRepository.save(question);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());

        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        this.questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser voter) {
        question.getVoters().add(voter);

        this.questionRepository.save(question);
    }
}
