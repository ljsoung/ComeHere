package com.jiseong.ym.exception;

import lombok.Getter;

/**
 * 서비스 비즈니스 로직에서 발생하는 커스텀 예외
 * ErrorCode를 함께 전달하여 GlobalExceptionHandler에서 일관된 에러 응답을 생성할 수 있게 한다.
 *
 * 사용 예시:
 *   throw new CustomException(ErrorCode.USER_NOT_FOUND);
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}