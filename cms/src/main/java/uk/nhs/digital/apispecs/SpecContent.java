package uk.nhs.digital.apispecs;

public class SpecContent {

    private String id;
    private String name;
    private String modified;
    private String self;

    public SpecContent(String id, String name, String modified, String self) {
        this.id = id;
        this.name = name;
        this.modified = modified;
        this.self = self;
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
