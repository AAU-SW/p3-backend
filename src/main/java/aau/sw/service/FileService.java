package aau.sw.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URI;
import java.time.Duration;

@Service
public class FileService {

    private final S3Client s3Client;

    public FileService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getDoSpaceRegion() {
        return doSpaceRegion;
    }

    public void setDoSpaceRegion(String doSpaceRegion) {
        this.doSpaceRegion = doSpaceRegion;
    }

    @Value("${DO_SPACE_BUCKET}")
    private String bucketName;

    @Value("${DO_SPACE_KEY}")
    private String doSpaceKey;

    @Value("${DO_SPACE_SECRET}")
    private String doSpaceSecret;

    @Value("${DO_SPACE_ENDPOINT}")
    private String doSpaceEndpoint;

    @Value("${DO_SPACE_REGION}")
    private String doSpaceRegion;

    public void putObject(byte[] data, String bucketName, String objectKey) {
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build(),
                    RequestBody.fromBytes(data));
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public String getFileUrl(String objectKey) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(doSpaceKey, doSpaceSecret);


        try (S3Presigner presigner = S3Presigner.builder()
                .endpointOverride(URI.create(doSpaceEndpoint))   // DigitalOcean Spaces endpoint
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(doSpaceRegion))
                .build()) {

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .getObjectRequest(getObjectRequest)
                    .signatureDuration(Duration.ofMinutes(60)) // URL valid for 60 min
                    .build();

            return presigner.presignGetObject(presignRequest).url().toString();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to generate pre-signed URL", e);
        }
    }

}
