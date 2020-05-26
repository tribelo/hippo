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
        List<ApiSpecification> specs = getApiSpecificationsList("248569",Optional.ofNullable(Instant.parse("2020-05-10T10:30:00.000Z")),"248456",Optional.ofNullable(Instant.parse("2020-05-20T08:30:00.000Z")));
        when(repository.findAllApiSpecifications()).thenReturn(specs);

        // Mock apigee service call
        when(apigeeService.apigeeSpecsStatuses()).thenReturn(getContentList("248569",Instant.parse("2020-05-10T10:30:00.000Z"),"248456",Instant.parse("2020-05-22T10:30:00.000Z")));

        service.publish();

        verify(specs.get(1)).setHtml(s2html);
        verify(specs.get(1)).saveAndPublish();
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_ifTheyHaveNotChangedInApigeeAfterTheyWerePublishedInCms() {

        // Mock repository call
        List<ApiSpecification> specs = getApiSpecificationsList("248569",Optional.ofNullable(Instant.parse("2020-05-10T10:30:00.000Z")),"248456",Optional.ofNullable(Instant.parse("2020-05-20T08:30:00.000Z")));
        when(repository.findAllApiSpecifications()).thenReturn(specs);

        // Mock apigee service call
        when(apigeeService.apigeeSpecsStatuses()).thenReturn(getContentList("248569",Instant.parse("2020-05-10T10:30:00.000Z"),"248456",Instant.parse("2020-05-20T08:30:00.000Z")));

        service.publish();

        verify(specs.get(0), never()).setHtml("");
        verify(specs.get(1), never()).setHtml("");

        verify(specs.get(0), never()).saveAndPublish();
        verify(specs.get(1), never()).saveAndPublish();
    }

    @Test
    public void publish_doesNotChangeNorPublishSpecifications_ifTheyDoNotHaveMatchingCounterpartsInApigee() {

        // Mock repository call
        List<ApiSpecification> specs = getApiSpecificationsList("248566",Optional.ofNullable(Instant.parse("2020-05-10T10:30:00.000Z")),"248567",Optional.ofNullable(Instant.parse("2020-05-20T08:30:00.000Z")));
        when(repository.findAllApiSpecifications()).thenReturn(specs);

        // Mock apigee service call
        when(apigeeService.apigeeSpecsStatuses()).thenReturn(getContentList("248568",Instant.parse("2020-05-10T11:30:00.000Z"),"248569",Instant.parse("2020-05-22T09:30:00.000Z")));

        service.publish();

        verify(specs.get(0), never()).setHtml("");
        verify(specs.get(1), never()).setHtml("");

        verify(specs.get(0), never()).saveAndPublish();
        verify(specs.get(1), never()).saveAndPublish();
    }

    private List<Content> getContentList(String id1, Instant modified1, String id2, Instant modified2){

        Content content1 = new Content();
        content1.setId(id1);
        content1.setModified(modified1);

        Content content2 = new Content();
        content2.setId(id2);
        content2.setModified(modified2);

        List<Content> contentList = new ArrayList<Content>();
        contentList.add(content1);
        contentList.add(content2);

        return contentList;
    }

    private  List<ApiSpecification> getApiSpecificationsList(String id1, Optional<Instant> lastPublicationInstant1, String id2, Optional<Instant> lastPublicationInstant2){

        ApiSpecification spec1 = mock(ApiSpecification.class);
        spec1.setHtml("<html><body> Some content spec1 </body></html>");
        when(spec1.getId()).thenReturn(id1);
        when(spec1.getLastPublicationInstant()).thenReturn(lastPublicationInstant1);

        ApiSpecification spec2 = mock(ApiSpecification.class);
        when(spec2.getId()).thenReturn(id2);
        when(spec2.getLastPublicationInstant()).thenReturn(lastPublicationInstant2);

        List<ApiSpecification> specs = new ArrayList<ApiSpecification>();
        specs.add(spec1);
        specs.add(spec2);

        return specs;
    }
}
