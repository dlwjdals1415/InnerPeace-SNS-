package com.social.innerPeace.board.post.component;

import com.social.innerPeace.dto.PostDTO;
import com.social.innerPeace.dto.WriteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${upload.dir}")
    private String fileDir;

    @Value("${thumbnail.dir}")
    private String fileDir2;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public String getFullPath2(String filename) {
        return fileDir2 + filename;
    }

    public WriteDTO storeFile(WriteDTO writeDTO) throws IOException{
        if (writeDTO == null) {
            return null;
        }
        //이미지 이름
        String uuid = UUID.randomUUID().toString();
        //이미지저장후 이미지 이름만 dto에저장
        String post_image = writeDTO.getPost_image().getOriginalFilename();
        String list_image = createStoreFileName(post_image,uuid);
        String fullpath = getFullPath(list_image);
        writeDTO.getPost_image().transferTo(new File(fullpath));
        writeDTO.setDb_post_image(list_image);
        //썸네일저장후 썸네일 이 름만 dto에 저장
        byte[] base64Bytes = writeDTO.getPost_image_thumbnail().getBytes();
        String base64ImageString = new String(base64Bytes, "UTF-8");
        base64ImageString = base64ImageString.replace("data:image/png;base64,", "");
        byte[] imageBytes = Base64.getDecoder().decode(base64ImageString);
        writeDTO.setPost_image_thumbnail(uuid+".png");

        try (FileOutputStream fos = new FileOutputStream(getFullPath2(uuid+".png"))) {
            fos.write(imageBytes); // 파일에 바이트 배열 쓰기
        }
        return writeDTO;
    }

    private String createStoreFileName(String originalFilename,String uuid) {
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
