package com.chakak.controller;

import com.chakak.dto.CommentDto;
import com.chakak.dto.ReportDto;
import com.chakak.service.CommentService;
import com.chakak.service.CustomUserDetails;
import com.chakak.service.ReactionService;
import com.chakak.service.ReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyPageViewController {
	private final CommentService commentService;
	private final ReactionService reactionService;
    private final ReportService reportService;
    
	// ✅ 내가 쓴 신고글 조회하기 
	 
    @GetMapping("/reports")
    public String getMyReports(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	if (userDetails == null) {
            return "redirect:/login"; 
        }
        String userId = userDetails.getUsername();

        List<ReportDto> myReports = reportService.getMyReports(userId);
        model.addAttribute("myReports", myReports);
        return "report/my/reports";
    }

	
	
	//✅ 내가 쓴 댓글 조회하기 
	@GetMapping("/comments")
	public String getMyComments(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (userDetails == null) {
            return "redirect:/login";
        }
        String userId = userDetails.getUsername();
        
	    List<CommentDto> myComments = commentService.getMyComment(userId);  // ✅ 실제 존재하는 메서드 사용
	    model.addAttribute("myComments", myComments);
	    return "report/my/reports"; 
	}
	
	//✅ 내가 누른 반응 조회 ( 좋아요 ) 
	@GetMapping("/likes")
	public String getLikedReports(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (userDetails == null) {
            return "redirect:/login";
        }
        String userId = userDetails.getUsername();

        List<ReportDto> likedReports = reactionService.getReactionsByType(userId, "LIKE");
        model.addAttribute("likedReports", likedReports);
        return "report/my/reports-reaction"; 
    }

	// ✅ 내가 누른 반응 조회 ( 싫어요 )
    @GetMapping("/dislikes")
    public String getDislikedReports(@AuthenticationPrincipal UserDetails userDetails, Model model) {
    	if (userDetails == null) {
            return "redirect:/login";
        }
        String userId = userDetails.getUsername();
        List<ReportDto> dislikedReports = reactionService.getReactionsByType(userId, "DISLIKE");
        model.addAttribute("dislikedReports", dislikedReports);
        return "report/my/reports-reaction"; 
    }
	
    @GetMapping("/reports-reaction")
    public String showReportsReactionPage() {
        return "report/my/reports-reaction";
    }


}
