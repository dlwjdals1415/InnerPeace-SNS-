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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardCommentServiceImpl implements BoardCommentService {

    @Value("${profile.dir}")
    String profile_dir;

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final HealerRepository healerRepository;

    public Long save(CommentDTO commentDTO) {
        Optional<Post> optionalPost = postRepository.findById(commentDTO.getPost_no());
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Comment comment = dtoToEntity(commentDTO);
            comment.setPost_no(post);
            Optional<Healer> healer = healerRepository.findByHealerNickName(commentDTO.getNickName());
            healer.ifPresent(comment::setHealer);
            return commentRepository.save(comment).getComment_no();
        } else {
            return null;
        }
    }

    @Transactional
    public List<CommentDTO> findAll(Long postNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "comment_no");

// 최대 36개까지 가져오도록 설정
        Pageable pageable = PageRequest.of(0, 36, sort);
        Page<Comment> postEntityList = commentRepository.findAllByPostNoOrderByCommentNoDesc(postNo,pageable);
        if (postEntityList != null && postEntityList.isEmpty() == false) {
            List<CommentDTO> commentDTOList = toList(postEntityList);
            try {
                for (CommentDTO commentDTO : commentDTOList) {
                    String profileImagePath = profile_dir + commentDTO.getHealer_profile_image();
                    String base64String = null;
                    byte[] fileBytes = readBytesFromFile(profileImagePath);
                    base64String = encodeBytesToBase64(fileBytes);
                    commentDTO.setHealer_profile_image("data:image/png;base64," + base64String);
                }

            } catch (IOException e) {
                // 예외 처리
            }

            return commentDTOList;
        }
        return null;
    }

    private static byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }

        return fileBytes;
    }

    private static String encodeBytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

}
