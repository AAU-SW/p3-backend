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

import aau.sw.aspect.LogExecution;
import aau.sw.model.Customers;
import aau.sw.repository.CustomerRepository;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    // create customer
    @PostMapping
    @LogExecution("Creating a new customer")
    public Customers createCustomer(@RequestBody Customers newCustomer) {
        return customerRepository.save(newCustomer);
    }

    // read all customers
    @GetMapping
    @LogExecution("Fetching all customers")
    public List<Customers> getCustomers(){
        return customerRepository.findAll();
    }

    // read customer by id
    @GetMapping("/{id}")
    @LogExecution("Fetching customer by ID: ")
    public ResponseEntity<Customers> getCustomerById(@PathVariable String id) {
        return customerRepository.findById(id)
                .map(customer -> ResponseEntity.ok().body(customer))
                .orElse(ResponseEntity.notFound().build());
    }

    // update customer by id
    @PutMapping("/{id}")
    @LogExecution("Updating customer by ID: ")
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
    @LogExecution("Deleting customer by ID: ")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        if (!customerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        customerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
