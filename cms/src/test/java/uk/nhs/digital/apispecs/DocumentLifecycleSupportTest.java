package uk.nhs.digital.apispecs;

import org.hippoecm.repository.api.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.JcrDocumentUtils;
import uk.nhs.digital.JcrNodeUtils;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JcrDocumentUtils.class)
public class DocumentLifecycleSupportTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private Node documentHandleNode;
    @Mock private Node draftNodeCheckedOut;
    @Mock private Session session;
    @Mock private DocumentManager documentManager;
    @Mock private Document draftDocumentVariant;

    private DocumentLifecycleSupport documentLifecycleSupport;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        given(documentHandleNode.isNodeType("hippo:handle")).willReturn(true);
        given(documentHandleNode.getSession()).willReturn(session);
        given(documentManager.obtainEditableDocument(documentHandleNode)).willReturn(draftDocumentVariant);
        given(draftDocumentVariant.getCheckedOutNode(session)).willReturn(draftNodeCheckedOut);

        mockStatic(JcrDocumentUtils.class);
        given(JcrDocumentUtils.documentManagerFor(session)).willReturn(documentManager);

        documentLifecycleSupport = DocumentLifecycleSupport.from(documentHandleNode);
    }

    @Test
    public void setProperty_updatesPropertyWithGivenValueOnDraftVariant() throws Exception {

        // given
        final String propertyName = "propertyName";
        final String value = "value";

        // when
        documentLifecycleSupport.setProperty(propertyName, value);

        // then
        then(draftNodeCheckedOut).should().setProperty(propertyName, value);
    }

    @Test
    public void saveAndPublish_savesChangeAndPublishesDocument_whenChangesMade() {

        // given
        documentLifecycleSupport.setProperty("aPropertyName", "aPropertyValue");

        // when
        documentLifecycleSupport.saveAndPublish();

        // then
        verifyStatic(JcrDocumentUtils.class);
        JcrDocumentUtils.save(session);

        verifyStatic(JcrDocumentUtils.class);
        JcrDocumentUtils.publish(documentHandleNode);
    }

    @Test
    public void saveAndPublish_doesNotSaveChangesButPublishesDocument_whenNoChangesMadeButUnpublishedVersionAvailable() {

        // given
        // setUp()

        // when
        documentLifecycleSupport.saveAndPublish();

        // then
        verifyStatic(JcrDocumentUtils.class, times(0));
        JcrDocumentUtils.save(session);

        verifyStatic(JcrDocumentUtils.class);
        JcrDocumentUtils.publish(documentHandleNode);
    }

    @Test
    public void from_throwsException_whenNodeNotOfTypeHandle() throws RepositoryException {

        // given
        given(documentHandleNode.isNodeType("hippo:handle")).willReturn(false);
        given(documentHandleNode.toString()).willReturn("testDocumentHandleNode");

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Failed to create document object for testDocumentHandleNode");

        // when
        DocumentLifecycleSupport.from(documentHandleNode);

        // then
        // expectations set up in 'given' are satisfied
    }
}