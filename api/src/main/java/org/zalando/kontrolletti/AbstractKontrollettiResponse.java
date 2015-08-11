package org.zalando.kontrolletti;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AbstractKontrollettiResponse<T> {

    private final T result;

    private final List<Link> links;

    @JsonCreator
    public AbstractKontrollettiResponse(@JsonProperty("result") final T result,
            @JsonProperty("_links") final List<Link> links) {
        this.result = result;
        this.links = links;
    }

    public T getResult() {
        return result;
    }

    public List<Link> getLinks() {
        return links;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + '{' + "result=" + result + ", links=" + links + '}';
    }
}
