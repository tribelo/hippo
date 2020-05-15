package uk.nhs.digital.apispecs;

import org.hippoecm.repository.api.Document;
import org.onehippo.forge.content.exim.core.DocumentManager;
import uk.nhs.digital.JcrDocumentUtils;
import uk.nhs.digital.JcrNodeUtils;

import javax.jcr.Node;
import javax.jcr.Session;

import static uk.nhs.digital.ExceptionUtils.wrapCheckedException;
import static uk.nhs.digital.JcrNodeUtils.validateIsOfTypeHandle;

class DocumentLifecycleSupport {

    private final Node documentHandleNode;

    // Set when any of the properties have been updated and the document needs saving to persist the changes.
    private boolean isDirty;

    // Populated only if isDirty.
    private DocumentManager documentManager;
    private Document draftDocumentVariant;
    private Node draftNodeCheckedOut;

    private DocumentLifecycleSupport(final Node documentHandleNode) {
        validate(documentHandleNode);

        this.documentHandleNode = documentHandleNode;
    }

    public static DocumentLifecycleSupport from(final Node documentHandleNode) {
        return new DocumentLifecycleSupport(documentHandleNode);
    }

    public void setProperty(final String propertyName, final String value) {
        try {
            ensureInitialisedForEditing();

            JcrNodeUtils.setPropertyQuietly(draftNodeCheckedOut, propertyName, value);

            setDirty(true);
        } catch (final Exception e) {
            throw new RuntimeException("Failed to update property " + propertyName + " on " + documentHandleNode, e);
        }
    }

    public void saveAndPublish() {
        save();
        publish();
    }

    private void save() {
        if (isDirty()) {
            try {
                JcrDocumentUtils.save(getSession());

                documentManager.commitEditableDocument(draftDocumentVariant);

                setDirty(false);
            } catch (Exception e) {
                throw new RuntimeException("Failed to save session for " + documentHandleNode, e);
            }
        }
    }

    private void publish() {
        try {
            JcrDocumentUtils.publish(documentHandleNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to publish " + documentHandleNode, e);
        }
    }

    private void validate(final Node documentHandleCandidateNode) {
        try {
            validateIsOfTypeHandle(documentHandleCandidateNode);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create document object for " + documentHandleCandidateNode, e);
        }
    }

    private Session getSession() {
        return JcrNodeUtils.getSessionQuietly(documentHandleNode);
    }

    private void ensureInitialisedForEditing() {
        documentManager = JcrDocumentUtils.documentManagerFor(getSession());

        draftDocumentVariant = documentManager.obtainEditableDocument(documentHandleNode);

        draftNodeCheckedOut = wrapCheckedException(() ->
            draftDocumentVariant.getCheckedOutNode(getSession())
        );
    }

    private boolean isDirty() {
        return isDirty;
    }

    private void setDirty(final boolean isDirty) {
        this.isDirty = isDirty;
    }
}
