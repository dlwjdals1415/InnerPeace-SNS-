package com.social.innerPeace.board.post.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.entity.Comment;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
import com.social.innerPeace.repository.CommentRepository;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
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
public class BoardPostServiceImpl implements BoardPostService{
    @Value("${thumbnail.dir}")
    String thumbnail_dir;
    @Value("${upload.dir}")
    String upload_dir;
    @Value("${profile.dir}")
    String profile_dir;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HealerRepository healerRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    FileStore fileStore;

    @Override
    public String write(PostDTO dto) {
        Optional<Healer> optionalHealer = healerRepository.findByHealerNickName(dto.getPost_writer());
        if(optionalHealer.isPresent()){
            Post post;
            try {
                dto = fileStore.storeFile(dto);
                Healer healer = optionalHealer.get();
                post = dtoToEntity(dto);
                post.setPost_writer(healer);
                if (dto.getMap_point_lat() != null) {
                    post.setMap_point_lat(Float.parseFloat(dto.getMap_point_lat()));
                    post.setMap_point_lng(Float.parseFloat(dto.getMap_point_lng()));
                }
                post = postRepository.save(post);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return post.getPost_image();
        }
        return null;
    }
    @Override
    public PostDTO findImagename(Long post_no){
        Post post = postRepository.findById(post_no).orElse(null);
        assert post != null;
        PostDTO dto = PostDTO.builder()
                .post_image(post.getPost_image())
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
    public PostDTO findByPostNo(Long postNo){
        Optional<Post> optionalPost = postRepository.findById(postNo);
        String base64String = null;
        if(optionalPost.isPresent()){
            Post post = optionalPost.get();
            PostDTO postDTO = entityToDto(post);
            String image = findImagename(postDTO.getPost_no()).getPost_image();
            String imagePath = upload_dir + image;
            //String profileImage = healerRepository.findBy
            //String profileImagePath = profile_dir +
            try {
                byte[] fileBytes = readBytesFromFile(imagePath);
                base64String = encodeBytesToBase64(fileBytes);
            } catch (IOException e) {
                // 예외 처리
            }
            postDTO.setPost_image("data:image/png;base64," + base64String);

            return postDTO;
        }

        return null;
    }



}
