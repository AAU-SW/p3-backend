package aau.sw.repository;

import aau.sw.model.Asset;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AssetRepository extends MongoRepository<Asset, String> {
    List<Asset> findByOrderRef_Id(String orderId); 
}
