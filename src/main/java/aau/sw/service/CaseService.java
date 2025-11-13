package aau.sw.service;

import aau.sw.model.Case;
import aau.sw.model.Image;
import aau.sw.repository.CaseRepository;
import aau.sw.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CaseService {
    private final CaseRepository caseRepository;
    private final AuditableService auditableService;
    private final ImageRepository imageRepository;
    private final FileService fileService;

    public CaseService(CaseRepository caseRepository, AuditableService auditableService, ImageRepository imageRepository, FileService fileService) {
        this.caseRepository = caseRepository;
        this.auditableService = auditableService;
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }
    
    public Case createCase(Case newCase){
        auditableService.setCreatedBy(newCase);
        return caseRepository.save(newCase);
    }

    public Image uploadFileToCase(Case connectedCaseId, MultipartFile imageFile) throws IOException {
        String fileExtension = getFileExtension(imageFile.getOriginalFilename());
        Image image = new Image();
        image.setFileExtension(fileExtension);
        image.setFileTitle(imageFile.getOriginalFilename());
        image.setConnectedCaseId(connectedCaseId);
        image = imageRepository.save(image);

        String imageId = image.getId();

        String objectKey = imageId + fileExtension;
        fileService.putObject(imageFile.getBytes(), fileService.getBucketName(), objectKey);
        return image;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
