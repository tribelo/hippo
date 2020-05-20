package uk.nhs.digital.apispecs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.apispecs.dto.Content;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApiSpecPublicationServiceTest {

    @InjectMocks
    private ApiSpecPublicationService service;

    @Mock
    private ApigeeService apigeeService;

    @Mock
    private ApiSpecRepository repository;

    @Mock
    private ApiSpecHtmlProvider htmlProvider;

    @Mock
    private ApiSpecification spec;

    List<Content> contentList;
    List<ApiSpecification> apiSpecifications;
    String html;

    @Before
    public void init() throws Exception {

        MockitoAnnotations.initMocks(this);

        Content content = new Content();
        content.setId("248569");
        contentList = new ArrayList<Content>();
        contentList.add(content);

        apiSpecifications = new ArrayList<ApiSpecification>();
        apiSpecifications.add(spec);

        html = "<html><body> Some content </body></html>";
    }

    @Test
    public void publishes() {

        // Mock html provider call
        lenient().when(htmlProvider.getHtmlForSpec(any())).thenReturn(html);

        // Mock repository call
        when(repository.findAllApiSpecifications()).thenReturn(apiSpecifications);

        // Mock apigee service call
        when(apigeeService.apigeeSpecsStatuses()).thenReturn(contentList);

        service.publish();

        assertEquals("248569", contentList.get(0).getId());

        assertEquals(1, apiSpecifications.size());

        assertTrue(html.contains("Some content"));

    }
}
