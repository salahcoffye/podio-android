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
package com.podio.sdk.internal;

import android.os.Handler;
import android.os.Looper;

import com.podio.sdk.PodioError;
import com.podio.sdk.Request.ErrorListener;
import com.podio.sdk.Request.ResultListener;

import java.util.ArrayList;

public class CallbackManager<T> {
    private static final ArrayList<ErrorListener> GLOBAL_ERROR_LISTENERS;

    static {
        GLOBAL_ERROR_LISTENERS = new ArrayList<ErrorListener>();
    }

    public static ErrorListener addGlobalErrorListener(ErrorListener errorListener) {
        return errorListener != null && GLOBAL_ERROR_LISTENERS.add(errorListener) ?
                errorListener :
                null;
    }

    public static ErrorListener removeGlobalErrorListener(ErrorListener errorListener) {
        int index = GLOBAL_ERROR_LISTENERS.indexOf(errorListener);

        return GLOBAL_ERROR_LISTENERS.contains(errorListener) ?
                GLOBAL_ERROR_LISTENERS.remove(index) :
                null;
    }

    private final ArrayList<ResultListener<T>> resultListeners;
    private final ArrayList<ErrorListener> errorListeners;

    public CallbackManager() {
        this.resultListeners = new ArrayList<ResultListener<T>>();
        this.errorListeners = new ArrayList<ErrorListener>();
    }

    public void addErrorListener(ErrorListener listener, boolean deliverErrorNow, Throwable error) {
        if (listener != null) {
            if (deliverErrorNow) {
                listener.onErrorOccured(error);
            } else {
                errorListeners.add(listener);
            }
        }
    }

    public void addResultListener(ResultListener<T> listener, boolean deliverResultNow, T result) {
        if (listener != null) {
            if (deliverResultNow) {
                listener.onRequestPerformed(result);
            } else {
                resultListeners.add(listener);
            }
        }
    }

    public void deliverError(Throwable error) {
        resultListeners.clear();

        if (Utils.isEmpty(errorListeners) && Utils.isEmpty(GLOBAL_ERROR_LISTENERS)) {
            throw new PodioError(error);
        }

        for (ErrorListener listener : errorListeners) {
            if (listener != null) {
                if (listener.onErrorOccured(error)) {
                    // The callback consumed the event, stop the bubbling.
                    errorListeners.clear();
                    return;
                }

                errorListeners.remove(listener);
            }
        }

        for (ErrorListener listener : GLOBAL_ERROR_LISTENERS) {
            if (listener != null) {
                if (listener.onErrorOccured(error)) {
                    // The callback consumed the event, stop the bubbling.
                    return;
                }
            }
        }
    }

    public void deliverErrorOnMainThread(final Throwable error) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                deliverError(error);
            }

        });
    }

    public void deliverResult(T result) {
        errorListeners.clear();

        for (ResultListener<T> listener : resultListeners) {
            if (listener != null) {
                if (listener.onRequestPerformed(result)) {
                    // The callback consumed the event, stop the bubbling.
                    resultListeners.clear();
                    break;
                }

                resultListeners.remove(listener);
            }
        }
    }

    public void deliverResultOnMainThread(final T result) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                deliverResult(result);
            }

        });
    }

    public ResultListener<T> removeResultListener(ResultListener<T> listener) {
        int index = resultListeners.indexOf(listener);

        return resultListeners.contains(listener) ?
                resultListeners.remove(index) :
                null;
    }

    public ErrorListener removeErrorListener(ErrorListener listener) {
        int index = errorListeners.indexOf(listener);

        return errorListeners.contains(listener) ?
                errorListeners.remove(index) :
                null;
    }

}
