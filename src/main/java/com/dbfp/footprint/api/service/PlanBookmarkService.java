package com.dbfp.footprint.api.service;

import com.dbfp.footprint.api.repository.MemberRepository;
import com.dbfp.footprint.api.repository.PlanBookmarkRepository;
import com.dbfp.footprint.api.repository.PlanRepository;
import com.dbfp.footprint.api.response.CreatePlanBookmarkResponse;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.PlanBookmark;
import com.dbfp.footprint.dto.PlanBookmarkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanBookmarkService {

    private final PlanBookmarkRepository planBookmarkRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    @Transactional
    public PlanBookmarkDto addBookmark(Long memberId, Long planId) {
        log.info("Attempting to add a bookmark - Member ID: {}, Plan ID: {}", memberId, planId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("Member not found - ID: {}", memberId);
                    return new IllegalArgumentException("존재하지 않는 회원입니다.");
                });
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Plan not found - ID: {}", planId);
                    return new IllegalArgumentException("존재하지 않는 일정입니다.");
                });

        Optional<PlanBookmark> existingBookmark = planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId);
        if (existingBookmark.isPresent()) {
            log.warn("Bookmark already exists - Member ID: {}, Plan ID: {}", memberId, planId);
            throw new IllegalStateException("즐겨찾기가 이미 존재합니다..");
        }

        PlanBookmark savedBookmark =  planBookmarkRepository.save(PlanBookmark.of(member, plan));
        log.info("Bookmark successfully added - Bookmark ID: {}", savedBookmark.getId());
        return PlanBookmarkDto.from(savedBookmark);
    }

    @Transactional
    public void removeBookmark(Long memberId, Long planId) {
        log.info("Attempting to remove a bookmark - Member ID: {}, Plan ID: {}", memberId, planId);
        if (!planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId).isPresent()) {
            log.error("Bookmark not found - Member ID: {}, Plan ID: {}", memberId, planId);
            throw new IllegalArgumentException("존재하지 않는 즐겨찾기 입니다.");
        }
        planBookmarkRepository.deleteByMemberIdAndPlanId(memberId, planId);
        log.info("Bookmark successfully removed - Member ID: {}, Plan ID: {}", memberId, planId);
    }

    public List<CreatePlanBookmarkResponse> findBookmarksByMemberId(Long memberId) {
        log.info("Fetching bookmarks for member - ID: {}", memberId);
        List<PlanBookmark> bookmarks = planBookmarkRepository.findAllByMemberId(memberId);
        List<CreatePlanBookmarkResponse> responses = bookmarks.stream()
                .map(CreatePlanBookmarkResponse::from)
                .collect(Collectors.toList());
        log.info("Found {} bookmarks for member - ID: {}", responses.size(), memberId);
        return responses;
    }
}
