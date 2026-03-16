package com.jiseong.ym.dto.post;

import com.jiseong.ym.entity.enums.PostStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 모집 상태 변경 요청 DTO
 * 작성자가 모집글을 OPEN ↔ CLOSED 로 전환할 때 사용한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class PostStatusUpdateRequest {

    @NotNull(message = "모집 상태를 선택해주세요.")
    private PostStatus status; // OPEN or CLOSED
}