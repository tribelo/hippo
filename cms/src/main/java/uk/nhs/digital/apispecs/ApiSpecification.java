package uk.nhs.digital.apispecs;

import java.time.Instant;
import java.util.Optional;

import static org.hippoecm.repository.util.WorkflowUtils.Variant.DRAFT;
import static uk.nhs.digital.apispecs.ApiSpecification.ApiSpecPropertyNames.*;

public class ApiSpecification {

    private DocumentLifecycleSupport documentLifecycleSupport;

    private ApiSpecification(final DocumentLifecycleSupport documentLifecycleSupport) {
        this.documentLifecycleSupport = documentLifecycleSupport;
    }

    public static ApiSpecification from(final DocumentLifecycleSupport documentLifecycleSupport) {
        return new ApiSpecification(documentLifecycleSupport);
    }

    public String getId() {
        return jcrDocument().getStringProperty(SPECIFICATION_ID.propName(), DRAFT)
            .orElseThrow(() -> new RuntimeException("Specification id not available"))
            ;
    }

    public void setHtml(final String html) {
        jcrDocument().setProperty(HTML.propName(), html);
    }

    public void saveAndPublish() {
        jcrDocument().saveAndPublish();
    }

    @Override public String toString() {
        return "ApiSpecification{" +
            "documentLifecycleSupport=" + documentLifecycleSupport +
            '}';
    }

    public Optional<Instant> getLastPublicationInstant() {
        return jcrDocument().getLastPublicationInstant(PUBLICATION_DATE.propName());
    }

    private DocumentLifecycleSupport jcrDocument() {
        return documentLifecycleSupport;
    }

    enum ApiSpecPropertyNames {
        HTML("website:html"),
        SPECIFICATION_ID("website:specification_id"),
        PUBLICATION_DATE("hippostdpubwf:publicationDate");

        private final String propName;

        ApiSpecPropertyNames(final String propName) {
            this.propName = propName;
        }

        public String propName() {
            return propName;
        }
    }
}
