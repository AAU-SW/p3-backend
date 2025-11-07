package aau.sw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3ClientConfig {

    @Value("${DO_SPACE_KEY}")
    private String doSpaceKey;

    @Value("${DO_SPACE_SECRET}")
    private String doSpaceSecret;

    @Value("${DO_SPACE_ENDPOINT}")
    private String doSpaceEndpoint;

    @Value("${DO_SPACE_REGION}")
    private String doSpaceRegion;

    @Bean
    public S3Client getS3() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(doSpaceKey, doSpaceSecret);

        return S3Client.builder()
                .endpointOverride(URI.create(doSpaceEndpoint))   // DigitalOcean Spaces endpoint
                .region(Region.of(doSpaceRegion))               // e.g., "nyc3"
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)           // required for DO Spaces
                        .build())
                .build();
    }
}
