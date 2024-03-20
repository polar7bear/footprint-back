package com.dbfp.footprint.api.controller;

import com.dbfp.footprint.api.response.CreatePlanBookmarkResponse;
import com.dbfp.footprint.api.service.PlanBookmarkService;
import com.dbfp.footprint.dto.PlanBookmarkDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
public class PlanBookmarkController {

    private final PlanBookmarkService planBookmarkService;

    @PostMapping("/{planId}")
    public ResponseEntity<PlanBookmarkDto> addBookmark(@PathVariable Long planId, @RequestParam Long memberId) {
        PlanBookmarkDto bookmarkDto = planBookmarkService.addBookmark(memberId, planId);
        return new ResponseEntity<>(bookmarkDto, HttpStatus.CREATED);
    }
    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> removeBookmark(@PathVariable Long planId, @RequestParam Long memberId) {
        planBookmarkService.removeBookmark(memberId, planId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CreatePlanBookmarkResponse>> getBookmarksByMember(@RequestParam Long memberId) {
        List<CreatePlanBookmarkResponse> bookmarks = planBookmarkService.findBookmarksByMemberId(memberId);
        return ResponseEntity.ok(bookmarks);
    }
}
