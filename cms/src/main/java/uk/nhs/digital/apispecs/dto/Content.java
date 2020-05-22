package uk.nhs.digital.apispecs.dto;

import java.time.Instant;

public class Content {

    private String id;
    private String name;
    private Instant modified;
    private String self;

    public void setId(String id) {
        this.id = id;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getModified() {
        return modified;
    }

    public String getSelf() {
        return self;
    }
}
