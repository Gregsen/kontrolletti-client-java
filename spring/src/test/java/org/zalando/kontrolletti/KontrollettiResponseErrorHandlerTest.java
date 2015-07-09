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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.springframework.http.client.ClientHttpResponse;

import org.springframework.web.client.ResponseErrorHandler;

public class KontrollettiResponseErrorHandlerTest {

    private KontrollettiResponseErrorHandler errorHandler;

    private ResponseErrorHandler mockDelegate;

    private ClientHttpResponse mockResponse;

    @Before
    public void setUp() throws Exception {
        mockDelegate = mock(ResponseErrorHandler.class);
        errorHandler = new KontrollettiResponseErrorHandler(mockDelegate);

        mockResponse = mock(ClientHttpResponse.class);

    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(mockDelegate, mockResponse);
    }

    @Test
    public void test404HasError() throws Exception {
        when(mockDelegate.hasError(any(ClientHttpResponse.class))).thenReturn(true);
        when(mockResponse.getStatusCode()).thenReturn(NOT_FOUND);

        assertThat(errorHandler.hasError(mockResponse)).isFalse();

        verify(mockResponse).getStatusCode();
    }

    @Test
    public void testDelegateHasError() throws Exception {
        when(mockDelegate.hasError(any(ClientHttpResponse.class))).thenReturn(true);
        when(mockResponse.getStatusCode()).thenReturn(BAD_REQUEST);

        assertThat(errorHandler.hasError(mockResponse)).isTrue();

        verify(mockResponse).getStatusCode();
        verify(mockDelegate).hasError(same(mockResponse));
    }

    @Test
    public void testDelegateHasNoError() throws Exception {
        when(mockDelegate.hasError(any(ClientHttpResponse.class))).thenReturn(false);
        when(mockResponse.getStatusCode()).thenReturn(BAD_REQUEST);

        assertThat(errorHandler.hasError(mockResponse)).isFalse();

        verify(mockResponse).getStatusCode();
        verify(mockDelegate).hasError(same(mockResponse));
    }

    @Test
    public void testHandleError() throws Exception {
        errorHandler.handleError(mockResponse);
        verify(mockDelegate).handleError(same(mockResponse));
    }

    @Test
    public void testDefaultDelegate() throws Exception {
        when(mockResponse.getStatusCode()).thenReturn(OK);

        final KontrollettiResponseErrorHandler defaultDelegateErrorHandler = new KontrollettiResponseErrorHandler();
        assertThat(defaultDelegateErrorHandler.hasError(mockResponse)).isFalse();

        verify(mockResponse, atLeastOnce()).getStatusCode();
    }
}
