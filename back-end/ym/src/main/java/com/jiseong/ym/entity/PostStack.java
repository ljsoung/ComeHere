package com.jiseong.ym.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_stacks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class PostStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 스택 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private RecruitmentPost post; // 게시글

    @Column(nullable = false, length = 100)
    private String stackName; // 기술스택 이름
}