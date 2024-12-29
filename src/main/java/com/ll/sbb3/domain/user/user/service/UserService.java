package com.ll.sbb3.domain.user.user.service;

import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.domain.user.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String password, String email) {
        SiteUser siteUser = SiteUser
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .createDate(LocalDateTime.now())
                .build();

        return this.userRepository.save(siteUser);
    }
}
