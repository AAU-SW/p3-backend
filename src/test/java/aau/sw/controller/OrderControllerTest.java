package aau.sw.controller;

import aau.sw.model.Order;
import aau.sw.repository.OrderRepository;
import aau.sw.service.OrderService;
import aau.sw.service.AuditableService;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AuditableService auditableService; // only needed because OrderService uses it; stays unused here

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        OrderController controller = new OrderController(orderService);
        // Inject the mocked repository into the private field
        ReflectionTestUtils.setField(controller, "orderRepository", orderRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("POST /api/orders creates an order and returns 201 + body")
    void createOrder_success() throws Exception {
        Order input = new Order();
        input.setOrderNumber("ORD-123");

        Order saved = new Order();
        saved.setId("generated-id");
        saved.setOrderNumber(input.getOrderNumber());

        when(orderService.createOrder(any(Order.class))).thenReturn(saved);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value("generated-id"))
            .andExpect(jsonPath("$.orderNumber").value("ORD-123"));

        verify(orderService).createOrder(any(Order.class));
        verifyNoInteractions(orderRepository); // create path doesn't call repository directly in controller
    }

    @Test
    @DisplayName("GET /api/orders returns list of orders")
    void getOrders_success() throws Exception {
        Order o1 = new Order();
        o1.setId("id1");
        o1.setOrderNumber("ORD-1");
        Order o2 = new Order();
        o2.setId("id2");
        o2.setOrderNumber("ORD-2");
        List<Order> orders = Arrays.asList(o1, o2);

        when(orderRepository.findAll()).thenReturn(orders);

        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value("id1"))
            .andExpect(jsonPath("$[0].orderNumber").value("ORD-1"))
            .andExpect(jsonPath("$[1].id").value("id2"))
            .andExpect(jsonPath("$[1].orderNumber").value("ORD-2"));

        verify(orderRepository).findAll();
        verifyNoInteractions(orderService); // controller method does not use service for GET
    }

    @Test
    @DisplayName("GET /api/orders returns empty list when none exist")
    void getOrders_empty() throws Exception {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/orders"))
            .andExpect(status().isOk())
            .andExpect(content().json("[]"));

        verify(orderRepository).findAll();
    }
}
