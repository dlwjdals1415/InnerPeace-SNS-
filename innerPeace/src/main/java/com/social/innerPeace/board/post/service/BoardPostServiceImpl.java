package com.social.innerPeace.board.post.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.detail.HealerDetails;
import com.social.innerPeace.dto.CommentDTO;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.entity.*;
import com.social.innerPeace.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    private final Map<String, String> imageCache = new HashMap<>();

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
                if (!dto.getPost_map_lat().isEmpty()) {
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
    public List<PostDTO> list() {
        List<Post> posts = postRepository.findAll(Sort.by(Sort.Direction.DESC, "postNo")).stream().limit(36).collect(Collectors.toList());
        return listEntityToDto(posts);
    }

    @Override
    public List<PostDTO> scrollList(Long postNo) {
        Pageable pageable = PageRequest.of(0, 36, Sort.by("postNo").descending());
        Page<Post> posts = postRepository.findByPostNoLessThanOrderByPostNoDesc(postNo, pageable);
        List<Post> postList = posts.getContent(); // 실제 Post 목록을 얻음
        return listEntityToDto(postList);
    }

    @Override
    public List<PostDTO> search(String searchkey) {
        String modifiedSearchKey = searchkey.trim(); // 앞뒤 공백 제거

        // searchKey가 "#"으로 시작하는 경우
        if (modifiedSearchKey.startsWith("#")) {
            modifiedSearchKey = modifiedSearchKey.substring(1); // "#" 제거
            Pageable pageable = PageRequest.of(0, 36, Sort.by("postNo").descending());
            Page<Post> posts = postRepository.findByTagContaining(modifiedSearchKey, pageable);
            List<Post> postList = posts.getContent(); // 실제 Post 목록을 얻음
            return listEntityToDto(postList);
        }
        // searchKey가 "@"으로 시작하는 경우
        else if (modifiedSearchKey.startsWith("@")) {
            modifiedSearchKey = modifiedSearchKey.substring(1); // "@" 제거
            Pageable pageable = PageRequest.of(0, 36, Sort.by("postNo").descending());
            Page<Post> posts = postRepository.findByHealerNickNameContaining(modifiedSearchKey, pageable);
            List<Post> postList = posts.getContent(); // 실제 Post 목록을 얻음
            return listEntityToDto(postList);
        }
        Pageable pageable = PageRequest.of(0, 36, Sort.by("postNo").descending());
        Page<Post> posts = postRepository.findByContentContaining(modifiedSearchKey, pageable);
        List<Post> postList = posts.getContent(); // 실제 Post 목록을 얻음
        return listEntityToDto(postList);
    }

    @Override
    public List<PostDTO> searchScrollList(Long postNo, String search) {
        String modifiedSearchKey = search.trim(); // 앞뒤 공백 제거

        // searchKey가 "#"으로 시작하는 경우
        if (modifiedSearchKey.startsWith("#")) {
            modifiedSearchKey = modifiedSearchKey.substring(1); // "#" 제거
            Pageable pageable = PageRequest.of(0, 36, Sort.by("postNo").descending());
            Page<Post> posts = postRepository.findByTagContainingAndPostNoLessThan(modifiedSearchKey, postNo, pageable);
            List<Post> postList = posts.getContent(); // 실제 Post 목록을 얻음
            return listEntityToDto(postList);
        }
        // searchKey가 "@"으로 시작하는 경우
        else if (modifiedSearchKey.startsWith("@")) {
            modifiedSearchKey = modifiedSearchKey.substring(1); // "@" 제거
            Pageable pageable = PageRequest.of(0, 36, Sort.by("postNo").descending());
            Page<Post> posts = postRepository.findByHealerNickNameContainingAndPostNoLessThan(modifiedSearchKey, postNo, pageable);
            List<Post> postList = posts.getContent(); // 실제 Post 목록을 얻음
            return listEntityToDto(postList);
        }
        Pageable pageable = PageRequest.of(0, 36, Sort.by("postNo").descending());
        Page<Post> posts = postRepository.findByContentContainingAndPostNoLessThan(modifiedSearchKey, postNo, pageable);
        List<Post> postList = posts.getContent(); // 실제 Post 목록을 얻음
        return listEntityToDto(postList);
    }

    @Override
    public List<PostDTO> listByHealerNickname(String healerNickname) {
        List<Post> posts = postRepository.findByHealerNickname(healerNickname, Sort.by(Sort.Direction.DESC, "postNo")).stream().limit(36).collect(Collectors.toList());
        return listEntityToDto(posts);
    }

    @Override
    public PostDTO detail(Long postNo, String healerNickName) {
        Post post = postRepository.findByPostNoWithHealer(postNo);
        if (post != null) {
            List<Post_Like> likeList = likeRepository.findByPostPostNo(postNo);
            List<Follow> followerList = healerRepository.findFollowersByHealerEmail(post.getHealer().getHealerEmail());
            PostDTO postDTO = entityToDto(post);
            postDTO.setHealer_nickname(post.getHealer().getHealerNickName());
            String image = findImagename(postDTO.getPost_no()).getPost_image();
            String postimagePath = upload_dir + image;
            String profileImagePath = profile_dir + post.getHealer().getHaelerProfileImage();
            String thumbnailImagePath = thumbnail_dir + image;
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
                String base64String = imageCache.get(postimagePath);
                if (base64String == null) {
                    byte[] fileBytes = readBytesFromFile(postimagePath);
                    base64String = encodeBytesToBase64(fileBytes);
                    imageCache.put(postimagePath, base64String);
                }
                postDTO.setPost_image("data:image/png;base64," + base64String);

                base64String = imageCache.get(thumbnailImagePath);
                if (base64String == null) {
                    byte[] fileBytes = readBytesFromFile(thumbnailImagePath);
                    base64String = encodeBytesToBase64(fileBytes);
                    imageCache.put(thumbnailImagePath, base64String);
                }
                postDTO.setPost_image_thumbnail("data:image/png;base64," + base64String);

                base64String = imageCache.get(profileImagePath);
                if (base64String == null) {
                    byte[] fileBytes = readBytesFromFile(profileImagePath);
                    base64String = encodeBytesToBase64(fileBytes);
                    imageCache.put(profileImagePath, base64String);
                }
                postDTO.setHealer_profile_image("data:image/png;base64," + base64String);

            } catch (IOException e) {
                // 예외 처리
            }

            return postDTO;
        }

        return null;
    }

    @Override
    public PostDTO modify(PostDTO dto, String loginHealer) {
        Optional<Post> optionalPost = postRepository.findById(dto.getPost_no());
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(loginHealer);
        if (!optionalHealer.get().getHealerEmail().equals(optionalPost.get().getHealer().getHealerEmail())) {
            return null;
        }
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            // 게시물을 찾은 경우
            if (dto.getPost_image_thumbnail() != null && !dto.getPost_image_thumbnail().isEmpty()) {
                // 이미지 해시값이 변경된 경우
                try {
                    dto = fileStore.storeFile(dto); // 파일 저장
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!dto.getPost_map_lat().isEmpty()) {
                post.setPostMapLat(Float.parseFloat(dto.getPost_map_lat()));
                post.setPostMapLng(Float.parseFloat(dto.getPost_map_lng()));
            }
            post.setPostImage(dto.getPost_image());
            post.setTags(splitAndClean(dto.getPost_tags()));
            post.setPostContent(dto.getPost_content());
            post = postRepository.save(post);
            return entityToDto(post);
        }
        return null;
    }


    @Override
    public int deletePost(PostDTO postDTO,String loginHealer) {
        Optional<Post> optionalPost = postRepository.findById(postDTO.getPost_no());
        if (!optionalPost.get().getHealer().getHealerNickName().equals(loginHealer)) {
            return 0;
        }else{
            postRepository.deleteById(postDTO.getPost_no());
            return 1;
        }
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

    private List<PostDTO> listEntityToDto(List<Post> posts) {
        List<PostDTO> dtoList = new ArrayList<>();
        for (Post post : posts) {
            String image = findImagename(post.getPostNo()).getPost_image();
            String imagePath = thumbnail_dir + image;
            String base64String = imageCache.get(imagePath);
            if (base64String == null) {
                try {
                    byte[] fileBytes = readBytesFromFile(imagePath);
                    base64String = encodeBytesToBase64(fileBytes);
                    imageCache.put(imagePath, base64String);
                } catch (IOException e) {
                    // 예외 처리
                }
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

    //태그를 나눠서 리스트에 추가하는 메소드
    public static List<String> splitAndClean(String input) {
        return Arrays.stream(input.split("#"))
                .map(String::trim)
                .filter(word -> !word.isEmpty())
                .map(word -> "#" + word)
                .collect(Collectors.toList());
    }


}
