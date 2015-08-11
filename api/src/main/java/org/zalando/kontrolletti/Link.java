package org.zalando.kontrolletti;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Link {

    private final String rel;

    private final String method;

    private final String type;

    private final String href;

    @JsonCreator
    public Link(@JsonProperty("rel") final String rel,
            @JsonProperty("method") final String method,
            @JsonProperty("type") final String type,
            @JsonProperty("href") final String href) {
        this.rel = rel;
        this.method = method;
        this.type = type;
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public String getMethod() {
        return method;
    }

    public String getType() {
        return type;
    }

    public String getHref() {
        return href;
    }

    @Override
    public String toString() {
        return "Link{"                         //
                + "rel='" + rel + '\''         //
                + ", method='" + method + '\'' //
                + ", type='" + type + '\''     //
                + ", href='" + href + '\''     //
                + '}';                         //
    }
}
