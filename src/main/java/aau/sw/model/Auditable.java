package aau.sw.model;


public interface Auditable {
    void setCreatedBy(User createdBy);
    User getCreatedBy(); 
}