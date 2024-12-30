package com.ll.sbb3.domain.question.answer.controller;

import com.ll.sbb3.domain.question.answer.form.AnswerForm;
import com.ll.sbb3.domain.question.answer.service.AnswerService;
import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.question.question.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;

    @PostMapping("/create/{questionId}")
    public String create(
            Model model,
            @PathVariable Integer questionId,
            @Valid AnswerForm answerForm,
            BindingResult bindingResult
            ) {
        Question question = this.questionService.findById(questionId);

        if(bindingResult.hasErrors()) {
            model.addAttribute("question", question);

            return "/domain/question/question/question_detail";
        }

        this.answerService.create(question, answerForm.getContent());

        return "redirect:/question/detail/%d".formatted(questionId);
    }
}
