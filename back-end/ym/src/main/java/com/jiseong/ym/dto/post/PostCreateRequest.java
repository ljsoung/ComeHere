package com.jiseong.ym.dto.post;

import com.jiseong.ym.entity.enums.MeetingType;
import com.jiseong.ym.entity.enums.PostCategory;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 모집글 작성 요청 DTO
 * stacks는 선택 항목이므로 null 대신 빈 리스트로 초기화한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "모집 유형을 선택해주세요.")
    private PostCategory category; // CONTEST, GRADUATION_PROJECT, TEAM_PROJECT, STUDY, HACKATHON, ETC

    @Min(value = 1, message = "모집 인원은 1명 이상이어야 합니다.")
    private int recruitmentCount;

    @NotBlank(message = "활동 기간을 입력해주세요.")
    private String period; // 예: "3개월", "방학 중"

    @NotNull(message = "마감일을 입력해주세요.")
    @FutureOrPresent(message = "마감일은 오늘 이후여야 합니다.")
    private LocalDate deadline;

    @NotNull(message = "진행 방식을 선택해주세요.")
    private MeetingType meetingType; // ONLINE, OFFLINE, HYBRID

    private List<String> stacks = new ArrayList<>(); // 기술 스택 (선택)
}