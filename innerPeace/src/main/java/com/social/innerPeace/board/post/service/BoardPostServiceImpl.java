package com.social.innerPeace.board.post.service;

import com.social.innerPeace.board.post.component.FileStore;
import com.social.innerPeace.dto.PosTListDTO;
import com.social.innerPeace.dto.WriteDTO;
import com.social.innerPeace.entity.Healer;
import com.social.innerPeace.entity.Post;
import com.social.innerPeace.repository.HealerRepository;
import com.social.innerPeace.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BoardPostServiceImpl implements BoardPostService{
    @Value("${thumbnail.dir}")
    String thumbnail_dir;
    @Autowired
    PostRepository postRepository;
    @Autowired
    HealerRepository healerRepository;
    @Autowired
    FileStore fileStore;
    @Override
    public String write(WriteDTO dto) {
        Optional<Healer> optionalHealer = healerRepository.findById(dto.getPost_writer());
        if(optionalHealer.isPresent()){
            Post post;
            try {
                dto = fileStore.storeFile(dto);
                Healer healer = optionalHealer.get();
                post = dtoToEntity(dto);
                post.setPost_writer(healer);
                post = postRepository.save(post);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return post.getPost_image();
        }
        return null;
    }
    @Override
    public PosTListDTO findImagename(Long post_no){
        Post post = postRepository.findById(post_no).orElse(null);
        PosTListDTO dto = PosTListDTO.builder()
                .post_image(post.getPost_image())
                .build();
        return dto;
    }
    @Override
    public List<Post> findAll(){
        List<Post> post = postRepository.findAll();
        return postRepository.findAll();
    }

    @Override
    public List<PosTListDTO> findAllPostsWithBase64Thumbnail() {
        List<Post> posts = findAll().stream().limit(36).collect(Collectors.toList());
        List<PosTListDTO> dtoList = new ArrayList<>();

        for (Post post : posts) {
            String image = findImagename(post.getPost_no()).getPost_image();
            String imagePath = thumbnail_dir + image;
            String base64String = null;
            try {
                byte[] fileBytes = readBytesFromFile(imagePath);
                base64String = encodeBytesToBase64(fileBytes);
            } catch (IOException e) {
                // 예외 처리
            }
            PosTListDTO dto = PosTListDTO
                    .builder()
                    .post_no(post.getPost_no())
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
}
