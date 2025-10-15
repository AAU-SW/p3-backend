package aau.sw.controller;

import aau.sw.service.AssetService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
public class AssetController {
    private final AssetService svc;
    public AssetController(AssetService svc) { this.svc = svc; }
}
