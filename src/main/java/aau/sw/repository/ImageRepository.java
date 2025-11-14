package aau.sw.repository;

import aau.sw.model.Case;
import aau.sw.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {
    List<Image> findByConnectedCaseId(Case connectedCaseId);
}