package aau.sw.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "case")
public class Case {

    @Id
    public String id;
    public String title;
    public String status;
    public String description;
    public String location;


}
