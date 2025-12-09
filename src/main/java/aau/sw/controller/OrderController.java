package aau.sw.controller;

import aau.sw.aspect.LogExecution;
import aau.sw.model.Order;
import aau.sw.repository.AssetRepository;
import aau.sw.repository.OrderRepository;
import aau.sw.service.OrderService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private AssetRepository assetRepository;

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
    return orderRepository
      .findById(id)
      .map(order -> ResponseEntity.ok().body(order))
      .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  @LogExecution("Updated order: ")
  public ResponseEntity<Order> updateOrder(
    @PathVariable String id,
    @RequestBody Order updatedOrder
  ) {
    return orderRepository
      .findById(id)
      .map(order -> {
        order.setConnectedCustomer(updatedOrder.getConnectedCustomer());
        order.setName(updatedOrder.getName());
        order.setNotes(updatedOrder.getNotes());
        order.setProduct(updatedOrder.getProduct());
        order.setOrderNumber(updatedOrder.getOrderNumber());
        orderRepository.save(order);
        return ResponseEntity.ok(order);
      })
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @LogExecution("Deleted order")
  public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
    if (!orderRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    assetRepository
      .findByOrderRef_Id(id)
      .forEach(asset -> {
        asset.setOrderRef(null);
        assetRepository.save(asset);
      });
    orderRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
