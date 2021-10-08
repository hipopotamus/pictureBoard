package hipo.pictureboard.service;

import hipo.pictureboard.domain.Img;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    public String getFullPath(String filename, String path) {
        return path + filename;
    }

    public Img storeFile(MultipartFile multipartFile, String path) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName, path)));
        return new Img(originalFileName, storeFileName);
    }

    private String createStoreFileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        String ext = extracted(originalFileName);
        return uuid + "." + ext;
    }

    private String extracted(String originalFileName) {
        int pos = originalFileName.lastIndexOf(".");
        return originalFileName.substring(pos + 1);
    }
}
