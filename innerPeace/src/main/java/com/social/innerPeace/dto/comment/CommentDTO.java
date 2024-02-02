package com.social.innerPeace.dto.comment;

import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;

@Data
@Setter
@Getter
@ToString
@Builder
public class CommentDTO {

        private String comment_content;
        private Long post_no;
        private String healerEmail;
private String nickName;

        public static Comment toCommentDTO(Comment comment, Long comment_no){
                Comment commentDTO = new Comment();
                commentDTO.setComment_content(comment.getComment_content());
                commentDTO.setPost_no(comment.getPost_no());



                return commentDTO;
        }


}
