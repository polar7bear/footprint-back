package com.dbfp.footprint.api.service.plan;

import com.dbfp.footprint.api.repository.member.MemberRepository;
import com.dbfp.footprint.api.repository.place.PlaceDetailsRepository;
import com.dbfp.footprint.api.repository.place.PlaceRepository;
import com.dbfp.footprint.api.repository.plan.PlanRepository;
import com.dbfp.footprint.api.repository.schedule.ScheduleRepository;
import com.dbfp.footprint.domain.Member;
import com.dbfp.footprint.domain.place.Place;
import com.dbfp.footprint.domain.place.PlaceDetails;
import com.dbfp.footprint.domain.plan.Plan;
import com.dbfp.footprint.domain.plan.Schedule;
import com.dbfp.footprint.dto.PlaceDetailsDto;
import com.dbfp.footprint.dto.PlaceDto;
import com.dbfp.footprint.dto.PlanDto;
import com.dbfp.footprint.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final ScheduleRepository scheduleRepository;
    private final PlaceRepository placeRepository;
    private final PlaceDetailsRepository placeDetailsRepository;
    private final MemberRepository memberRepository;

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

                for (PlaceDetailsDto placeDetailsDto : placeDto.getPlaceDetails()) {
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

    public PlanDto updatePlan(Long planId, PlanDto dto) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 여행계획입니다."));

        dto.updatePlanBasicInfo(plan, dto);
        updateSchedule(plan, dto.getSchedules());
        planRepository.save(plan);

        return PlanDto.from(plan);
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

            updatePlaceDetails(place, placeDto.getPlaceDetails());
        }
    }

    public void updatePlaceDetails(Place place, List<PlaceDetailsDto> placeDetailsDtos) {

        place.getPlaceDetails().removeIf(details ->
                placeDetailsDtos.stream().noneMatch(placeDetailsDto -> placeDetailsDto.getVisitTime().equals(details.getVisitTime())));

        for (PlaceDetailsDto detailsDto : placeDetailsDtos) {
            PlaceDetails details = place.getPlaceDetails().stream()
                    .filter(d -> d.getVisitTime().equals(detailsDto.getVisitTime()))
                    .findFirst()
                    .orElseGet(() -> {
                        PlaceDetails newDetails = new PlaceDetails();
                        newDetails.setPlace(place);
                        place.getPlaceDetails().add(newDetails);

                        return newDetails;
                    });
            detailsDto.updatePlaceDetailsBasicInfo(detailsDto, details);
        }
    }

    public void deletePlan(Long planId) {
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 여행계획입니다."));

        planRepository.delete(plan);
    }

    public PlanDto getPlanDetails(Long planId, Long memberId) {
        //여행 계획 존재하는지 확인
        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 여행 계획입니다"));

        //소유자 확인
        boolean isOwner = plan.getMember().getId().equals(memberId);

        //공개되지 않았고 소유자가 아닌 경우 접근 거부
        if (!plan.isVisible() && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "접근이 거부되었습니다.");
        }

        return PlanDto.from(plan);
    }

    public Page<PlanDto> getPublicPlans(Pageable pageable) {
        return planRepository.findByVisibleTrue(pageable)
                .map(PlanDto::from);
    }

    public List<PlanDto> findPlansByUserId(Long memberId) {
        List<Plan> plans = planRepository.findByMemberId(memberId);
        return plans.stream().map(PlanDto::from).collect(Collectors.toList());
    }

}
