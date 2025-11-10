package aau.sw.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * Test profile configuration supplying a Mockito-based MongoTemplate so the
 * Spring Boot test context can start without a running MongoDB instance.
 */
@Configuration
@Profile("test")
public class TestMongoConfig {

    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate() {
        return Mockito.mock(MongoTemplate.class);
    }

    @Bean(name = "mongoMappingContext")
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }
}
