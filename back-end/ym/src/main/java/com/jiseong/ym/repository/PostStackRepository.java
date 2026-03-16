package com.jiseong.ym.repository;

import com.jiseong.ym.entity.PostStack;
import com.jiseong.ym.entity.RecruitmentPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostStackRepository extends JpaRepository<PostStack, Long> {

    void deleteByPost(RecruitmentPost post);
}