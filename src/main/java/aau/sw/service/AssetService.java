package aau.sw.service;


import aau.sw.model.Asset;
import aau.sw.model.Image;
import aau.sw.model.Order;
import aau.sw.repository.AssetRepository;
import aau.sw.repository.ImageRepository;
import aau.sw.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final AuditableService auditableService;
    private final ImageRepository imageRepository;
    private final FileService fileService;
    private final OrderRepository orderRepository;

    public AssetService(AssetRepository assetRepository, 
                        AuditableService auditableService, 
                        ImageRepository imageRepository, 
                        FileService fileService,
                        OrderRepository orderRepository) {
        this.assetRepository = assetRepository;
        this.auditableService = auditableService;
        this.imageRepository = imageRepository;
        this.fileService = fileService;
        this.orderRepository = orderRepository;
    }

    public Asset createAsset(Asset asset){
        auditableService.setCreatedBy(asset); // Set creator 
        if (asset.getOrderRef() != null && asset.getOrderRef().getId() != null) {
            String orderId = asset.getOrderRef().getId();
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
                
            asset.setOrderRef(order);
        }

        return assetRepository.save(asset);   // save to MongoDB
    }

    public Asset createAssetWithImage(Asset asset, MultipartFile imageFile) throws IOException {
        auditableService.setCreatedBy(asset);
        if (asset.getOrderRef() != null && asset.getOrderRef().getId() != null) {
            String orderId = asset.getOrderRef().getId();
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
                
            asset.setOrderRef(order);
        }


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
