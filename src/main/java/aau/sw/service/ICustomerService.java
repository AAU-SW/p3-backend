package aau.sw.service;

import java.util.List;

import aau.sw.model.Customer;

public interface ICustomerService {
    Customer createCustomer(Customer newCustomer);
    List<Customer> getCustomers();
    Customer getCustomerById(String id);
    Customer updateCustomer(String id, Customer updatedCustomer);
    boolean deleteCustomer(String id);
}
