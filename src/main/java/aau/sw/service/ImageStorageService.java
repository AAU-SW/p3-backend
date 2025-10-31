package aau.sw.service;

import java.io.IOException;
import java.util.List;

import aau.sw.model.Image;
import org.springframework.web.multipart.MultipartFile;


public interface ImageStorageService {

    void saveFile(MultipartFile multipartFile) throws IOException;

    void deleteFile(Long id) throws Exception;

    List<Image> getImage();
}