package com.dbfp.footprint.api.service.plan;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.plan.PlanBookmarkRepository;
import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.api.response.PlanBookmarkResponse;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.PlanBookmark;
import com.dbfp.footprint.dto.PlanBookmarkDto;
import com.dbfp.footprint.dto.PlanDto;
import com.dbfp.footprint.exception.member.NotFoundMemberException;
import com.dbfp.footprint.exception.plan.BookmarkAlreadyExistsException;
import com.dbfp.footprint.exception.plan.BookmarkNotFoundException;
import com.dbfp.footprint.exception.plan.PlanNotFoundException;
import com.dbfp.footprint.exception.plan.PlanNotVisibleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        log.info("Member ID: {}, Plan ID: {}", memberId, planId);
        Member member = findMemberById(memberId);
        Plan plan = findPlanById(planId);

        if (!plan.isVisible()) {
            log.info("Plan ID: {}", planId);
            throw new PlanNotVisibleException("공유가 허용되지 않은 일정입니다.");
        }

        Optional<PlanBookmark> existingBookmark = planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId);
        if (existingBookmark.isPresent()) {
            log.info("Member ID: {}, Plan ID: {}", memberId, planId);
            throw new BookmarkAlreadyExistsException("즐겨찾기가 이미 존재합니다.");
        }

        plan.addBookmark();
        PlanBookmark savedBookmark =  planBookmarkRepository.save(PlanBookmark.of(member, plan));
        log.info("Bookmark ID: {}", savedBookmark.getId());
        return PlanBookmarkDto.from(savedBookmark);
    }

    @Transactional
    public void removeBookmark(Long memberId, Long planId) {
        log.info("Member ID: {}, Plan ID: {}", memberId, planId);

        PlanBookmark bookmark = planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId)
                .orElseThrow(() -> new BookmarkNotFoundException("존재하지 않는 즐겨찾기입니다."));

        Plan plan = bookmark.getPlan();
        plan.removeBookmark();

        planBookmarkRepository.delete(bookmark);
        log.info("Member ID: {}, Plan ID: {}", memberId, planId);
    }

    @Transactional
    public Page<PlanBookmarkResponse> findBookmarksByMemberId(Long memberId, Pageable pageable) {
        log.info("member - ID: {}", memberId);
        return planBookmarkRepository.findAllByMemberId(memberId, pageable).map(PlanBookmarkResponse::from);
    }

    private Member findMemberById(Long memberId) {
        log.info("Looking for member with ID: {}", memberId);
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundMemberException("멤버를 찾을 수 없음 - ID: " + memberId));
    }

    private Plan findPlanById(Long planId) {
        log.info("Looking for plan with ID: {}", planId);
        return planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("일정을 찾을 수 없음 - ID: " + planId));
    }

    @Transactional(readOnly = true)
    public List<PlanDto> getBookmarkedPlans(Long memberId) {
        List<PlanBookmark> bookmarks = planBookmarkRepository.findAllByMemberId(memberId);

        return bookmarks.stream()
                .map(bookmark -> {
                    Plan plan = bookmark.getPlan();
                    return PlanDto.from(plan);
                })
                .collect(Collectors.toList());
    }

}
