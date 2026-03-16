package com.jiseong.ym.entity;

import com.jiseong.ym.entity.common.BaseEntity;
import com.jiseong.ym.entity.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "applicant_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 지원 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant; // 지원자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private RecruitmentPost post; // 게시글

    @Column(columnDefinition = "TEXT")
    private String message; // 지원 메세지

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING; // 지원하면 Pending 상태

    // 상태 변경
    public void changeStatus(ApplicationStatus status) {
        this.status = status;
    }
}