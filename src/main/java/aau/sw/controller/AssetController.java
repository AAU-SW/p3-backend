package aau.sw.controller;

import aau.sw.model.Asset;
import aau.sw.dto.AssetReq;
import aau.sw.repository.AssetRepository;
import jakarta.validation.Valid;
import aau.sw.service.AssetService;
import aau.sw.aspect.LogExecution;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    @Autowired
    private AssetRepository assetRepository;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<Asset> createAsset(@Valid @RequestBody AssetReq req) {
        Asset newAsset = new Asset();
        newAsset.setName(req.name());
        newAsset.setStatus(req.status());
        newAsset.setRegistrationNumber(req.registrationNumber());
        Asset created = assetService.createAsset(newAsset);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @LogExecution("Fetch all asset")
    public List<Asset> getAssets() {
        return assetRepository.findAll();
    }

    // read asset by id
    @GetMapping("/{id}")
    @LogExecution("Fetched asset by ID")
    public ResponseEntity<Asset> getAssetById(@PathVariable String id) {
        return assetRepository.findById(id)
                .map(asset -> ResponseEntity.ok().body(asset))
                .orElse(ResponseEntity.notFound().build());
    }

    // update asset
    @PutMapping("/{id}")
    @LogExecution("Updated asset")
    public ResponseEntity<Asset> updateAsset(@PathVariable String id, @RequestBody Asset updatedAsset) {
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
    }

    // delete asset
    @DeleteMapping("/{id}")
    @LogExecution("Deleted asset")
    public ResponseEntity<Void> deleteAsset(@PathVariable String id) {
        if (!assetRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
