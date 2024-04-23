package com.dbfp.footprint.events.member;

import com.dbfp.footprint.api.repository.RefreshTokenRepository;
import com.dbfp.footprint.api.repository.plan.CopyPlanRepository;
import com.dbfp.footprint.api.repository.plan.PlanBookmarkRepository;
import com.dbfp.footprint.api.repository.plan.PlanLikeRepository;
import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.api.repository.review.ImageRepository;
import com.dbfp.footprint.api.repository.review.ReviewLikeRepository;
import com.dbfp.footprint.api.repository.review.ReviewRepository;
import com.dbfp.footprint.api.repository.schedule.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberDeletionListener {

    private static final Logger logger = LoggerFactory.getLogger(MemberDeletionListener.class);

    private final RefreshTokenRepository refreshTokenRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final PlanRepository planRepository;
    private final PlanLikeRepository planLikeRepository;
    private final PlanBookmarkRepository planBookmarkRepository;
    private final CopyPlanRepository copyPlanRepository;

    @EventListener
    @Transactional
    public void onMemberDeleted(MemberDeletedEvent event) {
        Long memberId = event.getMember().getId();

        //다른 사용자들이 참조할 수 있는 데이터 먼저 처리
        planLikeRepository.deleteByMemberId(memberId);
        planLikeRepository.deleteByPlanIn(event.getMember().getPlans());
        copyPlanRepository.deleteByOriginalPlanIn(event.getMember().getPlans());
        planBookmarkRepository.deleteByPlanIn(event.getMember().getPlans());


        //그 후에 회원과 직접 연관된 데이터 처리
        refreshTokenRepository.deleteByMemberId(memberId);
        reviewLikeRepository.deleteByMemberId(memberId);
        reviewRepository.deleteByMemberId(memberId);
        //planRepository.deleteByMemberId(memberId);
        //planBookmarkRepository.deleteByMemberId(memberId);
        //copyPlanRepository.deleteByMemberId(memberId);

        /*event.getMember().getPlans().forEach(plan -> {
            logger.info("Deleting copy plans for original plan {}", plan.getId());
            copyPlanRepository.deleteByOriginalPlanId(plan.getId());
        });*/

        event.getMember().getPlans().forEach(plan -> {
            logger.info("Deleting plan {}", plan.getId());
            planRepository.deleteById(plan.getId());
        });

    }
}
