package org.zalando.kontrolletti;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Repository {

    private final String uri;
    private final String host;
    private final String project;
    private final String repository;

    @JsonCreator
    public Repository(@JsonProperty("uri") final String uri,
            @JsonProperty("host") final String host,
            @JsonProperty("project") final String project,
            @JsonProperty("repository") final String repository) {
        this.uri = uri;
        this.host = host;
        this.project = project;
        this.repository = repository;
    }

    public String getUri() {
        return uri;
    }

    public String getHost() {
        return host;
    }

    public String getProject() {
        return project;
    }

    public String getRepository() {
        return repository;
    }

    @Override
    public String toString() {
        return "Repository{"                           //
                + "uri='" + uri + '\''                 //
                + ", host='" + host + '\''             //
                + ", project='" + project + '\''       //
                + ", repository='" + repository + '\'' //
                + '}';                                 //
    }
}
