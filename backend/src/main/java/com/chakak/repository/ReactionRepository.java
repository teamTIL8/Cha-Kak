package com.chakak.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chakak.domain.Reaction;



@Repository
public interface ReactionRepository extends JpaRepository<Reaction , Long > {
	List<Reaction> findByUser_UserIdAndReactionType ( String userId ,String reactionType );

}
