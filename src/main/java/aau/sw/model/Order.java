package aau.sw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order implements Auditable {

    @Id
    private String id;
    private String orderNumber;
    private String name;
    private String product;
    private String notes;

    @DBRef
    private Customers connectedCustomer;

    @DBRef
    private User createdBy;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Customers getConnectedCustomer() {
        return connectedCustomer;
    }

    public void setConnectedCustomer(Customers connectedCustomer) {
        this.connectedCustomer = connectedCustomer;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

}
