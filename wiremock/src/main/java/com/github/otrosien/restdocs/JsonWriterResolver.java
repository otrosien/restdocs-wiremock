package com.github.otrosien.restdocs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.snippet.WriterResolver;
import org.springframework.restdocs.templates.TemplateFormat;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

final class JsonWriterResolver implements WriterResolver {

    private final PlaceholderResolver placeholderResolver;

    private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper(
            "{", "}");

    private String encoding = "UTF-8";

    private final TemplateFormat templateFormat = new TemplateFormat() {

        @Override
        public String getId() {
            return "json";
        }

        @Override
        public String getFileExtension() {
            return "json";
        }
    };

    public JsonWriterResolver(PlaceholderResolver placeholderResolver) {
        this.placeholderResolver = placeholderResolver;
    }

    @Override
    public Writer resolve(String operationName, String snippetName,
            RestDocumentationContext context) throws IOException {
        File outputFile = resolveFile(
                this.propertyPlaceholderHelper.replacePlaceholders(operationName,
                        this.placeholderResolver),
                snippetName + "." + this.templateFormat.getFileExtension(), context);

        if (outputFile != null) {
            createDirectoriesIfNecessary(outputFile);
            return new OutputStreamWriter(new FileOutputStream(outputFile),
                    this.encoding);
        }
        else {
            return new OutputStreamWriter(System.out, this.encoding);
        }
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    File resolveFile(String outputDirectory, String fileName,
            RestDocumentationContext context) {
        File outputFile = new File(outputDirectory, fileName);
        if (!outputFile.isAbsolute()) {
            outputFile = makeRelativeToConfiguredOutputDir(outputFile, context);
        }
        return outputFile;
    }

    private File makeRelativeToConfiguredOutputDir(File outputFile,
            RestDocumentationContext context) {
        File configuredOutputDir = context.getOutputDirectory();
        if (configuredOutputDir != null) {
            return new File(configuredOutputDir, outputFile.getPath());
        }
        return null;
    }

    private void createDirectoriesIfNecessary(File outputFile) {
        File parent = outputFile.getParentFile();
        if (!parent.isDirectory() && !parent.mkdirs()) {
            throw new IllegalStateException(
                    "Failed to create directory '" + parent + "'");
        }
    }

}
