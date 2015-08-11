package org.zalando.kontrolletti;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RepositoryResponse extends AbstractKontrollettiResponse<Repository> {

    @JsonCreator
    public RepositoryResponse(@JsonProperty("result") final Repository result,
            @JsonProperty("_links") final List<Link> links) {
        super(result, links);
    }
}
