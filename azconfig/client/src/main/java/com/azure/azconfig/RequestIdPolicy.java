/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.azure.azconfig;

import com.microsoft.rest.v3.http.HttpPipelineCallContext;
import com.microsoft.rest.v3.http.HttpResponse;
import com.microsoft.rest.v3.http.NextPolicy;
import com.microsoft.rest.v3.http.policy.HttpPipelinePolicy;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Creates a policy which puts a UUID in the request header. Azure uses
 * the request id as the unique identifier for the request.
 * Also sets 'x-ms-return-client-request-id' header to tell server to return request id in the response.
 */
public class RequestIdPolicy implements HttpPipelinePolicy {
    private static final String REQUEST_ID_HEADER = "x-ms-client-request-id";
    private static final String ECHO_REQUEST_ID_HEADER = "x-ms-return-client-request-id";

    @Override
    public Mono<HttpResponse> process(HttpPipelineCallContext context, NextPolicy next) {
        String requestId = context.httpRequest().headers().value(REQUEST_ID_HEADER);
        if (requestId == null) {
            context.httpRequest().headers().set(REQUEST_ID_HEADER, UUID.randomUUID().toString());
        }
        context.httpRequest().headers().set(ECHO_REQUEST_ID_HEADER, "true");
        return next.process();
    }
}