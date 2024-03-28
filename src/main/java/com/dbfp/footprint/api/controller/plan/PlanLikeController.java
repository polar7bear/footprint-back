package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.service.plan.PlanLikeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans/like")
public class PlanLikeController {

    private final PlanLikeService planLikeService;

//    @ApiResponse(responseCode = "200", description = "플랜 좋아요 ", content = @Content(mediaType = "application/json", examples = {
//            @ExampleObject(name = "성공적인 응답", value = "{\"liked\": true}")
//    }))
//    @PostMapping("/{planId}")
//    public ResponseEntity<?> toggleLike(@PathVariable Long planId, @RequestParam Long memberId) {
//        boolean isLiked = planLikeService.toggleLike(memberId, planId);
//        return ResponseEntity.ok(Map.of("liked", isLiked));
//    }


    @ApiResponse(responseCode = "200", description = "좋아요 토글 성공",
            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                    examples = {@io.swagger.v3.oas.annotations.media.ExampleObject(name = "성공적인 응답",
                            value = "{\"liked\": true, \"likeCount\": 10}")})})

    @Transactional
    @PostMapping("/{planId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long planId, @RequestParam Long memberId) {
        boolean isLiked = planLikeService.toggleLike(memberId, planId);

        int likeCount = planLikeService.countLikes(planId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("liked", isLiked);
        responseBody.put("likeCount", likeCount);

        return ResponseEntity.ok(responseBody);
    }
    @ApiResponse(responseCode = "200", description = "플랜 좋아요 수 조회 성공", content = @Content(mediaType = "application/json", examples = {
                    @ExampleObject(name = "성공적인 응답", value = "{\"count\": 10}")
    }))
    @Transactional(readOnly = true)
    @GetMapping("/{planId}/count")
    public ResponseEntity<?> countLikes(@PathVariable Long planId) {
        int count = planLikeService.countLikes(planId);
        return ResponseEntity.ok(Map.of("count", count));
    }

}
