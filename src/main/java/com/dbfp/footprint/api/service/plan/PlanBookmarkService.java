package com.dbfp.footprint.api.service.plan;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.plan.PlanBookmarkRepository;
import com.dbfp.footprint.api.repository.plan.PlanRepository;
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
        Member member = findMemberById(memberId);
        Plan plan = findPlanById(planId);

        if (!plan.isVisible()) {
            log.info("Plan is not visible - Plan ID: {}", planId);
            throw new RuntimeException("공유가 허용되지 않은 일정입니다.");
        }

        Optional<PlanBookmark> existingBookmark = planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId);
        if (existingBookmark.isPresent()) {
            log.info("Bookmark already exists - Member ID: {}, Plan ID: {}", memberId, planId);
            throw new RuntimeException("즐겨찾기가 이미 존재합니다..");
        }

        PlanBookmark savedBookmark =  planBookmarkRepository.save(PlanBookmark.of(member, plan));
        log.info("Bookmark successfully added - Bookmark ID: {}", savedBookmark.getId());
        return PlanBookmarkDto.from(savedBookmark);
    }

    @Transactional
    public void removeBookmark(Long memberId, Long planId) {
        log.info("Attempting to remove a bookmark - Member ID: {}, Plan ID: {}", memberId, planId);
        PlanBookmark bookmark = planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId)
                .orElseThrow(() -> {
                    log.error("Bookmark not found - Member ID: {}, Plan ID: {}", memberId, planId);
                    return new RuntimeException("존재하지 않는 즐겨찾기입니다.");
                });
        planBookmarkRepository.delete(bookmark);
        log.info("Bookmark successfully removed - Member ID: {}, Plan ID: {}", memberId, planId);
    }

    public List<CreatePlanBookmarkResponse> findBookmarksByMemberId(Long memberId) {
        log.info("Fetching bookmarks for member - ID: {}", memberId);
        return planBookmarkRepository.findAllByMemberId(memberId).stream()
                .map(CreatePlanBookmarkResponse::from)
                .collect(Collectors.toList());
    }

    private Member findMemberById(Long memberId) {
        log.info("Looking for member with ID: {}", memberId);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> {
                    log.error("Member not found - ID: {}", memberId);
                    return new RuntimeException("멤버를 찾을 수 없음 - ID: " + memberId);
                });
    }

    private Plan findPlanById(Long planId) {
        log.info("Looking for plan with ID: {}", planId);
        return planRepository.findById(planId)
                .orElseThrow(() -> {
                    log.error("Plan not found - ID: {}", planId);
                    return new RuntimeException("일정을 찾을 수 없음 - ID: " + planId);
                });
    }

}
