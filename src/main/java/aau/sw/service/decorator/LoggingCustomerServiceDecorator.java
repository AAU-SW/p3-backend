package aau.sw.service.decorator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import aau.sw.model.Customer;
import aau.sw.service.CustomerService;
import aau.sw.service.ICustomerService;

@Primary
@Service
public class LoggingCustomerServiceDecorator implements ICustomerService {

    private final CustomerService decoratedService;
    private static final Logger log = LoggerFactory.getLogger(
            LoggingCustomerServiceDecorator.class);

    // Use constructor injection to provide the service to be decorated
    public LoggingCustomerServiceDecorator(
            @Qualifier("customerService") CustomerService decoratedService) {
        this.decoratedService = decoratedService;
    }

    @Override
    public Customer createCustomer(Customer newCustomer) {
        var result = decoratedService.createCustomer(newCustomer);

        log.info("Customer created successfully.");
        return result;
    }

    @Override
    public List<Customer> getCustomers() {
        var result = decoratedService.getCustomers();
        log.info("Fetched all customers. Count: {}", result.size());
        return result;
    }

    @Override
    public Customer getCustomerById(String id) {
        var result = decoratedService.getCustomerById(id);

        if (result != null) {
            log.info("Fetched customer with ID: {}", id);
        } else {
            log.info("Customer with ID: {} not found.", id);
        }

        return result;
    }

    @Override
    public Customer updateCustomer(String id, Customer updatedCustomer) {
        var result = decoratedService.updateCustomer(id, updatedCustomer);

        if (result != null) {
            log.info("Updated customer with ID: {}", id);
        } else {
            log.info("Failed to update. Customer with ID: {} not found.", id);
        }

        return result;
    }

    @Override
    public boolean deleteCustomer(String id) {
        var result = decoratedService.deleteCustomer(id);
        log.info("Deleted customer with ID: {}", id);
        return result;
    }
}
