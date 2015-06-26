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

import static java.lang.String.format;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.RequestEntity.head;

import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URLEncoder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTemplateKontrollettiOperations implements KontrollettiOperations {

    static final String X_NORMALIZED_REPOSITORY_URL = "X-Normalized-Repository-URL";

    private final RestOperations restOperations;

    private final String baseUrl;

    public RestTemplateKontrollettiOperations(final RestOperations restOperations, final String baseUrl) {
        this.restOperations = restOperations;
        this.baseUrl = baseUrl;
    }

    public String normalizeRepositoryUrl(final String repositoryUrl) {
        if (repositoryUrl == null || repositoryUrl.isEmpty()) {
            throw new IllegalArgumentException("repositoryUrl must not be null");
        }

        final URI uri;
        try {
            uri = UriComponentsBuilder.fromUriString(baseUrl)
                                      .pathSegment("api", "repos", URLEncoder.encode(repositoryUrl, "UTF-8"))
                                      .build(true).toUri();
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }

        final ResponseEntity<Void> response = restOperations.exchange(head(uri).build(), Void.TYPE);
        final HttpStatus statusCode = response.getStatusCode();

        if (statusCode.is2xxSuccessful() || statusCode.is3xxRedirection()) {
            return response.getHeaders().getFirst(X_NORMALIZED_REPOSITORY_URL);
        } else if (statusCode == NOT_FOUND) {
            return repositoryUrl;
        } else if (statusCode == BAD_REQUEST) {
            throw new IllegalArgumentException(format("malformed repositoryUrl: '%s'", repositoryUrl));
        } else {
            throw new IllegalStateException(format("Ooops, something went terribly wrong: %s", response));
        }
    }
}
