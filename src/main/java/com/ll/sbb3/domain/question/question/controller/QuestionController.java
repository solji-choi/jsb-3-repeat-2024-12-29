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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(defaultValue = "0") int page
    ) {

        Page<Question> paging = this.questionService.findAll(page);

        model.addAttribute("paging", paging);

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
}
