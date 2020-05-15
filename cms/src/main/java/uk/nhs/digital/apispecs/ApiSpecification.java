package uk.nhs.digital.apispecs;

public class ApiSpecification {

    private static final String PROPERTY_NAME_HTML = "website:html";

    private DocumentLifecycleSupport documentLifecycleSupport;

    private ApiSpecification(final DocumentLifecycleSupport documentLifecycleSupport) {
        this.documentLifecycleSupport = documentLifecycleSupport;
    }

    public static ApiSpecification from(final DocumentLifecycleSupport documentLifecycleSupport) {
        return new ApiSpecification(documentLifecycleSupport);
    }

    public void setHtml(final String html) {
        editable().setProperty(PROPERTY_NAME_HTML, html);
    }

    private DocumentLifecycleSupport editable() {
        return documentLifecycleSupport;
    }

    public void saveAndPublish() {
        editable().saveAndPublish();
    }
}
