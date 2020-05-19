package uk.nhs.digital.apispecs;

public class ApiSpecification {

    private static final String PROPERTY_NAME_HTML = "website:html";
    private static final String PROPERTY_NAME_SPECIFICATION_ID = "website:specification_id";

    private DocumentLifecycleSupport documentLifecycleSupport;

    private ApiSpecification(final DocumentLifecycleSupport documentLifecycleSupport) {
        this.documentLifecycleSupport = documentLifecycleSupport;
    }

    public static ApiSpecification from(final DocumentLifecycleSupport documentLifecycleSupport) {
        return new ApiSpecification(documentLifecycleSupport);
    }

    public void setHtml(final String html) {
        jcrDocument().setProperty(PROPERTY_NAME_HTML, html);
    }

    private DocumentLifecycleSupport jcrDocument() {
        return documentLifecycleSupport;
    }

    public void saveAndPublish() {
        jcrDocument().saveAndPublish();
    }

    @Override public String toString() {
        return "ApiSpecification{" +
            "documentLifecycleSupport=" + documentLifecycleSupport +
            '}';
    }

    public String getId() {
        return jcrDocument().getStringProperty(PROPERTY_NAME_SPECIFICATION_ID);
    }
}
