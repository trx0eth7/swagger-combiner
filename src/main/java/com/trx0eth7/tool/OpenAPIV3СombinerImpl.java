package com.trx0eth7.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trx0eth7.exception.ParseFileException;
import com.trx0eth7.tool.scanner.FileScanner;
import io.swagger.v3.core.util.Yaml;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.callbacks.Callback;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.links.Link;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;

public class OpenAPIV3СombinerImpl implements OpenAPIV3Сombiner {
    private FileScanner scanner;
    private ObjectMapper objectMapper;

    public OpenAPIV3СombinerImpl(FileScanner scanner) {
        this.scanner = scanner;
        this.objectMapper = Yaml.mapper(); //using default yaml parser
    }

    public OpenAPIV3СombinerImpl(FileScanner scanner, ObjectMapper objectMapper) {
        this.scanner = scanner;
        this.objectMapper = objectMapper;
    }

    @Override
    public String combine(String scanPathDir) {
        List<String> filePaths = scanner.scan(scanPathDir);
        return combine(filePaths);
    }

    private String combine(List<String> filePaths) {
        OpenAPI openAPIOut = new OpenAPI();

        for (String filePath : filePaths) {
            OpenAPI openAPI = parse(new File(filePath));
            combineOpenAPIs(openAPI, openAPIOut);
        }

        return openAPIOut.toString();
    }

    private OpenAPI parse(File file) {
        try {
            return objectMapper.readValue(file, OpenAPI.class);
        } catch (IOException e) {
            throw new ParseFileException(e);
        }
    }

