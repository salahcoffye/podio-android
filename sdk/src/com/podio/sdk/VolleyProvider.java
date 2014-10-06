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

package com.podio.sdk;

public class VolleyProvider extends Provider {

    protected Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    protected <T> PodioRequest<T> delete(Filter filter) {
        return client.request(PodioRequest.Method.DELETE, filter, null, null);
    }

    protected <T> PodioRequest<T> get(Filter filter, Class<T> classOfResult) {
        return client.request(PodioRequest.Method.GET, filter, null, classOfResult);
    }

    protected <T> PodioRequest<T> post(Filter filter, Object item, Class<T> classOfItem) {
        return client.request(PodioRequest.Method.POST, filter, item, classOfItem);
    }

    protected <T> PodioRequest<T> put(Filter filter, Object item, Class<T> classOfItem) {
        return client.request(PodioRequest.Method.PUT, filter, item, classOfItem);
    }

}