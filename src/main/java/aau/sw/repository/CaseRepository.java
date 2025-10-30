package aau.sw.repository;

import aau.sw.model.Case;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CaseRepository extends MongoRepository<Case, String> {
    List<Case> findByAssetId_Id(String assetId);
}