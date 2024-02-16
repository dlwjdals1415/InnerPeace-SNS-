package com.social.innerPeace.board.comment.service;

import com.social.innerPeace.dto.CommentListDTO;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService {

    @Value("${profile.dir}")
    String profile_dir;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final HealerRepository healerRepository;

    private final Map<String, String> imageCache = new HashMap<>();

    public Long write(CommentDTO commentDTO) {
        Optional<Post> optionalPost = postRepository.findById(commentDTO.getPost_no());
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Comment comment = dtoToEntity(commentDTO);
            comment.setPost(post);
            Optional<Healer> healer = healerRepository.findByHealerNickName(commentDTO.getNickName());
            healer.ifPresent(comment::setHealer);
            return commentRepository.save(comment).getCommentNo();
        } else {
            return null;
        }
    }

    @Override
    public Long modify(CommentDTO commentDTO) {
        Optional<Comment> optionalComment = commentRepository.findById(commentDTO.getComment_no());
        if(!commentDTO.getNickName().equals(optionalComment.get().getHealer().getHealerNickName())) {
            return 0L;
        }
        Comment comment = optionalComment.get();
        comment.setCommentContent(commentDTO.getComment_content());
        commentRepository.save(comment);
        return 1L;
    }

    @Override
    public Long delete(CommentDTO commentDTO) {
        Optional<Comment> optionalComment = commentRepository.findById(commentDTO.getComment_no());
        if(!commentDTO.getNickName().equals(optionalComment.get().getHealer().getHealerNickName())) {
            return 0L;
        }
        commentRepository.deleteById(commentDTO.getComment_no());
        return 1L;
    }

    @Transactional
    public List<CommentDTO> findAll(Long postNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "commentNo");

// 최대 36개까지 가져오도록 설정
        Pageable pageable = PageRequest.of(0, 36, sort);
        Page<Comment> commentList = commentRepository.findAllByPostNoOrderByCommentNoDesc(postNo,pageable);
        return listCommentToDTOList(commentList);
    }

    @Override
    public List<CommentDTO> scroll(Long postNo, Long commentNo) {
        // 서비스 레이어 또는 컨트롤러에서
        Pageable pageable = PageRequest.of(0, 36, Sort.by("commentNo").descending());
        Page<Comment> comments = commentRepository.findByPostNoAndCommentNoLessThanOrderByCommentNoDesc(postNo, commentNo, pageable);
        return listCommentToDTOList(comments);
    }


    private List<CommentDTO> listCommentToDTOList(Page<Comment> commentList) {
        if (!commentList.isEmpty()) {
            List<CommentDTO> commentDTOList = toList(commentList);
            try {
                for (CommentDTO commentDTO : commentDTOList) {
                    String profileImagePath = profile_dir + commentDTO.getHealer_profile_image();
                    String base64String = imageCache.get(profileImagePath);
                    if (base64String == null) {
                        byte[] fileBytes = readBytesFromFile(profileImagePath);
                        base64String = encodeBytesToBase64(fileBytes);
                        imageCache.put(profileImagePath, base64String);
                    }
                    commentDTO.setHealer_profile_image("data:image/png;base64," + base64String);
                }

            } catch (IOException e) {
                // 예외 처리
            }

            return commentDTOList;
        }
        return null;
    }


}
