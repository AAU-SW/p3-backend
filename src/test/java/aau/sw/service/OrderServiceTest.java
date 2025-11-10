package aau.sw.service;

import aau.sw.model.Order;
import aau.sw.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private AuditableService auditableService;

    @Test
    @DisplayName("createOrder sets createdBy then saves and returns saved entity")
    void createOrder_success() {
        Order input = new Order();
        Order saved = new Order();
        saved.setId("id123");

        when(orderRepository.save(input)).thenReturn(saved);

        OrderService service = new OrderService(orderRepository, auditableService);
        Order result = service.createOrder(input);

        assertThat(result).isSameAs(saved);

        // Verify order of interactions: auditableService first, then repository save
        InOrder inOrder = inOrder(auditableService, orderRepository);
        inOrder.verify(auditableService).setCreatedBy(input);
        inOrder.verify(orderRepository).save(input);
        verifyNoMoreInteractions(auditableService, orderRepository);
    }

    @Test
    @DisplayName("createOrder propagates exception from repository.save")
    void createOrder_repositoryThrows() {
        Order input = new Order();
    RuntimeException boom = new RuntimeException("DB down");
    when(auditableService.setCreatedBy(input)).thenReturn(input);
    when(orderRepository.save(input)).thenThrow(boom);

        OrderService service = new OrderService(orderRepository, auditableService);
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> service.createOrder(input));
        assertThat(thrown).isSameAs(boom);

        InOrder inOrder = inOrder(auditableService, orderRepository);
        inOrder.verify(auditableService).setCreatedBy(input);
        inOrder.verify(orderRepository).save(input);
    }
}
