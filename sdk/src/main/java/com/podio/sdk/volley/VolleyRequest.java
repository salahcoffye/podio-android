/*
 * Copyright (C) 2015 Citrix Systems, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.podio.sdk.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache.Entry;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.podio.sdk.ApiError;
import com.podio.sdk.ConnectionError;
import com.podio.sdk.json.JsonParser;
import com.podio.sdk.NoResponseError;
import com.podio.sdk.PodioError;
import com.podio.sdk.Session;
import com.podio.sdk.domain.File;
import com.podio.sdk.internal.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VolleyRequest<T> extends Request<T> implements com.podio.sdk.Request<T> {

    private static final class Multipart {
        private final String boundary;
        private final File.PushData file;

        private Multipart(File.PushData file) {
            this.boundary = UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9 ]", "");
            this.file = file;
        }

        private byte[] constructBody() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            char[] crlf = { 13, 10 };

            byte[] bytes = new StringBuilder()
                    /* boundary */
                    .append(crlf)
                    .append(crlf)
                    .append("--")
                    .append(boundary)
                    /* file name part */
                    .append(crlf)
                    .append("Content-Disposition: form-data; name=\"filename\"")
                    .append(crlf)
                    .append("Content-Type: text/plain; charset=\"utf-8\"")
                    .append(crlf)
                    .append(crlf)
                    .append(file.getName())
                    /* boundary */
                    .append(crlf)
                    .append("--")
                    .append(boundary)
                    /* source part */
                    .append(crlf)
                    .append("Content-Disposition: form-data; name=\"source\"; filename=\"")
                    .append(file.getFile().getName())
                    .append("\"")
                    .append(crlf)
                    .append("Content-Type: ")
                    .append(file.getMimeType())
                    .append(crlf)
                    .append("Content-Transfer-Encoding: binary")
                    .append(crlf)
                    .append(crlf).toString().getBytes();

            // Write the multipart data to the byte array stream.
            byteArrayOutputStream.write(bytes, 0, bytes.length);

            // Write the file content to the byte array stream.
            byte[] buffer = new byte[1024];
            int readCount;
            FileInputStream fileInputStream = new FileInputStream(file.getFile());

            while ((readCount = fileInputStream.read(buffer, 0, buffer.length)) > 0) {
                byteArrayOutputStream.write(buffer, 0, readCount);
            }

            fileInputStream.close();

            // Write the closing boundary to the byte array stream.
            bytes = new StringBuilder()
                    .append(crlf)
                    .append("--")
                    .append(boundary)
                    .append("--")
                    .append(crlf).toString().getBytes();

            byteArrayOutputStream.write(bytes, 0, bytes.length);

            return byteArrayOutputStream.toByteArray();
        }
    }

    public static ErrorListener addGlobalErrorListener(ErrorListener errorListener) {
        return VolleyCallbackManager.addGlobalErrorListener(errorListener);
    }

    public static SessionListener addGlobalSessionListener(SessionListener sessionListener) {
        return VolleyCallbackManager.addGlobalSessionListener(sessionListener);
    }

    public static ErrorListener removeGlobalErrorListener(ErrorListener errorListener) {
        return VolleyCallbackManager.addGlobalErrorListener(errorListener);
    }

    public static SessionListener removeGlobalSessionListener(SessionListener sessionListener) {
        return VolleyCallbackManager.removeGlobalSessionListener(sessionListener);
    }

    static <E> VolleyRequest<E> newRequest(com.podio.sdk.Request.Method method, String url, String body, Class<E> classOfResult) {
        int volleyMethod = parseMethod(method);

        VolleyRequest<E> request = new VolleyRequest<E>(volleyMethod, url, classOfResult, false);
        request.contentType = "application/json; charset=UTF-8";
        request.headers.put("X-Time-Zone", Calendar.getInstance().getTimeZone().getID());
        request.body = Utils.notEmpty(body) ? body.getBytes() : null;

        return request;
    }

    static VolleyRequest<Void> newAuthRequest(String url, Map<String, String> params) {
        int volleyMethod = parseMethod(com.podio.sdk.Request.Method.POST);

        VolleyRequest<Void> request = new VolleyRequest<Void>(volleyMethod, url, null, true);
        request.contentType = "application/x-www-form-urlencoded; charset=UTF-8";
        request.params.putAll(params);

        return request;
    }

    static <E> VolleyRequest<E> newUploadRequest(String url, File.PushData file, Class<E> classOfResult) {
        Multipart multipart = new Multipart(file);

        byte[] body;

        try {
            body = multipart.constructBody();
        } catch (IOException e) {
            body = null;
        }

        int volleyMethod = parseMethod(com.podio.sdk.Request.Method.POST);
        int contentLength = body != null ? body.length : 0;

        VolleyRequest<E> request = new VolleyRequest<E>(volleyMethod, url, classOfResult, false);
        request.contentType = "multipart/form-data; boundary=\"" + multipart.boundary + "\"";
        request.headers.put("Content-Length", Integer.toString(contentLength, 10));
        request.headers.put("X-Time-Zone", Calendar.getInstance().getTimeZone().getID());
        request.body = body;

        return request;
    }

    protected static int parseMethod(com.podio.sdk.Request.Method method) {
        switch (method) {
            case DELETE:
                return com.android.volley.Request.Method.DELETE;
            case GET:
                return com.android.volley.Request.Method.GET;
            case POST:
                return com.android.volley.Request.Method.POST;
            case PUT:
                return com.android.volley.Request.Method.PUT;
            default:
                return com.android.volley.Request.Method.GET;
        }
    }

    private final VolleyCallbackManager<T> callbackManager;

    private final Class<T> classOfResult;

    protected HashMap<String, String> headers;
    protected HashMap<String, String> params;
    protected String contentType;
    protected byte[] body;

    private T result;
    private PodioError error;
    private boolean isDone;
    private boolean isAuthRequest;
    private boolean hasSessionChanged;

    protected VolleyRequest(int method, String url, Class<T> resultType, boolean isAuthRequest) {
        super(method, url, null);
        setShouldCache(false);

        this.callbackManager = new VolleyCallbackManager<T>();
        this.classOfResult = resultType;

        this.headers = new HashMap<String, String>();
        this.params = new HashMap<String, String>();
        this.body = null;

        this.hasSessionChanged = false;
        this.isAuthRequest = isAuthRequest;
    }

    @Override
    public VolleyRequest<T> withResultListener(ResultListener<T> resultListener) {
        callbackManager.addResultListener(resultListener, isDone, result);
        return this;
    }

    @Override
    public VolleyRequest<T> withErrorListener(ErrorListener errorListener) {
        callbackManager.addErrorListener(errorListener, isDone && error != null, error);
        return this;
    }

    @Override
    public VolleyRequest<T> withSessionListener(SessionListener sessionListener) {
        callbackManager.addSessionListener(sessionListener, isDone && hasSessionChanged);
        return this;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return Utils.notEmpty(body) ? body : super.getBody();
    }

    @Override
    public String getBodyContentType() {
        return contentType;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        String accessToken = Session.accessToken();

        if (!isAuthRequest && Utils.notEmpty(accessToken)) {
            headers.put("Authorization", "Bearer " + accessToken);
        } else {
            headers.remove("Authorization");
        }

        return headers;
    }

    @Override
    public synchronized T waitForResult(long maxSeconds) throws PodioError {
        // This is still awkward as we might end up blocking the delivery of
        // a client side error (see
        try {
            wait(TimeUnit.SECONDS.toMillis(Math.max(maxSeconds, 0)));
        } catch (InterruptedException e) {
            callbackManager.deliverError(e);
        }

        if (error != null) {
            throw error;
        }

        return result;
    }

    @Override
    public void deliverError(VolleyError error) {
        // This method is executed on the main thread. Extra care should be
        // taken on what is done here.
        isDone = true;
        callbackManager.deliverError(this.error);
    }

    @Override
    protected void deliverResponse(T result) {
        // This method is executed on the main thread. Extra care should be
        // taken on what is done here.

        isDone = true;

        if (hasSessionChanged) {
            callbackManager.deliverSession();
        }

        callbackManager.deliverResult(result);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params.containsKey("refresh_token")) {
            String refreshToken = Session.refreshToken();
            params.put("refresh_token", refreshToken);
        }

        return params;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        // This method is executed on the worker thread. It's "safe" to perform
        // JSON parsing here.

        if (volleyError instanceof NoConnectionError && volleyError.getCause() instanceof UnknownHostException) {
            error = new ConnectionError(volleyError);
        } else if (volleyError instanceof TimeoutError) {
            error = new NoResponseError(volleyError);
        } else {
            String errorJson = getResponseBody(volleyError.networkResponse);
            int responseCode = getResponseCode(volleyError.networkResponse);

            if (Utils.notEmpty(errorJson) && responseCode > 0) {
                error = new ApiError(errorJson, responseCode, volleyError);
            } else {
                error = new PodioError(volleyError);
            }
        }

        synchronized (this) {
            notifyAll();
        }

        return volleyError;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse networkResponse) {
        // This method is executed on the worker thread. It's "safe" to perform
        // JSON parsing here.
        Response<T> response;

        try {
            Entry cacheHeaders = HttpHeaderParser.parseCacheHeaders(networkResponse);
            String charSet = HttpHeaderParser.parseCharset(networkResponse.headers);
            String json = new String(networkResponse.data, charSet);

            if (isAuthRequest) {
                Session.set(json);
                hasSessionChanged = true;
                result = null;
                response = Response.success(null, cacheHeaders);
            } else if (classOfResult == null || classOfResult == Void.class) {
                result = null;
                response = Response.success(null, cacheHeaders);
            } else {
                result = JsonParser.fromJson(json, classOfResult);
                response = Response.success(result, cacheHeaders);
            }
        } catch (UnsupportedEncodingException e) {
            // The provided response JSON is provided with an unknown char-set.
            result = null;
            response = Response.error(new ParseError(e));
        }

        synchronized (this) {
            notifyAll();
        }

        return response;
    }

    public ErrorListener removeErrorListener(ErrorListener errorListener) {
        return callbackManager.removeErrorListener(errorListener);
    }

    public ResultListener<T> removeResultListener(ResultListener<T> resultListener) {
        return callbackManager.removeResultListener(resultListener);
    }

    public SessionListener removeSessionListener(SessionListener sessionListener) {
        return callbackManager.removeSessionListener(sessionListener);
    }

    private String getResponseBody(NetworkResponse networkResponse) {
        try {
            String charSet = HttpHeaderParser.parseCharset(networkResponse.headers);
            return new String(networkResponse.data, charSet);
        } catch (UnsupportedEncodingException e) {
            // The provided error JSON is provided with an unknown char-set.
            return null;
        } catch (NullPointerException e) {
            // For some reason the VolleyError didn't provide a networkResponse.
            return null;
        }
    }

    private int getResponseCode(NetworkResponse networkResponse) {
        return networkResponse != null ? networkResponse.statusCode : 0;
    }
}