package aau.sw.service;


import aau.sw.model.Asset;
import aau.sw.repository.AssetRepository;
import org.springframework.stereotype.Service;

@Service
public class AssetService {
    private final AssetRepository assetRepository;
    private final AuditableService auditableService;

    public AssetService(AssetRepository assetRepository, AuditableService auditableService) { 
        this.assetRepository = assetRepository;
        this.auditableService = auditableService;
    }

    public Asset createAsset(Asset asset){
        auditableService.setCreatedBy(asset); // Set creator 
        return assetRepository.save(asset);   // save to MongoDB
    }
}
