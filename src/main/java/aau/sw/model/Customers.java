package aau.sw.model;

import org.springframework.data.annotation.Id;

public class Customers {
    @Id
    private String id;
    private String name;

    public Customers(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
