package uk.nhs.digital.apispecs;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.MockitoAnnotations.initMocks;

@Ignore("work in progress")
public class ApiSpecHtmlProviderTest {

    @Mock private ApigeeService apigeeService;
    @Mock ApiSpecification apiSpecification;

    private ApiSpecHtmlProvider apiSpecHtmlProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        apigeeService = new ApigeeService(null, null, null);

        apiSpecHtmlProvider = new ApiSpecHtmlProvider(apigeeService);
    }

    @Test
    public void converts() {

        // given

        // when
        final String actualSpecHtml = apiSpecHtmlProvider.getHtmlForSpec(apiSpecification);

        // then
        System.out.println(actualSpecHtml);

    }
}