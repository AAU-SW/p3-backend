package aau.sw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
public class Image {

    @Id
    private String id; // Mongo ObjectId

    private String fileExtension;

    // reference to the asset/case this image belongs to
    @DBRef
    private String connectedAssetId;
    @DBRef
    private String connectedCaseId;


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

    public String getConnectedCaseId() {
        return connectedCaseId;
    }

    public void setConnectedCaseId(String connectedCaseId) {
        this.connectedCaseId = connectedCaseId;
    }
}