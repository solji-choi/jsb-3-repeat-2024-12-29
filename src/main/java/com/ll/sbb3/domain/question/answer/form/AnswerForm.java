package com.ll.sbb3.domain.question.answer.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerForm {
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
}
