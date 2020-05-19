package uk.nhs.digital;

import org.hippoecm.repository.util.JcrUtils;
import org.hippoecm.repository.util.WorkflowUtils;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.forge.content.exim.core.impl.WorkflowDocumentManagerImpl;
import org.onehippo.repository.documentworkflow.DocumentWorkflow;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;

import java.util.stream.Stream;

import static uk.nhs.digital.ExceptionUtils.wrapCheckedException;
import static uk.nhs.digital.JcrNodeUtils.validateIsOfTypeHandle;

public abstract class JcrDocumentUtils {

    private static final String WORKFLOW_CATEGORY_DEFAULT = "default";

    public static void publish(final Node documentHandleNode) {
        try {
            validateIsOfTypeHandle(documentHandleNode);

            final DocumentWorkflow workflow = WorkflowUtils
                .getWorkflow(documentHandleNode, WORKFLOW_CATEGORY_DEFAULT, DocumentWorkflow.class)
                .orElseThrow(() -> new RuntimeException(
                    "Could not find workflow of category " + WORKFLOW_CATEGORY_DEFAULT + " implementing " + DocumentWorkflow.class.getName()
                ));

            workflow.publish();

        } catch (final Exception e) {
            throw new RuntimeException("Failed to publish document " + JcrUtils.getNodePathQuietly(documentHandleNode), e);
        }
    }

    public static void save(final Session session) {
        wrapCheckedException(session::save);
    }

    public static DocumentManager documentManagerFor(final Session session) {
        return new WorkflowDocumentManagerImpl(session);
    }

    public static Node getDocumentVariantNodeDraft(final Node documentHandleNode) {
        return getDocumentVariantNode(documentHandleNode, DocumentVariantType.DRAFT);
    }

    public static Node getDocumentVariantNodeUnpublished(final Node documentHandleNode) {
        return getDocumentVariantNode(documentHandleNode, DocumentVariantType.UNPUBLISHED);
    }

    public static Node getDocumentVariantNodePublished(final Node documentHandleNode) {
        return getDocumentVariantNode(documentHandleNode, DocumentVariantType.PUBLISHED);
    }

    public static Node getDocumentVariantNode(final Node documentHandleNode,
                                              final DocumentVariantType documentVariantType) {

        validateIsOfTypeHandle(documentHandleNode);

        return getDocumentVariantNodesStream(documentHandleNode)
            .filter(node -> wrapCheckedException(() ->
                documentVariantType.name().equalsIgnoreCase(node.getProperty("hippostd:state").getString())
            ))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Variant " + documentVariantType.name() + " not " +
                "found."));
    }

    private static NodeIterator getDocumentVariantNodes(final Node documentHandleNode) {
        return wrapCheckedException(() -> documentHandleNode.getNodes(documentHandleNode.getName() + "*"));
    }

    private static Stream<Node> getDocumentVariantNodesStream(final Node documentHandleNode) {
        return JcrNodeUtils.streamOf(getDocumentVariantNodes(documentHandleNode));
    }


    public enum DocumentVariantType {
        DRAFT,
        UNPUBLISHED,
        PUBLISHED;
    }

}
