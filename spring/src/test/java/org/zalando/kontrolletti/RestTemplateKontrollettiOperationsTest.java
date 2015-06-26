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

import static org.springframework.http.HttpStatus.MOVED_PERMANENTLY;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import static org.zalando.kontrolletti.RestTemplateKontrollettiOperations.X_NORMALIZED_REPOSITORY_URL;

import org.junit.Before;
import org.junit.Test;

import org.springframework.http.HttpHeaders;

import org.springframework.test.web.client.MockRestServiceServer;

import org.springframework.web.client.RestTemplate;

public class RestTemplateKontrollettiOperationsTest {

    private static final String BASE_URL = "https://kontrolletti.zalando.org";

    private RestTemplateKontrollettiOperations kontrollettiOperations;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() throws Exception {
        final RestTemplate restTemplate = new RestTemplate();

        mockServer = MockRestServiceServer.createServer(restTemplate);
        kontrollettiOperations = new RestTemplateKontrollettiOperations(restTemplate, BASE_URL);
    }

    @Test
    public void testNormalizeRepositoryUrl() throws Exception {
        final String repositoryUrl = "git@github.com:zalando/kontrolletti-client-java.git";
        final HttpHeaders headers = new HttpHeaders();
        headers.add(X_NORMALIZED_REPOSITORY_URL, "https://github.com/zalando/kontrolletti-client-java");

        mockServer.expect(requestTo(BASE_URL + "/api/repos/git%40github.com%3Azalando%2Fkontrolletti-client-java.git"))
                  .andRespond(withStatus(MOVED_PERMANENTLY).headers(headers));

        kontrollettiOperations.normalizeRepositoryUrl(repositoryUrl);

        mockServer.verify();
    }
}
