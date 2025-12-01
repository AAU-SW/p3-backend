package aau.sw.service;

import java.util.List;

import aau.sw.model.Customer;
import aau.sw.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer newCustomer) {
        return customerRepository.save(newCustomer);
    }
    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
    @Override
    public Customer getCustomerById(String id) {
        var customer = customerRepository.findById(id);
        return customer.orElse(null);
    }
    @Override
    public Customer updateCustomer(String id, Customer updatedCustomer) {
        if (!customerRepository.existsById(id)) {
            return null;
        }
        updatedCustomer.setId(id);
        customerRepository.save(updatedCustomer);

        return updatedCustomer;
    }
    @Override
    public boolean deleteCustomer(String id) {
        if (!customerRepository.existsById(id)) {
            return false;
        }
        customerRepository.deleteById(id);
        return true;
    }
}
