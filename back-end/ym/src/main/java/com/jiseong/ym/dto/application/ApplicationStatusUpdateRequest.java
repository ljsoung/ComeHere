package com.jiseong.ym.dto.application;

import com.jiseong.ym.entity.enums.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 지원 승인/거절 요청 DTO
 * 모집글 작성자가 지원자를 APPROVED 또는 REJECTED 로 처리할 때 사용한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicationStatusUpdateRequest {

    @NotNull(message = "처리할 상태를 선택해주세요.")
    private ApplicationStatus status; // APPROVED 또는 REJECTED
}