package com.ll.sbb3;

import com.ll.sbb3.domain.question.question.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Sbb3ApplicationTests {
	@Autowired
	private QuestionService questionService;

	@Test
	void contextLoads() {
		for(int i = 1; i <= 300; i++) {
			String subject = "테스트데이터입니다:[%03d]".formatted(i);
			String content ="냉무";

			this.questionService.create(subject, content, null);
		}
	}

}
