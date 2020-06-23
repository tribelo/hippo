package uk.nhs.digital.apispecs.commonmark;

import org.commonmark.node.Code;
import org.commonmark.node.Node;
import org.commonmark.renderer.html.AttributeProvider;

import java.util.Map;

public class CodeAttributeProvider implements AttributeProvider {

    @Override
    public void setAttributes(Node node, String tagName, Map<String, String> attributes) {

        if (node instanceof Code) {

            attributes.put("class", "codeinline");
        }
    }
}
