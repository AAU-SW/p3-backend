package aau.sw.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "cases")
public class Case implements Auditable {

        public enum Status {
        ACTIVE,
        CLOSED
    }

    @Id
    private String id;
    private String title;
    private Status status;
    private String description;
    private String location;

    @DBRef
    private User assignedTo;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private Date deletedAt;

    @DBRef
    private User createdBy;

    @DBRef
    private Asset assetId;

    @DBRef
    private Customers connectedCustomer;

    public Case() {
    }

    public Case(String title, Status status, String description, String location) {
        this.title = title;
        this.status = status;
        this.description = description;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Asset getAssetId() {
        return assetId;
    }

    public void setAssetId(Asset assetId) {
        this.assetId = assetId;
    }

    public Customers getConnectedCustomer() {
        return connectedCustomer;
    }

    public void setConnectedCustomer(Customers connectedCustomer) {
        this.connectedCustomer = connectedCustomer;
    }
}
