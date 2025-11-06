package aau.sw.controller;

import aau.sw.aspect.LogExecution;
import aau.sw.model.Order;
import aau.sw.repository.OrderRepository;
import aau.sw.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping
    @LogExecution("Created new Order")
    public ResponseEntity<Order> createOrder(@RequestBody Order newOrder) {
        Order created = orderService.createOrder(newOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderRepository.findAll();

    }
}
