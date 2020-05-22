package uk.nhs.digital.apispecs;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import uk.nhs.digital.apispecs.dto.Content;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Before
    public void init() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void publish_publishesSpecifications_thatChangedInApigeeAfterTheyWerePublishedInCms() {

        // Mock html provider call
        String s2html = "<html><body> Some content spec2 </body></html>";
        lenient().when(htmlProvider.getHtmlForSpec(any())).thenReturn(s2html);

        // Mock repository call
        List<ApiSpecification> specs = getApiSpecificationsList("248456",Optional.ofNullable(Instant.parse("2020-05-20T08:30:00.000Z")));
        when(repository.findAllApiSpecifications()).thenReturn(specs);

        // Mock apigee service call
        when(apigeeService.apigeeSpecsStatuses()).thenReturn(getContentList("248456",Instant.parse("2020-05-22T10:30:00.000Z")));

        service.publish();
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_ifTheyHaveNotChangedInApigeeAfterTheyWerePublishedInCms() {

        // Mock repository call
        List<ApiSpecification> specs = getApiSpecificationsList("248456",Optional.ofNullable(Instant.parse("2020-05-20T08:30:00.000Z")));
        when(repository.findAllApiSpecifications()).thenReturn(specs);

        // Mock apigee service call
        when(apigeeService.apigeeSpecsStatuses()).thenReturn(getContentList("248456",Instant.parse("2020-05-20T08:30:00.000Z")));

        service.publish();
    }

    private List<Content> getContentList(String id, Instant modified){

        Content content1 = new Content();
        content1.setId("248569");
        content1.setModified(Instant.parse("2020-05-10T10:30:00.000Z"));

        Content content2 = new Content();
        content2.setId(id);
        content2.setModified(modified);

        List<Content> contentList = new ArrayList<Content>();
        contentList.add(content1);
        contentList.add(content2);

        return contentList;
    }

    private  List<ApiSpecification> getApiSpecificationsList(String id, Optional<Instant> lastPublicationInstant){

        ApiSpecification spec1 = mock(ApiSpecification.class);
        spec1.setHtml("<html><body> Some content spec1 </body></html>");
        when(spec1.getId()).thenReturn("248569");
        when(spec1.getLastPublicationInstant()).thenReturn(Optional.ofNullable(Instant.parse("2020-05-10T10:30:00.000Z")));

        ApiSpecification spec2 = mock(ApiSpecification.class);
        when(spec2.getId()).thenReturn(id);
        when(spec2.getLastPublicationInstant()).thenReturn(lastPublicationInstant);

        List<ApiSpecification> specs = new ArrayList<ApiSpecification>();
        specs.add(spec1);
        specs.add(spec2);

        return specs;
    }
}
