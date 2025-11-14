package aau.sw.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import aau.sw.aspect.LogExecution;
import aau.sw.model.Asset;
import aau.sw.repository.AssetRepository;
import aau.sw.service.AssetService;

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
    @LogExecution("Created asset") 
    public ResponseEntity<Asset> createAssetJson(@RequestBody Asset asset) {
        Asset savedAsset = assetService.createAsset(asset);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAsset);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @LogExecution("Created asset with multipart data: ")
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
                    .body(e.getMessage());
        }
    }

    // read all assets
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
    // read asset by Order id
    @GetMapping("/order/{id}")
    @LogExecution("Fetched asset by order ID")
    public ResponseEntity<List<Asset>> getAssetByOrderId(@PathVariable String id) {
        List<Asset> assets = assetRepository.findByOrderRef_Id(id);
        if (assets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(assets);
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
