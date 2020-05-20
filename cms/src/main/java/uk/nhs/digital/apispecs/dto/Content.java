package uk.nhs.digital.apispecs.dto;

public class Content {

    private String id;
    private String name;
    private String modified;
    private String self;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModified() {
        return modified;
    }

    public String getSelf() {
        return self;
    }
}
