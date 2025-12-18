package aau.sw.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;



@Document(collection = "assets")
public class Asset implements Auditable {
    public Asset(AssetDto assetDto) {
        this.name = assetDto.name();
        this.registrationNumber = assetDto.registrationNumber();
        this.status = assetDto.status();
        this.profilePicture = assetDto.profilePicture();
        this.orderRef = assetDto.orderRef();
    }

    public enum Status {
        ACTIVE,
        CLOSED
    }

    @Id
    private String id;
    private String name;
    private String registrationNumber;
    private Status status;
    private String description;

    private Date lastInvoiced;

    @DBRef
    private Image profilePicture;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;

    private Date deletedAt;

    @DBRef
    private User createdBy;

    @DBRef
    private Order orderRef;

    public Asset() {
    }

    public Asset(String name, Status status, String description, String registrationNumber, Image profilePicture) {
        this.name = name;
        this.status = status;
        this.description = description;
        this.registrationNumber = registrationNumber;
        this.profilePicture = profilePicture;
    }

    public void close() {
      this.status = Status.CLOSED;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    @Override
    public User getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Order getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(Order orderRef) {
        this.orderRef = orderRef;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Date getLastInvoiced() {
        return lastInvoiced;
    }

    public void setLastInvoiced(Date lastInvoiced) {
        this.lastInvoiced = lastInvoiced;
    }


}
