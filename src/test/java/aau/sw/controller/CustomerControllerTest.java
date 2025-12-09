package aau.sw.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import aau.sw.model.Customer;
import aau.sw.service.AuditableService;
import aau.sw.service.CustomerService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

  @Mock
  private CustomerService customerService;

  @Mock
  private AuditableService auditableService; // only needed because CustomerService uses it; stays unused here

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    CustomerController controller = new CustomerController(customerService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  @DisplayName("POST /api/customers creates a customer and returns 201 + body")
  void createCustomer_success() throws Exception {
    //Arrange
    Customer expected = new Customer("test name");
    expected.setId("generated-id");

    when(customerService.createCustomer(any(Customer.class))).thenReturn(
      expected
    );

    // Act
    mockMvc
      .perform(
        post("/api/customers")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(expected))
      )
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").value("generated-id"))
      .andExpect(jsonPath("$.name").value("test name"));

    verify(customerService).createCustomer(any(Customer.class));
  }

  @Test
  @DisplayName("GET /api/customers returns list of customers")
  void getCustomers_success() throws Exception {
    // Arrange
    Customer c1 = new Customer("test name 1");
    c1.setId("id1");
    Customer c2 = new Customer("test name 2");
    c2.setId("id2");
    List<Customer> customers = Arrays.asList(c1, c2);

    when(customerService.getCustomers()).thenReturn(customers);

    // Act & Assert
    mockMvc
      .perform(get("/api/customers"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].id").value("id1"))
      .andExpect(jsonPath("$[0].name").value("test name 1"))
      .andExpect(jsonPath("$[1].id").value("id2"))
      .andExpect(jsonPath("$[1].name").value("test name 2"));

    verify(customerService).getCustomers();
  }

  @Test
  @DisplayName("GET /api/customers returns empty list when none exist")
  void getCustomers_empty() throws Exception {
    // Arrange
    when(customerService.getCustomers()).thenReturn(Collections.emptyList());

    // Act & Assert
    mockMvc
      .perform(get("/api/customers"))
      .andExpect(status().isOk())
      .andExpect(content().json("[]"));

    verify(customerService).getCustomers();
  }
}
