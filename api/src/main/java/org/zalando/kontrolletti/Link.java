/**
 * Copyright (C) 2015 Zalando SE (https://tech.zalando.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
