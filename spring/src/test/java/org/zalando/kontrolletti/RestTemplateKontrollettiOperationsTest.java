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

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.failBecauseExceptionWasNotThrown;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import static org.zalando.kontrolletti.RestTemplateKontrollettiOperations.X_NORMALIZED_REPOSITORY_URL;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpResponse;

import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

public class RestTemplateKontrollettiOperationsTest {

    private static final String BASE_URL = "https://kontrolletti.zalando.org";

    private static final String NORMALIZED_URL = "https://github.com/zalando/kontrolletti-client-java";
    private static final String NORMALIZED_ENCODED_URL =
        "https%3A%2F%2Fgithub.com%2Fzalando%2Fkontrolletti-client-java";

    private static final String DENORMALIZED_URL = "git@github.com:zalando/kontrolletti-client-java.git";
    private static final String DENORMALIZED_ENCODED_URL = "git%40github.com%3Azalando%2Fkontrolletti-client-java.git";

    private RestTemplateKontrollettiOperations kontrollettiOperations;

    private MockRestServiceServer mockServer;

    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new KontrollettiResponseErrorHandler());

        mockServer = MockRestServiceServer.createServer(restTemplate);
        kontrollettiOperations = new RestTemplateKontrollettiOperations(restTemplate, BASE_URL);
    }

    @Test
    public void testNormalizeDenormalizedRepositoryUrl() throws Exception {
        mockServer.expect(requestTo(BASE_URL + "/api/repos/" + DENORMALIZED_ENCODED_URL)) //
                  .andRespond(
                      withStatus(MOVED_PERMANENTLY)                                       //
                      .headers(headers(X_NORMALIZED_REPOSITORY_URL, NORMALIZED_URL)));

        assertThat(kontrollettiOperations.normalizeRepositoryUrl(DENORMALIZED_URL)).isEqualTo(NORMALIZED_URL);

        mockServer.verify();
    }

    @Test
    public void testNormalizeFoundNormalizedRepositoryUrl() throws Exception {
        mockServer.expect(requestTo(BASE_URL + "/api/repos/" + NORMALIZED_ENCODED_URL)) //
                  .andRespond(withStatus(OK)                                            //
                      .headers(headers(X_NORMALIZED_REPOSITORY_URL, NORMALIZED_URL)));

        assertThat(kontrollettiOperations.normalizeRepositoryUrl(NORMALIZED_URL)).isEqualTo(NORMALIZED_URL);

        mockServer.verify();
    }

    @Test
    public void testNormalizeNotFoundNormalizedRepositoryUrl() throws Exception {
        mockServer.expect(requestTo(BASE_URL + "/api/repos/" + NORMALIZED_ENCODED_URL)) //
                  .andRespond(withStatus(NOT_FOUND));

        assertThat(kontrollettiOperations.normalizeRepositoryUrl(NORMALIZED_URL)).isEqualTo(NORMALIZED_URL);

        mockServer.verify();
    }

    @Test
    public void testNormalizationErrorWithLaxErrorHandler() throws Exception {
        mockServer.expect(requestTo(BASE_URL + "/api/repos/" + NORMALIZED_ENCODED_URL)) //
                  .andRespond(withStatus(BAD_REQUEST));

        try {
            restTemplate.setErrorHandler(new PassThroughResponseErrorHandler());
            kontrollettiOperations.normalizeRepositoryUrl(NORMALIZED_URL);
            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        } catch (IllegalStateException ignore) { }

        mockServer.verify();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() throws Exception {
        kontrollettiOperations.normalizeRepositoryUrl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyInput() throws Exception {
        kontrollettiOperations.normalizeRepositoryUrl("");
    }

    private static HttpHeaders headers(final String key, final String value) {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(key, value);
        return headers;
    }

    private static class PassThroughResponseErrorHandler implements ResponseErrorHandler {
        @Override
        public boolean hasError(final ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(final ClientHttpResponse response) throws IOException { }
    }
}
