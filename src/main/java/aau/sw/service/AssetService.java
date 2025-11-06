package aau.sw.service;


import aau.sw.model.Asset;
import aau.sw.model.Image;
import aau.sw.repository.AssetRepository;
import aau.sw.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final AuditableService auditableService;
    private final ImageRepository imageRepository;
    private final FileService fileService;

    public AssetService(AssetRepository assetRepository, AuditableService auditableService, ImageRepository imageRepository, FileService fileService) {
        this.assetRepository = assetRepository;
        this.auditableService = auditableService;
        this.imageRepository = imageRepository;
        this.fileService = fileService;
    }

    public Asset createAsset(Asset asset){
        auditableService.setCreatedBy(asset); // Set creator 
        return assetRepository.save(asset);   // save to MongoDB
    }

    public Asset createAssetWithImage(Asset asset, MultipartFile imageFile) throws IOException {
        auditableService.setCreatedBy(asset);

        String fileExtension = getFileExtension(imageFile.getOriginalFilename());

        Image image = new Image();
        image.setFileExtension(fileExtension);
        image = imageRepository.save(image);

        String imageId = image.getId();


        String objectKey = imageId + fileExtension;
        fileService.putObject(imageFile.getBytes(), fileService.getBucketName(), objectKey);

        asset.setProfilePicture(image);
        Asset savedAsset = assetRepository.save(asset);

        return savedAsset;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}
