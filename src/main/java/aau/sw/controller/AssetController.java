package aau.sw.controller;

import aau.sw.model.Asset;
import aau.sw.repository.AssetRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    @Autowired
    private AssetRepository assetRepository;

    @GetMapping
    public List<Asset> getAssets(){
        return assetRepository.findAll();

    @PostMapping
    public Asset createAsset(@RequestBody Asset newAsset) {
        return assetRepository.save(newAsset);
    }
}
