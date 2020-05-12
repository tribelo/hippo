package uk.nhs.digital.apispecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ApiSpecRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecRepository.class);

    public List<CmsSpec> apiSpecifications() {

        List<CmsSpec> specifications = new ArrayList<CmsSpec>();

        LOGGER.info("Query CMS to find Api specification documents...");

        return specifications;
    }
}
