package com.chakak.dto;

import com.chakak.domain.Reaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReactionReportDto {
    private Long reportId;
    private String reportTitle;
    private String reactionType;     
    private LocalDateTime reactedAt; 

    public static ReactionReportDto fromEntity(Reaction reaction) {
        return ReactionReportDto.builder()
                .reportId(reaction.getReport().getReportId())
                .reportTitle(reaction.getReport().getTitle()) 
                .reactionType(reaction.getReactionType())
                .reactedAt(reaction.getCreatedAt())
                .build();
    }
}
