package uk.nhs.digital.apispecs;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

public class ApiSpecificationTest {

    private DocumentLifecycleSupport documentLifecycleSupport;

    @Before
    public void setUp() {
        documentLifecycleSupport = mock(DocumentLifecycleSupport.class);
    }

    @Test
    public void setHtml_delegatesToDocumentProxy() {

        // given
        final ApiSpecification apiSpecification = ApiSpecification.from(documentLifecycleSupport);

        final String updatedHtmlContent = "<p>updated HTML content</p>";

        // when
        apiSpecification.setHtml(updatedHtmlContent);

        // then
        then(documentLifecycleSupport).should().setProperty("website:html", updatedHtmlContent);
    }

    @Test
    public void publish_delegatesToDocumentProxy() {

        // given
        final ApiSpecification apiSpecification = ApiSpecification.from(documentLifecycleSupport);

        // when
        apiSpecification.saveAndPublish();

        // then
        then(documentLifecycleSupport).should().saveAndPublish();
    }
}