package com.social.innerPeace.board.comment.service;

import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.dto.WriteDTO;
import com.social.innerPeace.dto.comment.CommentDTO;
import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardCommentService {

    Long save(CommentDTO commentDTO);

    List<CommentDTO>findAll(Long postID);
    default Comment dtoToEntity(CommentDTO dto){
        Comment entity = Comment.builder()
                .comment_content(dto.getComment_content())

                .build();
        return entity;
    }

    default CommentDTO entityToDto(Comment entity){
        CommentDTO dto = CommentDTO.builder()
                .post_no(entity.getPost_no().getPost_no())
                .healerEmail(entity.getHealer_nickname().getHealerEmail())
                .nickName(entity.getHealer_nickname().getHealer_nickname())
                .comment_content(entity.getComment_content())
                .build();
        return dto;
    }

    default List<CommentDTO> toList(List<Comment> commentList){
        return commentList.stream().map(entity->entityToDto(entity)).collect(Collectors.toList());
    }
}
