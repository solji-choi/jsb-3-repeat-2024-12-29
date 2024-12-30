package com.ll.sbb3.domain.question.answer.repository;

import com.ll.sbb3.domain.question.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
}
