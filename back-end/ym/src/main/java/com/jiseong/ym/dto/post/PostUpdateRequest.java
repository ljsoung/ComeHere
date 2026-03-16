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
 * 모집글 수정 요청 DTO
 * PostCreateRequest와 필드 구성이 동일하지만, 별도 클래스로 분리하여
 * 추후 수정 전용 규칙(예: 특정 필드 수정 제한)을 추가할 때 유연하게 대응한다.
 */
@Getter
@Setter
@NoArgsConstructor
public class PostUpdateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "모집 유형을 선택해주세요.")
    private PostCategory category;

    @Min(value = 1, message = "모집 인원은 1명 이상이어야 합니다.")
    private int recruitmentCount;

    @NotBlank(message = "활동 기간을 입력해주세요.")
    private String period;

    @NotNull(message = "마감일을 입력해주세요.")
    @FutureOrPresent(message = "마감일은 오늘 이후여야 합니다.")
    private LocalDate deadline;

    @NotNull(message = "진행 방식을 선택해주세요.")
    private MeetingType meetingType;

    private List<String> stacks = new ArrayList<>();
}