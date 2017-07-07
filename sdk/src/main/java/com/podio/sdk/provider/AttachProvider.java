
/*
 * Copyright (C) 2017 Citrix Systems, Inc
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

package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.File;

/**
 * This class provides methods to attach Files API area.
 *
 */
public class AttachProvider extends Provider {

    public static class FileFilter extends Filter {

        protected FileFilter() {
            super("file");
        }

        Filter withFileId(long fileId) {
            addPathSegment(String.valueOf(fileId));
            addPathSegment("attach");
            return this;
        }
    }

    /**
     * Attach a file
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<File.Attach> attachFile(long fileId,String refType,int refId) {
        FileFilter filter = new FileFilter();
        filter.withFileId(fileId);
        File.Attach attach = new File.Attach(refType,refId);
        return post(filter, attach, File.Attach.class);
    }

}
