package com.jiseong.ym.dto.application;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 지원하기 요청 DTO
 * 지원 메시지는 선택 항목이므로 null을 허용한다.
 * applicantId는 JWT에서, postId는 PathVariable에서 가져오므로 DTO에 포함하지 않는다.
 */
@Getter
@Setter
@NoArgsConstructor
public class ApplicationCreateRequest {

    private String message; // 지원 메시지 (선택)
}