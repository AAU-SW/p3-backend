package aau.sw.controller;
import aau.sw.model.Asset;
import aau.sw.repository.AssetRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/assets")
public class AssetController {
    @Autowired
    private AssetRepository assetRepository;
  
    // create asset
    @PostMapping
    public Asset createAsset(@RequestBody Asset newAsset) {
        return assetRepository.save(newAsset);
    }
    
    // read asset
    @GetMapping
    public List<Asset> getAssets(){
        return assetRepository.findAll();
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
