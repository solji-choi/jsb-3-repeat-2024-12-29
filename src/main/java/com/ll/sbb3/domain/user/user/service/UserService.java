package com.ll.sbb3.domain.user.user.service;

import com.ll.sbb3.domain.user.user.entity.SiteUser;
import com.ll.sbb3.domain.user.user.repository.UserRepository;
import com.ll.sbb3.global.exceptions.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser signup(String username, String password, String email) {
        SiteUser siteUser = SiteUser
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();

        return this.userRepository.save(siteUser);
    }

    public SiteUser findByUsername(String username) {
        Optional<SiteUser> siteUser =  this.userRepository.findByUsername(username);

        if(siteUser.isPresent()) return siteUser.get();
        else throw new DataNotFoundException("siteuser not found");
    }
}
