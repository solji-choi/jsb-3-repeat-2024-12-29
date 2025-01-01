package com.ll.sbb3.domain.question.question.service;

import com.ll.sbb3.domain.question.answer.entity.Answer;
import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.question.question.repository.QuestionRepository;
import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.global.exceptions.DataNotFoundException;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);

                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);

                return cb.or(
                        cb.like(q.get("subject"), "%" + kw + "%"),
                        cb.like(q.get("content"), "%" + kw + "%"),
                        cb.like(u1.get("username"), "%" + kw + "%"),
                        cb.like(a.get("content"), "%" + kw + "%"),
                        cb.like(u2.get("username"), "%" + kw + "%")
                );
            }
        };
    }

    public List<Question> findAll() {
        return this.questionRepository.findAll();
    }

    public Page<Question> findAll(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);

        return this.questionRepository.findAll(spec, pageable);
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
        question.getVoter().add(voter);

        this.questionRepository.save(question);
    }
}
