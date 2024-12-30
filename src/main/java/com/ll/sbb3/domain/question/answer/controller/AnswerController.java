package com.ll.sbb3.domain.question.answer.controller;

import com.ll.sbb3.domain.question.answer.form.AnswerForm;
import com.ll.sbb3.domain.question.answer.service.AnswerService;
import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.question.question.service.QuestionService;
import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.domain.user.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{questionId}")
    public String create(
            Model model,
            @PathVariable Integer questionId,
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            Principal principal
            ) {
        Question question = this.questionService.findById(questionId);
        SiteUser author = this.userService.findByUsername(principal.getName());

        if(bindingResult.hasErrors()) {
            model.addAttribute("question", question);

            return "/domain/question/question/question_detail";
        }

        this.answerService.create(question, answerForm.getContent(), author);

        return "redirect:/question/detail/%d".formatted(questionId);
    }
}
