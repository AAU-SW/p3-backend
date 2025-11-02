package aau.sw.controller;

import aau.sw.model.Asset;
import aau.sw.repository.AssetRepository;
import aau.sw.service.AssetService;
import aau.sw.service.LoggingService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final LoggingService loggingService;
    private final AssetService assetService;

    @Autowired
    private AssetRepository assetRepository;
  
    public AssetController(LoggingService loggingService, AssetService assetService) {
        this.assetService = assetService;
        this.loggingService = loggingService;
    }

    // create asset
    @PostMapping
    public ResponseEntity<Asset> createAsset(@RequestBody Asset newAsset) {
        return loggingService.logExecution(
            ()-> {
           Asset created = assetService.createAsset(newAsset);
           return ResponseEntity.status(HttpStatus.CREATED).
                        body(created);
                    },
                    "Created new asset: " + newAsset.getName()
           ); 
    }

    // read asset
    @GetMapping
    public List<Asset> getAssets() {
        return assetRepository.findAll();
    }

    // read asset by id
    @GetMapping("/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable String id) {
        return assetRepository.findById(id)
                .map(asset -> ResponseEntity.ok().body(asset))
                .orElse(ResponseEntity.notFound().build());
    }

    // update asset
    @PutMapping("/{id}")
    public ResponseEntity<Asset> updateAsset(@PathVariable String id, @RequestBody Asset updatedAsset) {
        return loggingService.logExecution(
            () -> {
                return assetRepository.findById(id)
                .map(asset -> {
                    asset.setName(updatedAsset.getName());
                    asset.setDescription(updatedAsset.getDescription());
                    asset.setStatus(updatedAsset.getStatus());
                    asset.setRegistrationNumber(updatedAsset.getRegistrationNumber());
                    assetRepository.save(asset);
                    return ResponseEntity.ok(asset);
                })
                .orElse(ResponseEntity.notFound().build());
            },
            "Update asset whit ID: " + id
        );
    }

    // delete asset
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String id) {
        return loggingService.logExecution(
            () -> {
                if (!assetRepository.existsById(id)) {
                    return ResponseEntity.notFound().build();
                }
                assetRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            },
            "Deleted asset whit ID " + id
        );
    }
}
