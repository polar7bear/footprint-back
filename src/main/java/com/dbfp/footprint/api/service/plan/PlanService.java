package com.dbfp.footprint.api.service.plan;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.place.PlaceDetailsRepository;
import com.dbfp.footprint.api.repository.place.PlaceRepository;
//import com.dbfp.footprint.api.repository.plan.CopyPlanRepository;
import com.dbfp.footprint.api.repository.plan.PlanBookmarkRepository;
import com.dbfp.footprint.api.repository.plan.PlanLikeRepository;
import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.api.repository.schedule.ScheduleRepository;
import com.dbfp.footprint.api.response.PlanResponse;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.domain.place.PlaceDetails;
import com.dbfp.footprint.domain.plan.CopyPlan;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.Schedule;
import com.dbfp.footprint.dto.PlaceDetailsDto;
import com.dbfp.footprint.dto.PlaceDto;
import com.dbfp.footprint.dto.PlanDto;
import com.dbfp.footprint.dto.ScheduleDto;
import com.dbfp.footprint.exception.plan.PlanNotFoundException;
import com.dbfp.footprint.exception.plan.PlanNotVisibleException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final ScheduleRepository scheduleRepository;
    private final PlaceRepository placeRepository;
    private final PlaceDetailsRepository placeDetailsRepository;
    private final MemberRepository memberRepository;
    private final PlanLikeRepository planLikeRepository;
    private final PlanBookmarkRepository planBookmarkRepository;
    //private final CopyPlanRepository copyPlanRepository;

    @Transactional
    public PlanDto createPlan(PlanDto planDto, Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Plan plan = Plan.of(planDto, member);
        plan = planRepository.save(plan);
        Plan finalPlan = plan;
        int totalCost = 0;

        for (ScheduleDto scheduleDto : planDto.getSchedules()) {
            Schedule schedule = Schedule.of(scheduleDto, finalPlan);
            schedule = scheduleRepository.save(schedule);

            for (PlaceDto placeDto : scheduleDto.getPlaces()) {
                Place place = Place.of(placeDto, schedule);
                place = placeRepository.save(place);

                /*for (PlaceDetailsDto placeDetailsDto : placeDto.getPlaceDetails()) {
                    PlaceDetails details = PlaceDetails.of(placeDetailsDto, place);
                    placeDetailsRepository.save(details);

                    totalCost += placeDetailsDto.getCost();
                }*/
                PlaceDetailsDto placeDetailsDto = placeDto.getPlaceDetails();
                if (placeDetailsDto != null) {
                    PlaceDetails details = PlaceDetails.of(placeDetailsDto, place);
                    placeDetailsRepository.save(details);
                    totalCost += placeDetailsDto.getCost();
                }
            }
        }

        PlanDto resultDto = PlanDto.from(finalPlan);
        resultDto.setTotalCost(totalCost);
        return resultDto;
    }

    @Transactional
    public PlanDto updatePlan(Long planId, PlanDto dto) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 여행계획입니다."));

        dto.updatePlanBasicInfo(plan, dto);
        updateSchedule(plan, dto.getSchedules());
        planRepository.save(plan);

        int totalCost = calculateTotalCost(dto.getSchedules());
        PlanDto updatedPlanDto = PlanDto.from(plan);
        updatedPlanDto.setTotalCost(totalCost);

        return updatedPlanDto;
    }

    public void updateSchedule(Plan plan, List<ScheduleDto> scheduleDtos) {

        plan.getSchedules().removeIf(schedule ->
                scheduleDtos.stream().noneMatch(scheduleDto -> scheduleDto.getDay() == schedule.getDay()));

        for (ScheduleDto scheduleDto : scheduleDtos) {
            Schedule schedule = plan.getSchedules().stream()
                    .filter(s -> s.getDay() == scheduleDto.getDay())
                    .findFirst()
                    .orElseGet(() -> {
                        Schedule newSchedule = new Schedule();
                        newSchedule.setDay(scheduleDto.getDay());
                        newSchedule.setPlan(plan);
                        plan.getSchedules().add(newSchedule);

                        return newSchedule;
                    });

            updatePlace(schedule, scheduleDto.getPlaces());
        }
    }

    public void updatePlace(Schedule schedule, List<PlaceDto> placeDtos) {

        schedule.getPlace().removeIf(place ->
                placeDtos.stream().noneMatch(placeDto -> placeDto.getPlaceName().equals(place.getPlaceName())));

        for (PlaceDto placeDto : placeDtos) {
            Place place = schedule.getPlace().stream()
                    .filter(p -> p.getPlaceName().equals(placeDto.getPlaceName()))
                    .findFirst()
                    .orElseGet(() -> {
                        Place newPlace = new Place();
                        newPlace.setPlaceName(placeDto.getPlaceName());
                        newPlace.setSchedule(schedule);
                        schedule.getPlace().add(newPlace);

                        return newPlace;
                    });
            placeDto.updatePlaceBasicInfo(placeDto, place);

            if(placeDto.getPlaceDetails() != null) {
                updatePlaceDetails(place, placeDto.getPlaceDetails());
            }
        }
    }

    public void updatePlaceDetails(Place place, PlaceDetailsDto placeDetailsDto) {

        PlaceDetails details = place.getPlaceDetails() != null ? place.getPlaceDetails() : new PlaceDetails();
        details.setMemo(placeDetailsDto.getMemo());
        details.setCost(placeDetailsDto.getCost());
        details.setVisitTime(placeDetailsDto.getVisitTime());
        details.setPlace(place);
        place.setPlaceDetails(details);
        placeDetailsRepository.save(details);
    }

    public void deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 여행계획입니다."));

        planRepository.delete(plan);
    }

    public int calculateTotalCost(List<ScheduleDto> scheduleDtos) {
        int totalCost = 0;
        for (ScheduleDto scheduleDto : scheduleDtos) {
            for (PlaceDto placeDto : scheduleDto.getPlaces()) {
                totalCost += placeDto.getPlaceDetails().getCost();;
            }
        }
        return totalCost;
    }

    

    @Transactional(readOnly = true)
    public PlanResponse getPlanDetails(Long planId, Long memberId) {
        //여행 계획 존재하는지 확인
        Plan plan = getPlanIfExists(planId);
        //소유자 확인
        //공개되지 않았고 소유자가 아닌 경우 접근 거부
        checkPlanVisibility(plan, memberId);

        return PlanResponse.from(plan);
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> getSchedulesByPlanId(Long planId, Long memberId){
        //여행 계획 존재하는지 확인
        Plan plan = getPlanIfExists(planId);
        //소유자 확인
        //공개되지 않았고 소유자가 아닌 경우 접근 거부
        checkPlanVisibility(plan, memberId);
        List<Schedule> schedules = scheduleRepository.findByPlanId(planId);
        return schedules.stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ScheduleDto> getSchedulesByPlanIdAndDay(Long planId, int day, Long memberId) {
        Plan plan = getPlanIfExists(planId);
        //소유자 확인
        //공개되지 않았고 소유자가 아닌 경우 접근 거부
        checkPlanVisibility(plan, memberId);

        // Retrieve schedule by planId and day
        Optional<ScheduleDto> scheduleDto = scheduleRepository.findByPlanIdAndDay(planId, day)
                .map(ScheduleDto::from);

        return scheduleDto;
    }


    @Transactional(readOnly = true)
    public Page<PlanResponse> getPublicPlans(Pageable pageable) {
        return planRepository.findByVisibleTrue(pageable)
                .map(PlanResponse::from);
   }

//    @Transactional(readOnly = true)
//    public Page<PlanResponse> searchPlansByKeyword(String keyword, Pageable pageable) {
//        String keywordLike = null;
//        if (keyword != null && !keyword.isEmpty()) {
//            keywordLike = "%" + keyword.toLowerCase() + "%";
//        }
//        return planRepository.findByKeyword(keywordLike, pageable)
//                .map(PlanResponse::from);
//    }
    @Transactional(readOnly = true)
    public Page<PlanResponse> findPlansByUserId(Long memberId, Pageable pageable) {
        return planRepository.findByMemberId(memberId, pageable).map(PlanResponse::from);
    }


    private Plan getPlanIfExists(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new PlanNotFoundException("존재하지 않는 여행 계획입니다"));
    }

    private void checkPlanVisibility(Plan plan, Long memberId) {
        boolean isOwner = plan.getMember().getId().equals(memberId);
        if (!plan.isVisible() && !isOwner) {
            throw new PlanNotVisibleException("접근이 거부되었습니다.");
        }
    }
}
