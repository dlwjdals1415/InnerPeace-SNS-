package com.social.innerPeace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long comment_no;
    private String comment_content;
    private Long Post_no;
    private String healer_id;
    private String healer_nickname;
    private String profileImg;
    private LocalDateTime comment_regday;
}
