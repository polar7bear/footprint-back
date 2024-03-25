package com.dbfp.footprint.api.controller.plan;

import com.dbfp.footprint.api.service.plan.PlanLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plans/like")
public class PlanLikeController {

    private final PlanLikeService planLikeService;

    @PostMapping("/{planId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long planId, @RequestParam Long memberId) {
        boolean isLiked = planLikeService.toggleLike(memberId, planId);
        return ResponseEntity.ok(Map.of("liked", isLiked));
    }

    @GetMapping("/{planId}/count")
    public ResponseEntity<?> countLikes(@PathVariable Long planId) {
        int count = planLikeService.countLikes(planId);
        return ResponseEntity.ok(Map.of("count", count));
    }

}
