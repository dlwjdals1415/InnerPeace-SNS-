package com.social.innerPeace.board.post.component;


import com.social.innerPeace.dto.PostDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${upload.dir}")
    private String fileDir;

    @Value("${thumbnail.dir}")
    private String fileDir2;


    public PostDTO storeFile(PostDTO postDTO) throws IOException{
        if (postDTO == null) {
            return null;
        }
        //이미지 이름
        String uuid = UUID.randomUUID().toString();
        //이미지저장후 이미지 이름만 dto에저장
        String post_image = postDTO.getPost_image_file().getOriginalFilename();
        String filename = createStoreFileName(post_image,uuid);
        postDTO.getPost_image_file().transferTo(new File(fileDir + filename));
        postDTO.setPost_image(filename);
        //썸네일저장후 썸네일 이 름만 dto에 저장
        byte[] base64Bytes = postDTO.getPost_image_thumbnail().getBytes();
        String base64ImageString = new String(base64Bytes, "UTF-8");
        base64ImageString = base64ImageString.replace("data:image/png;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);

        try (FileOutputStream fos = new FileOutputStream(fileDir2+filename)) {
            fos.write(imageBytes); // 파일에 바이트 배열 쓰기
        }
        return postDTO;
    }

    private String createStoreFileName(String originalFilename,String uuid) {
        return uuid +"."+ extractExt(originalFilename);
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
