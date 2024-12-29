package com.ll.sbb3.domain.user.user.controller;

import com.ll.sbb3.domain.user.user.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    record UserCreateForm(
            @Size(min = 3, max = 25)
            @NotEmpty(message = "사용자ID는 필수항목입니다.")
            String username,

            @NotEmpty(message = "비밀번호는 필수항목입니다.")
            String password1,

            @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
            String password2,

            @NotEmpty(message = "이메일은 필수항목입니다.")
            @Email
            String email
    ) {}

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "domain/user/user/signup_form";
    }

    @PostMapping("/signup")
    public String signup(
            @Valid UserCreateForm userCreateForm,
            BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return "domain/user/user/signup_form";
        }

        if(!userCreateForm.password1.equals(userCreateForm.password2)) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");

            return "domain/user/user/signup_form";
        }

        try {
            userService.create(userCreateForm.username, userCreateForm.password1, userCreateForm.email);
        } catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");

            return "domain/user/user/signup_form";
        } catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());

            return "domain/user/user/signup_form";
        }

        return "redirect:/";
    }
}
