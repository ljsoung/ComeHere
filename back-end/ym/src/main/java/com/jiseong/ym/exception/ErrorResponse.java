package com.jiseong.ym.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 에러 발생 시 클라이언트에 전달하는 응답 형식
 * 모든 에러 응답을 동일한 구조로 통일하여 프론트엔드에서 처리하기 쉽게 한다.
 *
 * 응답 예시:
 * {
 *   "status": 404,
 *   "message": "사용자를 찾을 수 없습니다."
 * }
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;      // HTTP 상태 코드 (숫자)
    private String message;  // 에러 메시지
}