package com.social.innerPeace.board.post.component;

import com.social.innerPeace.dto.WriteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    @Value("${upload.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public WriteDTO storeFile(WriteDTO writeDTO) throws IOException{
        if (writeDTO == null) {
            return null;
        }
        String post_image = writeDTO.getPost_image().getOriginalFilename();
        String list_image = createStoreFileName(post_image);
        String fullpath = getFullPath(list_image);
        writeDTO.getPost_image().transferTo(new File(fullpath));
        return writeDTO;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
