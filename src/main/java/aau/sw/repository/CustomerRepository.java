package aau.sw.repository;

import aau.sw.model.Customers;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customers, String> {
    
}
