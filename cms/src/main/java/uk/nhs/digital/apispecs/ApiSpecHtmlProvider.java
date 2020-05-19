package uk.nhs.digital.apispecs;

import io.swagger.codegen.v3.*;
import io.swagger.codegen.v3.generators.html.StaticHtml2Codegen;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;

public class ApiSpecHtmlProvider {

    private final ApigeeService apigeeService;

    public ApiSpecHtmlProvider(final ApigeeService apigeeService) {
        this.apigeeService = apigeeService;
    }

    public String getHtmlForSpec(final ApiSpecification apiSpecification) {

        File openApiSpecificationJson = null;
        List<File> apiSpecificationOutputFiles = emptyList();
        File swaggerOutputDirectory = null;

        try {
            openApiSpecificationJson = getOpenApiSpecFor(apiSpecification);

            swaggerOutputDirectory = getSwaggerOutputDirectory();

            apiSpecificationOutputFiles = convertToHtml(openApiSpecificationJson, swaggerOutputDirectory);

            return extractHtml(apiSpecificationOutputFiles);

        } catch (final Exception e) {
            throw new RuntimeException("Failed to generate HTML for specification " + apiSpecification, e);
        } finally {
            cleanup(apiSpecificationOutputFiles, openApiSpecificationJson, swaggerOutputDirectory);
        }
    }

    private String extractHtml(final List<File> apiSpecificationOutputFiles) {

        final File indexFile = apiSpecificationOutputFiles.get(0);

        return readFileContent(indexFile);
    }

    private String readFileContent(final File indexFile) {
        try {
            return FileUtils.readFileToString(indexFile, "UTF-8");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read file " + indexFile, e);
        }
    }

    private void cleanup(final List<File> filesCollection, final File... individualFiles) {
        Stream.concat(
            filesCollection.stream(),
            Arrays.stream(Optional.ofNullable(individualFiles).orElse(new File[0]))
        ).forEach(this::deleteFileIfExists);
    }

    private void deleteFileIfExists(final File fileToDelete) {

        try {
            if (fileToDelete != null) {
                if (fileToDelete.isFile()) {
                    fileToDelete.delete();
                } else if (fileToDelete.isDirectory()) {
                    FileUtils.deleteDirectory(fileToDelete);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<File> convertToHtml(final File openApiSpecificationJson, final File swaggerOutputDirectory) {


        final ClientOptInput clientOptInput = new ClientOptInput();

        final ClientOpts clientOptions = new ClientOpts();

        clientOptions.setOutputDirectory(swaggerOutputDirectory.getPath());
        // clientOptions.setUri(openApiSpecificationJson.toURI().toASCIIString());


        final CodegenConfig codegenConfig = new StaticHtml2Codegen();


        clientOptInput
            .opts(clientOptions)
            .config(codegenConfig)
            .setOpenAPI(new OpenAPIV3Parser().read(openApiSpecificationJson.toURI().toASCIIString(), null, null));


        final Generator generator = new DefaultGenerator().opts(clientOptInput);
        final List<File> outputFiles = Optional.ofNullable(generator.generate())
            .orElseGet(Collections::emptyList);

        return outputFiles;
    }

    private File getSwaggerOutputDirectory() throws IOException {
        return Files.createTempDirectory("cms_swagger_codegen_output").toFile();
    }

    private File getOpenApiSpecFor(final ApiSpecification apiSpecification) {

        final String openApiSpecJson = apigeeService.getSpecification(apiSpecification.getId());

        final File tempFile;
        try {
            tempFile = Files.createTempFile("cms_apispecjsonfile_", ".tmp").toFile();
            FileUtils.write(tempFile, openApiSpecJson, "UTF-8");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to obtain API specification.", e);
        }

        return tempFile;
    }
}
