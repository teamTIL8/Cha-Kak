package com.chakak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chakak.domain.Reaction;
import com.chakak.domain.Report;
import com.chakak.domain.User;



@Repository
public interface ReactionRepository extends JpaRepository<Reaction , Long > {
	// 내가 누른 반응 조회 시 사용할 메소드
	List<Reaction> findByUser_UserIdAndReactionType ( String userId ,String reactionType );
	
	
	// 반응 등록 및 삭제 시 사용할 메소드 
	List<Reaction> findByUserAndReport(User user , Report report);
	long countByReportAndReactionType(Report report,String reactionType);

}
