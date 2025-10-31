package aau.sw.controller;

import aau.sw.model.Order;
import aau.sw.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;


    @PostMapping
    public Order createOrder(@RequestBody Order newOrder) {
        return orderRepository.save(newOrder);
    }

    @GetMapping
    public List<Order> getOrders() {
        return orderRepository.findAll();

    }
}
