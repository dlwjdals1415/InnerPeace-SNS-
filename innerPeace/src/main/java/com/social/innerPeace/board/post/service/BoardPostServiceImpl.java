package com.social.innerPeace.board.post.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.detail.HealerDetails;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.entity.*;
import com.social.innerPeace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardPostServiceImpl implements BoardPostService {
    @Value("${thumbnail.dir}")
    String thumbnail_dir;
    @Value("${upload.dir}")
    String upload_dir;
    @Value("${profile.dir}")
    String profile_dir;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private HealerRepository healerRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private FileStore fileStore;

    @Override
    public String write(PostDTO dto) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(dto.getHealer_nickname());
        if (optionalHealer.isPresent()) {
            Post post;
            try {
                dto = fileStore.storeFile(dto);
                Healer healer = optionalHealer.get();
                post = dtoToEntity(dto);
                post.setHealer(healer);
                if (!dto.getPost_map_lat().isEmpty() && dto.getPost_map_lat() != null) {
                    post.setPostMapLat(Float.parseFloat(dto.getPost_map_lat()));
                    post.setPostMapLng(Float.parseFloat(dto.getPost_map_lng()));
                }
                post.setTags(splitAndClean(dto.getPost_tags()));
                post = postRepository.save(post);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return post.getPostImage();
        }
        return null;
    }


    //태그를 나눠서 리스트에 추가하는 메소드
    public static List<String> splitAndClean(String input) {
        List<String> words = new ArrayList<>();

        // '#'을 기준으로 문자열을 분리
        String[] splitWords = input.split(" ");

        // 분리된 각 단어에서 공백을 제거하여 리스트에 추가
        for (String word : splitWords) {
            String cleanWord = word.trim(); // 공백 제거
            if (!cleanWord.isEmpty()) { // 빈 문자열이 아니면 리스트에 추가
                words.add(cleanWord);
            }
        }

        return words;
    }

    @Override
    public PostDTO findImagename(Long post_no) {
        Post post = postRepository.findById(post_no).orElse(null);
        assert post != null;
        PostDTO dto = PostDTO.builder()
                .post_image(post.getPostImage())
                .build();
        return dto;
    }

    @Override
    public List<PostDTO> findAllPostsWithBase64Thumbnail() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "postNo")).stream().limit(36).collect(Collectors.toList());
        List<PostDTO> dtoList = new ArrayList<>();

        for (Post post : posts) {
            String image = findImagename(post.getPostNo()).getPost_image();
            String imagePath = thumbnail_dir + image;
            String base64String = null;
            try {
                byte[] fileBytes = readBytesFromFile(imagePath);
                base64String = encodeBytesToBase64(fileBytes);
            } catch (IOException e) {
                // 예외 처리
            }
            PostDTO dto = PostDTO
                    .builder()
                    .post_no(post.getPostNo())
                    .post_image_thumbnail("data:image/png;base64," + base64String)
                    .build();
            dtoList.add(dto);
        }
        return dtoList;
    }

    private static byte[] readBytesFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(fileBytes);
        }

        return fileBytes;
    }

    // 바이트 배열을 Base64로 인코딩하는 메서드
    private static String encodeBytesToBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public PostDTO findByPostNo(Long postNo, String healerNickName) {
        Post post = postRepository.findByPostNoWithHealer(postNo);
        String base64String = null;
        if (post != null) {
            List<Post_Like> likeList = likeRepository.findByPostPostNo(postNo);
            List<Follow> followerList = healerRepository.findFollowersByHealerEmail(post.getHealer().getHealerEmail());
            PostDTO postDTO = entityToDto(post);
            postDTO.setHealer_nickname(post.getHealer().getHealerNickName());
            String image = findImagename(postDTO.getPost_no()).getPost_image();
            String postimagePath = upload_dir + image;
            String profileImagePath = profile_dir + post.getHealer().getHaelerProfileImage();
            postDTO.setLikes(likeList.size());
            if (healerNickName != null && !healerNickName.isEmpty()) {
                Optional<Post_Like> like = likeList.stream().filter(post_like -> post_like.getHealer().getHealerNickName().equals(healerNickName)).findFirst();
                if (like.isPresent()) {
                    postDTO.setLikeStatus("좋아요 취소");
                } else {
                    postDTO.setLikeStatus("좋아요");
                }
                Optional<Follow> follow = followerList.stream().filter(f -> f.getFollowing().getHealerNickName().equals(post.getHealer().getHealerNickName())).findFirst();
                if (follow.isPresent()) {
                    postDTO.setFollowstat("팔로우 취소");
                } else {
                    postDTO.setFollowstat("팔로우");
                }
            }
            try {
                byte[] fileBytes = readBytesFromFile(postimagePath);
                base64String = encodeBytesToBase64(fileBytes);
                postDTO.setPost_image("data:image/png;base64," + base64String);
                fileBytes = readBytesFromFile(profileImagePath);
                base64String = encodeBytesToBase64(fileBytes);
                postDTO.setHealer_profile_image("data:image/png;base64," + base64String);

            } catch (IOException e) {
                // 예외 처리
            }

            return postDTO;
        }

        return null;
    }

    @Override
    public String modify(PostDTO dto) {
        Post post;
        String oldImage = dto.getPost_image();
        if (dto.getPost_image().equals(dto.getPost_image())) {

        }
        try {
            dto = fileStore.storeFile(dto);
            post = dtoToEntity(dto);
            if (!dto.getPost_map_lat().isEmpty() && dto.getPost_map_lat() != null) {
                post.setPostMapLat(Float.parseFloat(dto.getPost_map_lat()));
                post.setPostMapLng(Float.parseFloat(dto.getPost_map_lng()));
            }
            post.setTags(splitAndClean(dto.getPost_tags()));
            post = postRepository.save(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return post.getPostImage();
    }

    @Override
    public String like(Long postNo, String healerNickname) {
        Optional<Healer> healer = healerRepository.findByHealerNickName(healerNickname);
        if (healer.isPresent()) {
            Optional<Post> post = postRepository.findById(postNo);
            if (post.isPresent()) {
                Optional<Post_Like> like = likeRepository.findByHealerHealerEmailAndPostPostNo(healer.get().getHealerEmail(), post.get().getPostNo());
                if (like.isPresent()) {
                    likeRepository.delete(like.get());
                    return "좋아요";
                } else {
                    Post_Like post_like = Post_Like.builder()
                            .healer(healer.get())
                            .post(post.get())
                            .build();
                    likeRepository.save(post_like);
                    return "좋아요 취소";
                }
            }
        }
        return null;
    }

}
