package com.ll.sbb3;

import com.ll.sbb3.domain.question.answer.entity.Answer;
import com.ll.sbb3.domain.question.answer.repository.AnswerRepository;
import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.question.question.repository.QuestionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class Sbb3ApplicationTests {
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Test
	@DisplayName("question insert")
	void testJpa1() {
		Question q1 = Question
				.builder()
				.subject("sbb가 무엇인가요?")
				.content("sbb에 대해서 알고 싶습니다.")
				.createDate(LocalDateTime.now())
				.build();

		this.questionRepository.save(q1);

		Question q2 = Question
				.builder()
				.subject("스프링부트 모델 질문입니다.")
				.content("id는 자동으로 생성되나요?")
				.createDate(LocalDateTime.now())
				.build();

		this.questionRepository.save(q2);
	}

	@Test
	@DisplayName("question findAll")
	void testJpa2() {
		List<Question> qList = this.questionRepository.findAll();

		assertEquals(2, qList.size());
		assertEquals("sbb가 무엇인가요?", qList.get(0).getSubject());
	}

	@Test
	@DisplayName("question findById")
	void testJpa3() {
		Optional<Question> oq = this.questionRepository.findById(1);

		if(oq.isPresent()) {
			assertEquals("sbb가 무엇인가요?", oq.get().getSubject());
		}
	}

	@Test
	@DisplayName("question findBySubject")
	void testJpa4() {
		Question q = this.questionRepository.findBySubject("sbb가 무엇인가요?");

		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("question findBySubjectAndContent")
	void testJpa5() {
		Question q = this.questionRepository.findBySubjectAndContent("sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");

		assertEquals(1, q.getId());
	}

	@Test
	@DisplayName("question findBySubjectLike")
	void testJpa6() {
		List<Question> qList = this.questionRepository.findBySubjectLike("sbb%");

		assertEquals("sbb가 무엇인가요?", qList.get(0).getSubject());
	}

	@Test
	@DisplayName("question modify")
	void testJpa7() {
		Optional<Question> oq = this.questionRepository.findById(1);

		assertTrue(oq.isPresent());

		Question question = oq.get();

		question.setSubject("제목 수정함!");

		this.questionRepository.save(question);
	}

	@Test
	@DisplayName("question delete")
	void testJpa8() {
		assertEquals(2, this.questionRepository.count());

		Optional<Question> oq = this.questionRepository.findById(1);

		assertTrue(oq.isPresent());

		Question question = oq.get();

		this.questionRepository.delete(question);

		assertEquals(1, this.questionRepository.count());
	}

	@Test
	@DisplayName("answer insert")
	void testJpa9() {
		Optional<Question> oq = this.questionRepository.findById(2);

		assertTrue(oq.isPresent());

		Question question = oq.get();

		Answer a = Answer
				.builder()
				.content("ㅇㅋ")
				.createDate(LocalDateTime.now())
				.question(question)
				.build();

		this.answerRepository.save(a);
	}

	@Test
	@DisplayName("answer findById")
	void testJpa10() {
		Optional<Answer> oa = this.answerRepository.findById(1);

		assertTrue(oa.isPresent());

		assertEquals(2, oa.get().getQuestion().getId());
	}

	@Test
	@DisplayName("find answer from question")
	@Transactional
	void testJpa11() {
		Optional<Question> oq = this.questionRepository.findById(2);

		assertTrue(oq.isPresent());

		assertEquals("ㅇㅋ", oq.get().getAnswerList().get(0).getContent());
	}
}
