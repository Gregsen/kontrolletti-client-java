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
