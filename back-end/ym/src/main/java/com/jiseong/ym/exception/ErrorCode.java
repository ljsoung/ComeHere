package com.jiseong.ym.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 서비스 전체에서 사용하는 에러 코드 목록
 * CustomException을 던질 때 이 Enum을 사용하면, GlobalExceptionHandler가 자동으로 적절한 HTTP 상태와 메시지를 응답한다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ── 회원 ──────────────────────────────────────────────────
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    NICKNAME_DUPLICATED(HttpStatus.CONFLICT, "이미 사용 중인 닉네임입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다."),

    // ── 모집글 ────────────────────────────────────────────────
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "모집글을 찾을 수 없습니다."),
    POST_NOT_AUTHOR(HttpStatus.FORBIDDEN, "모집글 작성자만 수행할 수 있습니다."),
    POST_CLOSED(HttpStatus.BAD_REQUEST, "마감된 모집글입니다."),
    INVALID_DEADLINE(HttpStatus.BAD_REQUEST, "마감일은 오늘 이후여야 합니다."),

    // ── 지원 ──────────────────────────────────────────────────
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "지원 내역을 찾을 수 없습니다."),
    ALREADY_APPLIED(HttpStatus.CONFLICT, "이미 지원한 모집글입니다."),
    CANNOT_APPLY_OWN_POST(HttpStatus.BAD_REQUEST, "본인이 작성한 모집글에는 지원할 수 없습니다."),
    APPLICATION_NOT_APPLICANT(HttpStatus.FORBIDDEN, "본인의 지원만 취소할 수 있습니다."),
    APPLICATION_NOT_AUTHOR(HttpStatus.FORBIDDEN, "모집글 작성자만 지원을 처리할 수 있습니다."),

    // ── 북마크 ────────────────────────────────────────────────
    ALREADY_BOOKMARKED(HttpStatus.CONFLICT, "이미 북마크한 모집글입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "북마크를 찾을 수 없습니다."),

    // ── 댓글 ──────────────────────────────────────────────────
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_NOT_AUTHOR(HttpStatus.FORBIDDEN, "댓글 작성자만 수행할 수 있습니다.");

    private final HttpStatus status;  // HTTP 응답 상태 코드
    private final String message;     // 클라이언트에 전달할 에러 메시지
}