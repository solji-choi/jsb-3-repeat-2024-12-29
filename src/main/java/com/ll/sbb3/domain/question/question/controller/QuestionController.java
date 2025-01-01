package com.ll.sbb3.domain.question.question.controller;

import com.ll.sbb3.domain.question.answer.form.AnswerForm;
import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.question.question.service.QuestionService;
import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.domain.user.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    public String getItems(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String kw
    ) {
        Page<Question> paging = this.questionService.findAll(page, kw);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);

        return "/domain/question/question/question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            Model model,
            @PathVariable Integer id,
            AnswerForm answerForm
    ) {
        Question question = this.questionService.findById(id);

        model.addAttribute("question", question);

        return "/domain/question/question/question_detail";
    }

    record QuestionForm(
            @NotEmpty(message = "제목을 입력해주세요.")
            @Size(max = 200)
            String subject,

            @NotEmpty(message = "내용을 입력해주세요.")
            String content
    ) {}

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(QuestionForm questionForm) {
        return "/domain/question/question/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            Principal principal
    ) {
        if(bindingResult.hasErrors()) {
            return "/domain/question/question/question_form";
        }

        SiteUser author = this.userService.findByUsername(principal.getName());

        this.questionService.create(questionForm.subject, questionForm.content, author);

        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(
            Model model,
            @PathVariable Integer id,
            Principal principal
    ) {
        Question question = this.questionService.findById(id);

        if(!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        QuestionForm questionForm = new QuestionForm(
                question.getSubject(),
                question.getContent()
        );

        model.addAttribute("questionForm", questionForm);

        return "/domain/question/question/question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(
            @Valid QuestionForm questionForm,
            BindingResult bindingResult,
            @PathVariable Integer id,
            Principal principal
    ) {
        if(bindingResult.hasErrors()) return "/domain/question/question/question_form";

        Question question = this.questionService.findById(id);

        if(!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");

        this.questionService.modify(question, questionForm.subject, questionForm.content);

        return "redirect:/question/detail/%d".formatted(id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            Principal principal
    ) {
        Question question = this.questionService.findById(id);

        if(!question.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");

        this.questionService.delete(question);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String vote(
            @PathVariable Integer id,
            Principal principal
    ) {
        Question question = this.questionService.findById(id);
        SiteUser voter = this.userService.findByUsername(principal.getName());

        this.questionService.vote(question, voter);

        return "redirect:/question/detail/%d".formatted(id);
    }
}
