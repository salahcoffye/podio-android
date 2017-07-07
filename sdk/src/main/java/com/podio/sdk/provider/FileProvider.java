
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.File;

/**
 * This class provides methods to access Files API area.
 *
 */
public class FileProvider extends Provider {

    public static class FileFilter extends Filter {

        protected FileFilter() {
            super("file");
        }

        Filter withFileId(long fileId) {
            addPathSegment(fileId+"/attach");
            return this;
        }
    }

    /**
     * Uploads a new file
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<File> uploadFile(java.io.File file) {
        FileFilter filter = new FileFilter();
        return post(filter, file, File.class);
    }


    /**
     * Attach a file
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<File.Attach> attachFile(long fileId,String refType,String refId) {
        FileFilter filter = new FileFilter();
        filter.withFileId(fileId);
        File.Attach attach = new File.Attach(refType,refId);
        return post(filter, attach, File.Attach.class);
    }

}