    //TODO make simple (use SOLID)
    private void combineOpenAPIs(OpenAPI in, OpenAPI out) {
        String inOpenApiVersion = in.getOpenapi();
        Info inInfo = in.getInfo();
        ExternalDocumentation inExternalDocs = in.getExternalDocs();
        List<Server> inServers = in.getServers();
        List<SecurityRequirement> inSecurity = in.getSecurity();
        List<Tag> inTags = in.getTags();
        Paths inPaths = in.getPaths();
        Components inComponents = in.getComponents();
        Map<String, Object> inExtensions = in.getExtensions();

        if (nonNull(inOpenApiVersion)) {
            out.setOpenapi(inOpenApiVersion);
        }
        if (nonNull(inInfo)) {
            out.setInfo(inInfo);
        }
        if (nonNull(inExternalDocs)) {
            out.setExternalDocs(inExternalDocs);
        }
        if (nonNull(inServers)) {
            for (Server server : in.getServers()) {
                out.addServersItem(server);
            }
        }
        if (nonNull(inSecurity)) {
            for (SecurityRequirement securityRequirement : in.getSecurity()) {
                out.addSecurityItem(securityRequirement);
            }
        }
        if (nonNull(inTags)) {
            for (Tag tag : in.getTags()) {
                out.addTagsItem(tag);
            }
        }
        if (nonNull(inPaths)) {
            Paths outPaths = out.getPaths();
            if (nonNull(outPaths)) {
                for (Map.Entry<String, PathItem> pathItemEntry : inPaths.entrySet()) {
                    outPaths.addPathItem(pathItemEntry.getKey(), pathItemEntry.getValue());
                }
            } else {
                out.setPaths(inPaths);
            }
        }
        if (nonNull(inComponents)) {
            Components outComponents = out.getComponents();

            if (nonNull(outComponents)) {
                Map<String, Callback> inCallbacks = inComponents.getCallbacks();

                if (nonNull(inCallbacks)) {
                    Map<String, Callback> outCallbacks = outComponents.getCallbacks();

                    if (nonNull(outCallbacks)) {
                        for (Map.Entry<String, Callback> callbackEntry : inCallbacks.entrySet()) {
                            outComponents.addCallbacks(callbackEntry.getKey(), callbackEntry.getValue());
                        }
                    } else {
                        outComponents.setCallbacks(inCallbacks);
                    }
                }

                Map<String, Example> inExamples = inComponents.getExamples();

                if (nonNull(inExamples)) {
                    Map<String, Example> outExamples = outComponents.getExamples();

                    if (nonNull(outExamples)) {
                        for (Map.Entry<String, Example> exampleEntry : inExamples.entrySet()) {
                            outComponents.addExamples(exampleEntry.getKey(), exampleEntry.getValue());
                        }
                    } else {
                        outComponents.setExamples(inExamples);
                    }
                }

                Map<String, Object> inComponentsExtensions = inComponents.getExtensions();

                if (nonNull(inComponentsExtensions)) {
                    Map<String, Object> outComponentsExtensions = outComponents.getExtensions();

                    if (nonNull(outComponentsExtensions)) {
                        for (Map.Entry<String, Object> extensionEntry : inComponentsExtensions.entrySet()) {
                            outComponents.addExtension(extensionEntry.getKey(), extensionEntry.getValue());
                        }
                    } else {
                        outComponents.setExtensions(inComponentsExtensions);
                    }
                }

                Map<String, Header> inHeaders = inComponents.getHeaders();

                if (nonNull(inHeaders)) {
                    Map<String, Header> outHeaders = outComponents.getHeaders();

                    if (nonNull(outHeaders)) {
                        for (Map.Entry<String, Header> headerEntry : inHeaders.entrySet()) {
                            outComponents.addHeaders(headerEntry.getKey(), headerEntry.getValue());
                        }
                    } else {
                        outComponents.setHeaders(inHeaders);
                    }
                }

                Map<String, Link> inLinks = inComponents.getLinks();

                if (nonNull(inLinks)) {
                    Map<String, Link> outLinks = outComponents.getLinks();

                    if (nonNull(outLinks)) {
                        for (Map.Entry<String, Link> linkEntry : inLinks.entrySet()) {
                            outComponents.addLinks(linkEntry.getKey(), linkEntry.getValue());
                        }
                    } else {
                        outComponents.setLinks(inLinks);
                    }
                }

                Map<String, Parameter> inParameters = inComponents.getParameters();

                if (nonNull(inParameters)) {
                    Map<String, Parameter> outParameters = outComponents.getParameters();

                    if (nonNull(outParameters)) {
                        for (Map.Entry<String, Parameter> parameterEntry : inParameters.entrySet()) {
                            outComponents.addParameters(parameterEntry.getKey(), parameterEntry.getValue());
                        }
                    } else {
                        outComponents.setParameters(inParameters);
                    }
                }

                Map<String, RequestBody> inRequestBodies = inComponents.getRequestBodies();

                if (nonNull(inRequestBodies)) {
                    Map<String, RequestBody> outRequestBodies = outComponents.getRequestBodies();

                    if (nonNull(outRequestBodies)) {
                        for (Map.Entry<String, RequestBody> requestBodyEntry : inRequestBodies.entrySet()) {
                            outComponents.addRequestBodies(requestBodyEntry.getKey(), requestBodyEntry.getValue());
                        }
                    } else {
                        outComponents.setRequestBodies(inRequestBodies);
                    }
                }

                Map<String, ApiResponse> inResponses = inComponents.getResponses();

                if (nonNull(inResponses)) {
                    Map<String, ApiResponse> outResponses = outComponents.getResponses();

                    if (nonNull(outResponses)) {
                        for (Map.Entry<String, ApiResponse> responseEntry : inResponses.entrySet()) {
                            outComponents.addResponses(responseEntry.getKey(), responseEntry.getValue());
                        }
                    } else {
                        outComponents.setResponses(inResponses);
                    }
                }

                Map<String, Schema> inSchemas = inComponents.getSchemas();

                if (nonNull(inSchemas)) {
                    Map<String, Schema> outSchemas = outComponents.getSchemas();

                    if (nonNull(outSchemas)) {
                        for (Map.Entry<String, Schema> schemaEntry : inSchemas.entrySet()) {
                            outComponents.addSchemas(schemaEntry.getKey(), schemaEntry.getValue());
                        }
                    } else {
                        outComponents.setSchemas(inSchemas);
                    }
                }

                Map<String, SecurityScheme> inSecuritySchemes = inComponents.getSecuritySchemes();

                if (nonNull(inSecuritySchemes)) {
                    Map<String, SecurityScheme> outSecuritySchemes = outComponents.getSecuritySchemes();

                    if (nonNull(outSecuritySchemes)) {
                        for (Map.Entry<String, SecurityScheme> securitySchemeEntry : inSecuritySchemes.entrySet()) {
                            outComponents.addSecuritySchemes(securitySchemeEntry.getKey(), securitySchemeEntry.getValue());
                        }
                    } else {
                        outComponents.setSecuritySchemes(inSecuritySchemes);
                    }
                }

            } else {
                out.setComponents(inComponents);
            }
        }
        if (nonNull(inExtensions)) {
            Map<String, Object> outExtensions = out.getExtensions();
            if (nonNull(outExtensions)) {
                for (Map.Entry<String, Object> extensionEntry : inExtensions.entrySet()) {
                    outExtensions.putIfAbsent(extensionEntry.getKey(), extensionEntry.getValue());
                }
            } else {
                out.setExtensions(inExtensions);
            }
        }
    }
}


