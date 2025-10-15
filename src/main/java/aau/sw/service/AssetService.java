package aau.sw.service;

import aau.sw.repository.AssetRepository;
import org.springframework.stereotype.Service;

@Service
public class AssetService {
    private final AssetRepository repo;
    public AssetService(AssetRepository repo) { this.repo = repo; }
}
