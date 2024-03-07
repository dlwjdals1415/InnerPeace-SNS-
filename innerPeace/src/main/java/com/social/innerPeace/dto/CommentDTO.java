package com.social.innerPeace.dto;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long comment_no;
    private String comment_content;
    private Long post_no;
    private String nickName;
    private String profile_image;
    private LocalDateTime comment_regday;

}
