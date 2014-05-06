/*
 *  Copyright (C) 2014 Copyright Citrix Systems, Inc.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of 
 *  this software and associated documentation files (the "Software"), to deal in 
 *  the Software without restriction, including without limitation the rights to 
 *  use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 *  of the Software, and to permit persons to whom the Software is furnished to 
 *  do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all 
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE 
 *  SOFTWARE.
 */

package com.podio.sdk.provider;

import com.podio.sdk.PodioFilter;
import com.podio.sdk.PodioProvider;
import com.podio.sdk.PodioProviderListener;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.client.delegate.ItemParser;
import com.podio.sdk.domain.Session;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public class BasicPodioProvider implements PodioProvider {

    private final ResultListener resultListener = new ResultListener() {
        @Override
        public void onFailure(Object ticket, String message) {
            if (providerListener != null) {
                providerListener.onRequestFailure(ticket, message);
            }
        }

        @Override
        public void onSessionChange(Object ticket, Session session) {
            if (providerListener != null) {
                providerListener.onSessionChange(ticket, session);
            }
        }

        @Override
        public void onSuccess(Object ticket, Object content) {
            if (providerListener != null) {
                providerListener.onRequestComplete(ticket, content);
            }
        }
    };

    private PodioProviderListener providerListener;
    private ItemParser<?> itemParser;
    protected RestClient client;

    @Override
    public Object changeRequest(PodioFilter filter, Object item) {
        Object ticket = null;

        if (client != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.PUT, filter, item);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object deleteRequest(PodioFilter filter) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.DELETE, filter, null);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object fetchRequest(PodioFilter filter) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.GET, filter, null);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object pushRequest(PodioFilter filter, Object item) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.POST, filter, item);

            if (client.enqueue(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    /**
     * Sets the callback interface used to report the result through. If this
     * callback is not given, then the rest operations can still be executed
     * silently. Note, though, that the GET operation, even though technically
     * possible, wouldn't make any sense without this callback.
     * 
     * @param providerListener
     *            The callback implementation. Null is valid.
     */
    public void setProviderListener(PodioProviderListener providerListener) {
        this.providerListener = providerListener;
    }

    /**
     * Sets the rest client that will perform the rest operation.
     * 
     * @param client
     *            The target {@link RestClient}.
     */
    public void setRestClient(RestClient client) {
        this.client = client;
    }

    public void setItemParser(ItemParser<?> itemParser) {
        this.itemParser = itemParser;
    }

    protected RestRequest buildRestRequest(RestOperation operation, PodioFilter filter,
            Object content) {
        RestRequest request = new RestRequest() //
                .setContent(content) //
                .setFilter(filter) //
                .setItemParser(itemParser) //
                .setOperation(operation) //
                .setResultListener(resultListener) //
                .setTicket(filter);

        return request;
    }
}
