package com.dbfp.footprint.api.service;

import com.dbfp.footprint.api.repository.MemberRepository;
import com.dbfp.footprint.api.repository.PlanBookmarkRepository;
import com.dbfp.footprint.api.repository.PlanRepository;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.PlanBookmark;
import com.dbfp.footprint.dto.PlanBookmarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanBookmarkService {

    private final PlanBookmarkRepository planBookmarkRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    @Transactional
    public PlanBookmarkDto addBookmark(Long memberId, Long planId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 일정입니다."));

        Optional<PlanBookmark> existingBookmark = planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId);
        if (existingBookmark.isPresent()) {
            throw new IllegalStateException("즐겨찾기가 이미 존재합니다..");
        }

        PlanBookmark savedBookmark =  planBookmarkRepository.save(PlanBookmark.of(member, plan));
        return PlanBookmarkDto.from(savedBookmark);
    }

    @Transactional
    public void removeBookmark(Long memberId, Long planId) {
        if (!planBookmarkRepository.findByMemberIdAndPlanId(memberId, planId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 즐겨찾기 입니다.");
        }
        planBookmarkRepository.deleteByMemberIdAndPlanId(memberId, planId);
    }

    public List<PlanBookmarkDto> findBookmarksByMemberId(Long memberId) {
        return planBookmarkRepository.findAllByMemberId(memberId).stream()
                .map(PlanBookmarkDto::from)
                .collect(Collectors.toList());
    }
}
