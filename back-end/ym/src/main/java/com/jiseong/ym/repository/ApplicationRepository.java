package com.jiseong.ym.repository;

import com.jiseong.ym.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByPostIdAndApplicantId(Long postId, Long applicantId);

    Optional<Application> findByPostIdAndApplicantId(Long postId, Long applicantId);

    List<Application> findByPostId(Long postId);

    Page<Application> findByApplicantId(Long applicantId, Pageable pageable);
}