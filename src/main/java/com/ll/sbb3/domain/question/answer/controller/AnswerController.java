package com.ll.sbb3.domain.question.answer.controller;

import com.ll.sbb3.domain.question.answer.entity.Answer;
import com.ll.sbb3.domain.question.answer.form.AnswerForm;
import com.ll.sbb3.domain.question.answer.service.AnswerService;
import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.question.question.service.QuestionService;
import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.domain.user.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(
            AnswerForm answerForm,
            @PathVariable Integer id,
            Principal principal
    ) {
        Answer answer = this.answerService.findById(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        answerForm.setContent(answer.getContent());

        return "/domain/question/answer/answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(
            @Valid AnswerForm answerForm,
            BindingResult bindingResult,
            @PathVariable Integer id,
            Principal principal
    ) {
        if(bindingResult.hasErrors()) return "/domain/question/answer/answer_form";

        Answer answer = this.answerService.findById(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        this.answerService.modify(answer, answerForm.getContent());

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            Principal principal
    ) {
        Answer answer = this.answerService.findById(id);

        if(!answer.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");

        this.answerService.delete(answer);

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String vote(
            @PathVariable Integer id,
            Principal principal
    ) {
        Answer answer = this.answerService.findById(id);
        SiteUser voter = this.userService.findByUsername(principal.getName());

        this.answerService.vote(answer, voter);

        return "redirect:/question/detail/%d".formatted(answer.getQuestion().getId());
    }
}
