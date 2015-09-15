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
