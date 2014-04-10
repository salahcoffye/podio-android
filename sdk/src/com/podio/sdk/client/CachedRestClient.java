package com.podio.sdk.client;

import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;

import com.podio.sdk.Filter;
import com.podio.sdk.RestClient;
import com.podio.sdk.RestClientDelegate;
import com.podio.sdk.client.delegate.SQLiteClientDelegate;
import com.podio.sdk.internal.request.RestOperation;

/**
 * A RestClient that, when requesting data, returns content from a local
 * database first, before passing the request on to the parent,
 * {@link HttpRestClient}, implementation. For any other operation (basically,
 * pushing or deleting data) the request is relayed directly to the parent
 * implementation.
 * 
 * @author László Urszuly
 */
public class CachedRestClient extends HttpRestClient {
    private static final String DATABASE_NAME = "podio.db";
    private static final int DATABASE_VERSION = 1;

    private final String contentScheme;

    private RestClientDelegate databaseDelegate;
    private ArrayList<RestRequest> delegatedRequests;

    /**
     * Creates a new <code>CachedRestClient</code> with a default pending
     * requests capacity of 10.
     * 
     * @param context
     *            The context in which to operate on the database and network
     *            files.
     * @param authority
     *            The content authority, this authority will apply to both the
     *            database and the network Uri.
     */
    public CachedRestClient(Context context, String authority) {
        this(context, authority, 10);
    }

    /**
     * Creates a new <code>CachedRestClient</code> with the given pending
     * requests capacity.
     * 
     * @param context
     *            The context in which to operate on the database and network
     *            files.
     * @param authority
     *            The content authority, this authority will apply to both the
     *            database and the network Uri.
     * @param queueCapacity
     *            The number of pending request this {@link RestClient} will
     *            keep in its queue.
     */
    public CachedRestClient(Context context, String authority, int queueCapacity) {
        super(context, authority, queueCapacity);
        contentScheme = "content";
        delegatedRequests = new ArrayList<RestRequest>();
        databaseDelegate = new SQLiteClientDelegate(context, DATABASE_NAME, DATABASE_VERSION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected RestResult handleRequest(RestRequest restRequest) {
        RestResult result = null;

        if (restRequest != null) {
            RestOperation operation = restRequest.getOperation();

            if (operation == RestOperation.GET && !delegatedRequests.contains(restRequest)) {
                Filter filter = restRequest.getFilter();

                // Query the locally cached data first...
                if (filter != null) {
                    Uri uri = filter.buildUri(contentScheme, authority);
                    Class<?> itemType = restRequest.getItemType();
                    result = databaseDelegate.get(uri, itemType);
                }

                // ...and then queue the request once again for the super
                // implementation to act upon.
                delegatedRequests.add(restRequest);
                super.perform(restRequest);
            } else {
                delegatedRequests.remove(restRequest);
                result = super.handleRequest(restRequest);
            }
        }

        return result;
    }

    /**
     * Sets the database helper class which will manage the actual database
     * access operations.
     * 
     * @param databaseHelper
     *            The helper implementation.
     */
    public void setDatabaseDelegate(RestClientDelegate databaseDelegate) {
        if (databaseDelegate != null) {
            this.databaseDelegate = databaseDelegate;
        }
    }

}
