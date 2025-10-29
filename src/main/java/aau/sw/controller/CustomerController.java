package aau.sw.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aau.sw.model.Customers;
import aau.sw.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    // create customer
    @PostMapping
    public Customers createCustomer(@RequestBody Customers newCustomer) {
        return customerRepository.save(newCustomer);
    }

    // read all customers
    @GetMapping
    public List<Customers> getCustomers(){
        return customerRepository.findAll();
    }

    // read customer by id
    @GetMapping("/{id}")
    public ResponseEntity<Customers> getCustomerById(@PathVariable String id) {
        return customerRepository.findById(id)
                .map(customer -> ResponseEntity.ok().body(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    // update all customers
    @PutMapping
    public void updateCustomer(@RequestBody Customers updatedCustomer) {
        customerRepository.findById(updatedCustomer.getId()).ifPresent(customer -> {
            customer.setName(updatedCustomer.getName());
            customerRepository.save(customer);
        });
    }

    // update customer by id
    @PutMapping("/{id}")
    public ResponseEntity<Customers> updateCustomer(@PathVariable String id, @RequestBody Customers updatedCustomer) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedCustomer.setId(id);
        customerRepository.save(updatedCustomer);
        return ResponseEntity.ok().body(updatedCustomer);
    }

    // delete customer by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
