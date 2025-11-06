package aau.sw.service;

import org.springframework.stereotype.Service;

import aau.sw.model.Order;
import aau.sw.repository.OrderRepository;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuditableService auditableService;

    public OrderService(OrderRepository orderRepository, AuditableService auditableService){
        this.orderRepository = orderRepository;
        this.auditableService = auditableService;
    }

    public Order createOrder(Order order){
        auditableService.setCreatedBy(order);
        return orderRepository.save(order);
    }
    
}
