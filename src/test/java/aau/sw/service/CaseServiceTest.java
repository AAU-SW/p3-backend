package aau.sw.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import aau.sw.model.Case;
import aau.sw.repository.CaseRepository;
import aau.sw.repository.ImageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CaseServiceTest {

  @Mock
  private CaseRepository caseRepository;

  @Mock
  private AuditableService auditableService;

  @Mock
  private ImageRepository imageRepository;

  @Mock
  private FileService fileService;

  @Test
  @DisplayName("createCase sets createdBy then saves and returns saved entity")
  void createCase_success() {
    // Arrange
    Case input = new Case();
    Case expected = new Case();
    expected.setId("id123");

    when(caseRepository.save(input)).thenReturn(expected);

    CaseService service = new CaseService(
      caseRepository,
      auditableService,
      imageRepository,
      fileService
    );

    // Act
    Case actual = service.createCase(input);

    assertThat(actual).isSameAs(expected);
  }

  @Test
  @DisplayName("createOrder propagates exception from repository.save")
  void createCase_repositoryThrows() {
    // Arrange
    Case input = new Case();
    RuntimeException expected = new RuntimeException("DB down");
    when(auditableService.setCreatedBy(input)).thenReturn(input);
    when(caseRepository.save(input)).thenThrow(expected);

    CaseService service = new CaseService(
      caseRepository,
      auditableService,
      imageRepository,
      fileService
    );

    // Act
    RuntimeException thrown = assertThrows(RuntimeException.class, () ->
      service.createCase(input)
    );

    // Assert
    assertThat(thrown).isSameAs(expected);
  }
}
