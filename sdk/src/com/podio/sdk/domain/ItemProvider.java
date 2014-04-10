package com.podio.sdk.domain;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.ProviderListener;
import com.podio.sdk.RestClient;
import com.podio.sdk.client.RestRequest;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public class ItemProvider<T> implements Provider<T> {

    private final ResultListener resultListener = new ResultListener() {
        @Override
        public void onFailure(Object ticket, String message) {
            if (providerListener != null) {
                providerListener.onRequestFailed(ticket, message);
            }
        }

        @Override
        public void onSuccess(Object ticket, Object content) {
            if (providerListener != null) {
                providerListener.onRequestCompleted(ticket, content);
            }
        }
    };

    private ProviderListener providerListener;
    private RestClient client;

    @Override
    public Object changeItem(Filter filter, T item) {
        Object ticket = null;

        if (client != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.PUT, filter, item);

            if (client.perform(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object deleteItems(Filter filter) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.DELETE, filter);

            if (client.perform(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object fetchItems(Filter filter) {
        Object ticket = null;

        if (client != null && filter != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.GET, filter);

            if (client.perform(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public Object pushItem(T item) {
        Object ticket = null;

        if (client != null) {
            RestRequest restRequest = buildRestRequest(RestOperation.POST, item);

            if (client.perform(restRequest)) {
                ticket = restRequest.getTicket();
            }
        }

        return ticket;
    }

    @Override
    public void setProviderListener(ProviderListener providerListener) {
        this.providerListener = providerListener;
    }

    @Override
    public void setRestClient(RestClient client) {
        this.client = client;
    }

    private RestRequest buildRestRequest(RestOperation operation, Filter filter) {
        return buildRestRequest(operation, filter, null);
    }

    private RestRequest buildRestRequest(RestOperation operation, T content) {
        return buildRestRequest(operation, null, content);
    }

    private RestRequest buildRestRequest(RestOperation operation, Filter filter, T content) {
        Class<?> classOfContent = content != null ? content.getClass() : null;

        RestRequest request = new RestRequest() //
                .setContent(content) //
                .setItemType(classOfContent) //
                .setOperation(operation) //
                .setResultListener(resultListener) //
                .setFilter(filter) //
                .setTicket(filter);

        return request;
    }
}
