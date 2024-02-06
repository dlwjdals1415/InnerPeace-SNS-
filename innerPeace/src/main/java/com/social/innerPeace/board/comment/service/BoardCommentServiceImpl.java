package com.social.innerPeace.board.comment.service;

import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.dto.CommentDTO;
import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
import com.social.innerPeace.repository.CommentRepository;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final HealerRepository healerRepository;

    public Long save(CommentDTO commentDTO) {
        Optional<Post> optionalPost = postRepository.findById(commentDTO.getPost_no());
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Comment comment = dtoToEntity(commentDTO);
            comment.setPost_no(post);
            Optional<Healer> healer = healerRepository.findByHealerNickName(commentDTO.getHealerEmail());
            healer.ifPresent(comment::setHealer_nickname);
            return commentRepository.save(comment).getComment_no();
        } else {
            return null;
        }
    }

    @Transactional
    public List<CommentDTO>findAll(Long postNo) {
        List<Comment> postEntityList = commentRepository.findAllByPostNo(postNo);
        if(postEntityList != null && postEntityList.isEmpty()==false){
            List<CommentDTO> commentDTOList = toList(postEntityList);
            return commentDTOList;
        }
        return null;
    }

}
