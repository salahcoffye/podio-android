
package com.podio.sdk.domain;

import com.podio.sdk.domain.data.Data;
import com.podio.sdk.internal.Utils;

/**
 */
public class File implements Data{

    private final Long file_id;
    private final Long size;
    private final String description;
    private final String hosted_by;
    private final String hosted_by_humanized_name;
    private final String link;
    private final String link_target;
    private final String mimetype;
    private final String name;
    private final String perma_link;
    private final String thumbnail_link;

    public File(long fileId) {
        this.file_id = fileId;
        this.size = null;
        this.description = null;
        this.hosted_by = null;
        this.hosted_by_humanized_name = null;
        this.link = null;
        this.link_target = null;
        this.mimetype = null;
        this.name = null;
        this.perma_link = null;
        this.thumbnail_link = null;
    }

    public String getDescription() {
        return description;
    }

    public long getId() {
        return Utils.getNative(file_id, -1L);
    }

    public String getHostName() {
        return hosted_by;
    }

    public String getHumanizedHostName() {
        return hosted_by_humanized_name;
    }

    public String getLink() {
        return link;
    }

    public String getLinkTarget() {
        return link_target;
    }

    public String getMimeType() {
        return mimetype;
    }

    public String getName() {
        return name;
    }

    public String getPermaLink() {
        return perma_link;
    }

    public long getSize() {
        return Utils.getNative(size, 0L);
    }

    public String getThumbnailLink() {
        return thumbnail_link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        File file = (File) o;

        return !(file_id != null ? !file_id.equals(file.file_id) : file.file_id != null);

    }

    @Override
    public int hashCode() {
        return file_id != null ? file_id.hashCode() : 0;
    }


    public static class Attach{

        public Attach(String ref_type, int ref_id) {
            this.ref_type = ref_type;
            this.ref_id = ref_id;
        }

        private final String ref_type;
        private final int ref_id;

        public String getRef_type() {
            return ref_type;
        }

        public int getRef_id() {
            return ref_id;
        }

    }
}
