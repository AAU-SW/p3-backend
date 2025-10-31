package aau.sw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {

    @Id
    private String id;
    private String orderNumber;

    @DBRef
    private Customers connectedCustomer;

    public Order(){
    }

    public Order(String orderNumber, Customers connectedCustomer ){
        this.orderNumber = orderNumber;
        this.connectedCustomer = connectedCustomer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Customers getConnectedCustomer() {
        return connectedCustomer;
    }

    public void setConnectedCustomer(Customers connectedCustomer) {
        this.connectedCustomer = connectedCustomer;
    }


}
