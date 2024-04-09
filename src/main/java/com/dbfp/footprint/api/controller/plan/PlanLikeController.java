package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.response.ApiResult;
import com.dbfp.footprint.api.service.plan.PlanLikeService;
import io.swagger.v3.oas.annotations.Operation;
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


    @Operation(summary = "좋아요 토글", description = "특정 계획에 대한 사용자의 좋아요 상태를 토글합니다. " +
            "좋아요가 추가되었다면 liked 값이 true로 반환되고, " +
            "취소되었다면 liked 값이 false로 반환됩니다.")
    @ApiResponse(responseCode = "200", description = "좋아요 토글 성공",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(name = "성공적인 응답", value = "{\"liked\": true, \"likeCount\": 10}")))
    @Transactional
    @PostMapping("/{planId}")
    public ResponseEntity<ApiResult<Map<String, Object>>> toggleLike(@PathVariable Long planId, @RequestParam Long memberId) {
        boolean isLiked = planLikeService.toggleLike(memberId, planId);
        int likeCount = planLikeService.countLikes(planId);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("liked", isLiked);
        responseBody.put("likeCount", likeCount);

        return ResponseEntity.ok(new ApiResult<>(responseBody));
    }
    @Operation(summary = "플랜 좋아요 수 조회", description = "특정 계획에 대한 좋아요 수를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "플랜 좋아요 수 조회 성공",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(name = "성공적인 응답", value = "{\"count\": 10}")))
    @Transactional(readOnly = true)
    @GetMapping("/{planId}/count")
    public ResponseEntity<ApiResult<Integer>> countLikes(@PathVariable Long planId) {
        int count = planLikeService.countLikes(planId);
        return ResponseEntity.ok(new ApiResult<>(count));
    }

}
