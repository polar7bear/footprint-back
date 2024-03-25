package com.dbfp.footprint.api.service.plan;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.plan.PlanLikeRepository;
import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.PlanLike;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlanLikeService {

    private final PlanLikeRepository planLikeRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    @Transactional
    public  boolean toggleLike(Long memberId, Long planId){

        Member member = findMemberById(memberId);
        Plan plan = findPlanById(planId);

        Optional<PlanLike> like = planLikeRepository.findByMemberIdAndPlanId(memberId, planId);
        if (like.isPresent()) {
            planLikeRepository.delete(like.get());
            return false;//좋아요 취소
        } else {
            planLikeRepository.save(PlanLike.of(member, plan));
            return true;//좋아요 추가
        }
    }

    public int countLikes(Long planId) {
        return planLikeRepository.countByPlanId(planId);
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
