package aau.sw.config;

import aau.sw.repository.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile("test")
public class TestBeansConfig {

    @Bean
    public AssetRepository assetRepository() { return Mockito.mock(AssetRepository.class); }

    @Bean
    public CaseRepository caseRepository() { return Mockito.mock(CaseRepository.class); }

    @Bean
    public CustomerRepository customerRepository() { return Mockito.mock(CustomerRepository.class); }

    @Bean
    public ImageRepository imageRepository() { return Mockito.mock(ImageRepository.class); }

    @Bean
    public OrderRepository orderRepository() { return Mockito.mock(OrderRepository.class); }

    @Bean
    public UserRepository userRepository() { return Mockito.mock(UserRepository.class); }

    @Bean
    public S3Client s3Client() { return Mockito.mock(S3Client.class); }
}
