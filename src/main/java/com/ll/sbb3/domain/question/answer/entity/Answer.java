package com.ll.sbb3.domain.question.answer.entity;

import com.ll.sbb3.domain.question.question.entity.Question;
import com.ll.sbb3.domain.user.user.entity.SiteUser;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<SiteUser> voters;
}
