package aau.sw.controller;
import aau.sw.model.Asset;
import aau.sw.repository.AssetRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/api/assets")
public class AssetController {
    @Autowired
    private AssetRepository assetRepository;
  
    @PostMapping
    public Asset createAsset(@RequestBody Asset newAsset) {
        return assetRepository.save(newAsset);
    }

    @GetMapping
    public List<Asset> getAssets(){
        return assetRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable String id) {
        if (!assetRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assetRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
