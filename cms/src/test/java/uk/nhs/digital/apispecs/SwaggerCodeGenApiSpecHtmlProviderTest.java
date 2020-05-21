package uk.nhs.digital.apispecs;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.FileUtils.readFileInClassPath;

public class SwaggerCodeGenApiSpecHtmlProviderTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock private ApigeeService apigeeService;
    @Mock private ApiSpecification apiSpecification;

    private SwaggerCodeGenApiSpecHtmlProvider swaggerCodeGenApiSpecHtmlProvider;

    private final String specificationId = "123456";

    @Before
    public void setUp() {
        initMocks(this);

        given(apiSpecification.getId()).willReturn(specificationId);

        swaggerCodeGenApiSpecHtmlProvider = new SwaggerCodeGenApiSpecHtmlProvider(apigeeService);
    }

    @Test
    public void getHtmlForSpec_convertsProvidedOpenApiJsonToHtml() {

        // given
        final String apigeeApiSpecificationJson = apigeeApiSpecificationJson();
        given(apigeeService.getSpecification(any(String.class))).willReturn(apigeeApiSpecificationJson);

        // when
        final String actualSpecHtml = swaggerCodeGenApiSpecHtmlProvider.getHtmlForSpec(apiSpecification);

        // then
        final String expectedSpecHtml = vanillaSwaggerCodeGenGeneratedSpecificationHtml();

        then(apigeeService).should().getSpecification(specificationId);

        assertThat(
            "Specification HTML has been generated using vanilla Swagger CodeGen v3",
            actualSpecHtml,
            is(expectedSpecHtml)
        );
    }

    @Test
    public void getHtmlForSpec_throwsException_onApigeeFailure() {

        // given
        final RuntimeException apigeeServiceException = new RuntimeException();
        given(apigeeService.getSpecification(any(String.class))).willThrow(apigeeServiceException);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(startsWith("Failed to generate HTML for specification "));
        expectedException.expectCause(sameInstance(apigeeServiceException));

        // when
        swaggerCodeGenApiSpecHtmlProvider.getHtmlForSpec(apiSpecification);

        // then
        // expectations set in 'given' are satisfied
    }

    @Test
    public void getHtmlForSpec_throwsException_onCodeGenFailure() {

        // given
        final String invalidSpecificationJson = "invalid specification JSON";
        given(apigeeService.getSpecification(any(String.class))).willReturn(invalidSpecificationJson);

        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(startsWith("Failed to generate HTML for specification "));
        expectedException.expectCause(isA(NullPointerException.class));

        // when
        swaggerCodeGenApiSpecHtmlProvider.getHtmlForSpec(apiSpecification);

        // then
        // expectations set in 'given' are satisfied
    }

    private String apigeeApiSpecificationJson() {
        return readFileInClassPath("/test-data/api-specifications/SwaggerCodeGenApiSpecHtmlProviderTest/realistic-openapi-v3-specification.json");
    }

    private String vanillaSwaggerCodeGenGeneratedSpecificationHtml() {
        return readFileInClassPath("/test-data/api-specifications/SwaggerCodeGenApiSpecHtmlProviderTest/vanilla-codegen-v3-generated-spec.html");
    }
}