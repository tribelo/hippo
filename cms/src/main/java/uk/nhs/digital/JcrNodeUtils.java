package uk.nhs.digital;


import org.apache.commons.lang3.Validate;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static uk.nhs.digital.ExceptionUtils.wrapCheckedException;

public abstract class JcrNodeUtils {

    @SuppressWarnings("unchecked") // it's guaranteed that NodeIterator operates on instances of Node class
    public static Stream<Node> streamOf(final NodeIterator nodeIterator) {

        return StreamSupport.stream(
            Spliterators.<Node>spliteratorUnknownSize(nodeIterator, Spliterator.ORDERED),
            false
        );
    }

    public static void setPropertyQuietly(final Node node, final String propertyName, final String value) {
        wrapCheckedException(() -> node.setProperty(propertyName, value));
    }

    public static Session getSessionQuietly(final Node node) {
        return wrapCheckedException(node::getSession);
    }

    public static void validateIsOfTypeHandle(final Node documentHandleCandidateNode) {
        Validate.isTrue(
            wrapCheckedException(() -> documentHandleCandidateNode.isNodeType("hippo:handle")),
            "Node's 'jcr:primaryType' property has to be of type 'hippo:handle'"
        );
    }
}
