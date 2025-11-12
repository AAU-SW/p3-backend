package aau.sw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aau.sw.aspect.LogExecution;
import aau.sw.model.Order;
import aau.sw.repository.OrderRepository;
import aau.sw.service.OrderService;

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
    @LogExecution("Fetched all Orders")
    public List<Order> getOrders() {
        return orderRepository.findAll();

    }

    @GetMapping("/{id}")
    @LogExecution("Fetched Order by ID: ")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElse(ResponseEntity.notFound().build());
    }
    
    
}
