package com.podio.sdk.client;

import java.util.List;

import android.test.AndroidTestCase;

import com.podio.sdk.Filter;
import com.podio.sdk.domain.ItemFilter;
import com.podio.sdk.internal.request.RestOperation;
import com.podio.sdk.internal.request.ResultListener;

public class RestRequestTest extends AndroidTestCase {

    /**
     * Verifies that all setters of the {@link RestRequest} returns the
     * RestRequest itself (to enabled a chained design pattern).
     * 
     * <pre>
     * 
     * 1. Create a new RestRequest object.
     * 
     * 2. Verify that each setter returns that very same object.
     * 
     * </pre>
     */
    public void testRestRequestSettersReturnRestRequest() {
        RestRequest target = new RestRequest();

        assertEquals(target, target.setContent(null));
        assertEquals(target, target.setFilter(null));
        assertEquals(target, target.setItemType(null));
        assertEquals(target, target.setOperation(null));
        assertEquals(target, target.setResultListener(null));
    }

    /**
     * Verifies that the assigned fields are kept intact until they are
     * requested again.
     * 
     * <pre>
     * 
     * 1. Create a new RestRequest object.
     * 
     * 2. Initialize the object with some known values.
     * 
     * 3. Call the corresponding getters and verify the results haven't
     *      changed.
     * 
     * </pre>
     */
    public void testRestRequestGettersReturnCorrectValues() {
        RestRequest target = new RestRequest();

        RestOperation operation = RestOperation.GET;

        Object item = new Object();
        Class<?> itemType = item.getClass();

        Filter filter = new ItemFilter("test") //
                .addQueryParameter("id", "1") //
                .addQueryParameter("id", "2");

        ResultListener resultListener = new ResultListener() {
            @Override
            public void onFailure(Object ticket, String message) {
            }

            @Override
            public void onSuccess(Object ticket, List<?> items) {
            }
        };

        target.setContent(item) //
                .setFilter(filter) //
                .setItemType(itemType) //
                .setOperation(operation) //
                .setResultListener(resultListener);

        assertEquals(item, target.getContent());
        assertEquals(filter, target.getFilter());
        assertEquals(itemType, target.getItemType());
        assertEquals(operation, target.getOperation());
        assertEquals(resultListener, target.getResultListener());
    }
}