package aau.sw.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import aau.sw.model.Case;
import aau.sw.repository.CaseRepository;
import aau.sw.service.AuditableService;
import aau.sw.service.CaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CaseControllerTest {

  @Mock
  private CaseService caseService;

  @Mock
  private CaseRepository caseRepository;

  @Mock
  private AuditableService auditableService; // only needed because CaseService uses it; stays unused here

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    CaseController controller = new CaseController(caseService);
    // Inject the mocked repository into the private field
    ReflectionTestUtils.setField(controller, "caseRepository", caseRepository);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("POST /api/cases creates a case and returns 201 + body")
  void createCase_success() throws Exception {
    //Arrange
    Case expected = new Case();
    expected.setDescription("test description");
    expected.setId("generated-id");

    when(caseService.createCase(any(Case.class))).thenReturn(expected);

    // Act
    mockMvc
      .perform(
        post("/api/cases")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(expected))
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value("generated-id"))
      .andExpect(jsonPath("$.description").value("test description"));

    verify(caseService).createCase(any(Case.class));
    verifyNoInteractions(caseRepository); // create path doesn't call repository directly in controller
  }

  @Test
  @DisplayName("GET /api/cases returns list of cases")
  void getCases_success() throws Exception {
    // Arrange
    Case c1 = new Case();
    c1.setId("id1");
    c1.setDescription("test description 1");
    Case c2 = new Case();
    c2.setId("id2");
    c2.setDescription("test description 2");
    List<Case> cases = Arrays.asList(c1, c2);

    when(caseRepository.findAll()).thenReturn(cases);

    // Act & Assert
    mockMvc
      .perform(get("/api/cases"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value("id1"))
      .andExpect(jsonPath("$[0].description").value("test description 1"))
      .andExpect(jsonPath("$[1].id").value("id2"))
      .andExpect(jsonPath("$[1].description").value("test description 2"));

    verify(caseRepository).findAll();
    verifyNoInteractions(caseService); // controller method does not use service for GET
  }

  @Test
  @DisplayName("GET /api/cases returns empty list when none exist")
  void getCases_empty() throws Exception {
    when(caseRepository.findAll()).thenReturn(Collections.emptyList());

    mockMvc
      .perform(get("/api/cases"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));

    verify(caseRepository).findAll();
  }
}
