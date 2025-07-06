package com.chakak.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chakak.domain.User;
import com.chakak.dto.ReportDto;
import com.chakak.service.CustomUserDetails;
import com.chakak.service.ReactionService;



@RestController
@RequestMapping("/")
public class ReactionController {
	
	@Autowired
	private ReactionService reactionService;
	
	@GetMapping("/users/me/likes") 
	public ResponseEntity<List<ReportDto>> getLikedReports(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        //String userId = userDetails.getUsername();
		
		///🌟🌟 하드코딩 처리 ////////
		String userId = "test1234";
    	
    	
    	////////////////////////
    	///
        return ResponseEntity.ok(reactionService.getReactionsByType(userId, "LIKE"));
    }
	
	@GetMapping("/users/me/dislikes")
	public ResponseEntity<List<ReportDto>> getDislikedReports(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        //String userId = userDetails.getUsername();
		
		///🌟🌟🌟🌟 하드코딩 처리 ////
		String userId= "test1234";
    	///
        return ResponseEntity.ok(reactionService.getReactionsByType(userId, "DISLIKE"));
    }
	

	// 좋아요 또는 싫어요 등록 / 취소 (toggle)
	@PostMapping("/reactions/{reportId}")
	 public ResponseEntity<String> toggleReaction(
	            @PathVariable Long reportId,
	            @RequestParam String reactionType,
	            @AuthenticationPrincipal CustomUserDetails userDetails) {
		
		////🌟🌟🌟🌟 하드코딩 /////////
		String userId = "test1234"; // ✅ 하드코딩된 userId
        

	        User user = new User();
	        user.setUserId(userDetails.getUsername());

	        reactionService.toggleReaction(reportId, user, reactionType);
	        return ResponseEntity.ok("반응이 처리되었습니다.");
	    }
	
	//좋아요 또는 싫어요 개수 
	 @GetMapping("/reactions/{reportId}/count")
	    public ResponseEntity<Long> countReaction(
	            @PathVariable Long reportId,
	            @RequestParam String reactionType
	    ) {
	        long count = reactionService.countReaction(reportId, reactionType);
	        return ResponseEntity.ok(count);
	    }
}
