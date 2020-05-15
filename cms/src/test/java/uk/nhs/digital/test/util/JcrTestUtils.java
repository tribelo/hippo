package uk.nhs.digital.test.util;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.stream.Stream;

import static uk.nhs.digital.ExceptionUtils.wrapCheckedException;
import static uk.nhs.digital.JcrNodeUtils.streamOf;

public class JcrTestUtils {

    public static Node getDocumentVariantNode(final Node documentHandleNode, final JcrTestUtils.BloomReachJcrDocumentVariantType bloomReachJcrDocumentVariantType) {

        return getDocumentVariantNodesStream(documentHandleNode)
            .filter(node -> wrapCheckedException(() ->
                node.getProperty("hippostd:state").getString().equalsIgnoreCase(bloomReachJcrDocumentVariantType.name())
            ))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Variant " + bloomReachJcrDocumentVariantType.name() + " not found."));
    }

    private static NodeIterator getDocumentVariantNodes(final Node documentHandleNode) {
        return wrapCheckedException(() -> documentHandleNode.getNodes(documentHandleNode.getName() + "*"));
    }

    private static Stream<Node> getDocumentVariantNodesStream(final Node documentHandleNode) {
        return streamOf(getDocumentVariantNodes(documentHandleNode));
    }

    public enum BloomReachJcrDocumentVariantType {
        DRAFT,
        UNPUBLISHED,
        PUBLISHED;
    }
}
