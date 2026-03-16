package com.jiseong.ym.repository;

import com.jiseong.ym.entity.RecruitmentPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecruitmentPostRepository extends JpaRepository<RecruitmentPost, Long>,
        JpaSpecificationExecutor<RecruitmentPost> {

    Page<RecruitmentPost> findByAuthorId(Long authorId, Pageable pageable);
}