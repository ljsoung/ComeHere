package com.jiseong.ym.repository.spec;

import com.jiseong.ym.entity.PostStack;
import com.jiseong.ym.entity.RecruitmentPost;
import com.jiseong.ym.entity.enums.PostCategory;
import com.jiseong.ym.entity.enums.PostStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 모집글 동적 검색/필터 Specification
 * JpaSpecificationExecutor와 함께 사용하며, 조건이 null이면 해당 필터를 무시한다.
 * 여러 조건을 AND로 조합하여 다양한 검색 조합을 지원한다.
 */
public class RecruitmentPostSpec {

    /**
     * 검색 조건을 받아 Specification을 생성
     *
     * @param keyword   제목 키워드 (부분 일치)
     * @param category  모집 유형 필터
     * @param stackName 기술 스택 필터
     * @param status    모집 상태 필터 (OPEN / CLOSED)
     */
    public static Specification<RecruitmentPost> withCondition(
            String keyword, PostCategory category, String stackName, PostStatus status) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 제목 키워드 검색 (LIKE '%keyword%')
            if (StringUtils.hasText(keyword)) {
                predicates.add(cb.like(root.get("title"), "%" + keyword + "%"));
            }

            // 모집 유형 필터 (정확히 일치)
            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            // 기술 스택 필터 — post_stacks 테이블과 INNER JOIN
            if (StringUtils.hasText(stackName)) {
                Join<RecruitmentPost, PostStack> stackJoin = root.join("stacks", JoinType.INNER);
                predicates.add(cb.equal(stackJoin.get("stackName"), stackName));
                // JOIN으로 같은 게시글이 여러 번 조회될 수 있으므로 distinct 처리
                query.distinct(true);
            }

            // 모집 상태 필터 (OPEN / CLOSED)
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // 모든 조건을 AND로 결합
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}