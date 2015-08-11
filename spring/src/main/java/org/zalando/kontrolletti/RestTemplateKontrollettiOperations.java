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

import static java.util.Arrays.stream;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.RequestEntity.get;
import static org.springframework.http.RequestEntity.head;

import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URLEncoder;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.util.Assert;

import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

public class RestTemplateKontrollettiOperations implements KontrollettiOperations {

    static final String X_NORMALIZED_REPOSITORY_URL = "X-Normalized-Repository-URL";

    private static final String UTF_8 = "UTF-8";

    private final RestOperations restOperations;

    private final String baseUrl;

    /**
     * Constructor.
     *
     * @param  restOperations  usually a RestTemplate or OAuth2RestTemplate. Make sure it does not throw an exception on
     *                         404 responses, because this client implementation often handles 404s itself.
     * @param  baseUrl         URL of a Kontrolletti instance, e.g. https://kontrolletti.example.org
     */
    public RestTemplateKontrollettiOperations(final RestOperations restOperations, final String baseUrl) {
        this.restOperations = restOperations;
        this.baseUrl = baseUrl;
    }

    @Override
    public String normalizeRepositoryUrl(final String repositoryUrl) {
        Assert.hasText(repositoryUrl, "repositoryUrl must not be blank");

        final URI uri = buildUri("api", "repos", repositoryUrl);
        final ResponseEntity<Void> response = restOperations.exchange(head(uri).build(), Void.TYPE);
        final HttpStatus statusCode = response.getStatusCode();

        if (statusCode.is2xxSuccessful() || statusCode.is3xxRedirection()) {
            return response.getHeaders().getFirst(X_NORMALIZED_REPOSITORY_URL);
        } else if (statusCode == NOT_FOUND) { // url already normalized, but repo was not found
            return repositoryUrl;
        } else {
            throw new IllegalStateException("Unexpected response: " + response);
        }
    }

    @Override
    public RepositoryResponse getRepository(final String repositoryUrl) {
        Assert.hasText(repositoryUrl, "repositoryUrl must not be blank");

        final URI uri = buildUri("api", "repos", repositoryUrl);

        final ResponseEntity<RepositoryResponse> response = restOperations.exchange(get(uri).build(),
                RepositoryResponse.class);
        final HttpStatus statusCode = response.getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            return response.getBody();
        } else if (statusCode == NOT_FOUND) {
            return null;
        } else {
            throw new IllegalStateException("Unexpected response: " + response);
        }
    }

    private URI buildUri(final String... pathSegments) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(baseUrl);
        if (pathSegments != null && pathSegments.length > 0) {
            stream(pathSegments)                                //
            .map(RestTemplateKontrollettiOperations::urlEncode) //
                                   .forEach(builder::pathSegment);
        }

        return builder.build(true).toUri();
    }

    private static String urlEncode(final String string) {
        try {
            return URLEncoder.encode(string, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}
