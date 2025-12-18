package aau.sw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import aau.sw.model.Asset;
import aau.sw.model.Image;
import aau.sw.model.Order;
import aau.sw.repository.AssetRepository;
import aau.sw.repository.ImageRepository;
import aau.sw.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class AssetServiceTest {
    
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AuditableService auditableService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private FileService fileService;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private AssetService assetService;

    // Happy path test for createAsset method
    @Test
    @DisplayName("createAsset calls auditableService and assetRepository")
    void createAsset_success() {
        Asset inputAsset = new Asset();
        Order orderRef = new Order();
        orderRef.setId("order-id-123");
        inputAsset.setOrderRef(orderRef);

        Order foundOrder = new Order();
        foundOrder.setId("order-id-123");

        when(orderRepository.findById("order-id-123")).
        thenReturn(Optional.of(foundOrder));
        when(assetRepository.save(inputAsset)).thenReturn(inputAsset);

        Asset result = assetService.createAsset(inputAsset);

        verify(orderRepository).findById("order-id-123");
        verify(auditableService).setCreatedBy(inputAsset);
        verify(assetRepository).save(inputAsset);
        assertThat(result).isSameAs(inputAsset);

        assertThat(result.getOrderRef()).isEqualTo(foundOrder);
    };

    //sad path test for createAsset when order not found
    @Test
    @DisplayName("createAsset throws when order not found")
    void createAsset_orderNotFound() {
        Asset inputAsset = new Asset();
        Order orderRef = new Order();
        orderRef.setId("non-order-existing");
        inputAsset.setOrderRef(orderRef);

        when(orderRepository.findById("non-order-existing")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            assetService.createAsset(inputAsset);
        });
        assertThat(exception.getMessage()).isEqualTo("Order not found with id: non-order-existing");

        verify(assetRepository, never()).save(any());
    };


    // Happy path test for createAssetWithImage
    @Test
    @DisplayName("createAssetWithImage orchestrates DB save and File Upload correctly")
    void createAssetWithImage_success() throws IOException {
        Asset inputAsset = new Asset();

        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getOriginalFilename()).thenReturn("valed.png");
        when(mockFile.getBytes()).thenReturn(new byte[] {1,2,3});

        Image savedImage = new Image();
        savedImage.setId("image-123");
        savedImage.setFileExtension(".png");

        when(imageRepository.save(any(Image.class))).thenReturn(savedImage);

        when(assetRepository.save(any(Asset.class))).thenReturn(inputAsset);

        when(fileService.getBucketName()).thenReturn("test-bucket");

        assetService.createAssetWithImage(inputAsset, mockFile);

        verify(fileService).putObject(any(), eq("test-bucket"), eq("image-123.png"));
        verify(imageRepository).save(any(Image.class));

        verify(assetRepository).save(inputAsset);

        assertThat(inputAsset.getProfilePicture()).isEqualTo(savedImage);
    };

    //sad path test for createAssetWithImage when image file is null
    @Test
    @DisplayName("createAssetWithImage throws when image file is null")
    void createAssetWithImage_imageFileNull() {
        Asset inputAsset = new Asset();
        MultipartFile nullFile = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            assetService.createAssetWithImage(inputAsset, nullFile);
        });
        assertThat(exception.getMessage()).isEqualTo("Image file must not be null or empty");

        verify(imageRepository, never()).save(any());
        verify(fileService, never()).putObject(any(), any(), any());
        verify(assetRepository, never()).save(any());
    }
}
        
