package aau.sw.controller;

import aau.sw.model.Asset;
import aau.sw.repository.AssetRepository;
import aau.sw.service.AssetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;
    private final ObjectMapper objectMapper;

    @Autowired
    private AssetRepository assetRepository;


    public AssetController(AssetService assetService, ObjectMapper objectMapper) {
        this.assetService = assetService;
        this.objectMapper = objectMapper;
    }

    // create asset
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAssetJson(@RequestBody Asset asset) {
        Asset savedAsset = assetService.createAsset(asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAsset);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAssetMultipart(
            @RequestPart("asset") String assetJson,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            Asset asset = objectMapper.readValue(assetJson, Asset.class);

            if (image != null && !image.isEmpty()) {
                Asset savedAsset = assetService.createAssetWithImage(asset, image);
                return ResponseEntity.status(HttpStatus.CREATED).body(savedAsset);
            }
            Asset savedAsset = assetService.createAsset(asset);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAsset);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create asset: " + e.getMessage());
        }
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
    public void updateAsset(@PathVariable String id, @RequestBody Asset updatedAsset) {
        assetRepository.findById(id).ifPresent(asset -> {
            asset.setName(updatedAsset.getName());
            asset.setDescription(updatedAsset.getDescription());
            asset.setStatus(updatedAsset.getStatus());
            asset.setRegistrationNumber(updatedAsset.getRegistrationNumber());
            assetRepository.save(asset);
        });
    }

    // delete asset
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String id) {
        if (!assetRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}