package aau.sw.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "images")
public class Image {

    @Id
    private String id;

    private String fileExtension;
    private String fileTitle;

    @CreatedDate
    private Date createdAt;

    // reference to the asset/case this image belongs to
    @DBRef
    private String connectedAssetId;
    @DBRef
    private Case connectedCaseId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getConnectedAssetId() {
        return connectedAssetId;
    }

    public void setConnectedAssetId(String connectedAssetId) {
        this.connectedAssetId = connectedAssetId;
    }

    public Case getConnectedCaseId() {
        return connectedCaseId;
    }

    public void setConnectedCaseId(Case connectedCaseId) {
        this.connectedCaseId = connectedCaseId;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}