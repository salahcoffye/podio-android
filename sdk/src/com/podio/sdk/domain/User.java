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

package com.podio.sdk.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.podio.sdk.internal.utils.Utils;

public class User {
    private static final transient DateFormat FORMATTER_DATETIME = DateFormat.getDateTimeInstance();

    public static enum Flag {
        god, bulletin_author, app_store_manager, experiment_manager,
        org_viewer, org_manager, api_manager, extension_manager,
        tnol_manager, seo_manager, out_of_office, sales, sales_default,
        sales_large, sales_contract, undefined
    }

    public static enum Status {
        inactive, active, deleted, blocked, blacklisted, undefined
    }

    public static class Email {
        private final Boolean disabled = null;
        private final Boolean primary = null;
        private final Boolean verified = null;
        private final String mail = null;

        public String getAddress() {
            return mail;
        }

        public boolean isDisabled() {
            return Utils.getNative(disabled, false);
        }

        public boolean isPrimary() {
            return Utils.getNative(primary, false);
        }

        public boolean isVerified() {
            return Utils.getNative(verified, false);
        }
    }

    private final Integer user_id = null;
    private final List<Flag> flags = null;
    private final List<Email> mails = null;
    private final Status status = null;
    private final String activated_on = null;
    private final String created_on = null;
    private final String locale = null;
    private final String mail = null;
    private final String timezone = null;

    /**
     * Gets the date when the user was activated.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getActivatedDate() {
        Date date;

        try {
            date = FORMATTER_DATETIME.parse(activated_on);
        } catch (ParseException e) {
            date = null;
        } catch (NullPointerException e) {
            date = null;
        }

        return date;
    }

    public String getActivatedDateString() {
        return activated_on;
    }

    /**
     * Gets the date when the user was created.
     * 
     * @return A date object, or null if the date couldn't be parsed.
     */
    public Date getCreatedDate() {
        Date date;

        try {
            date = FORMATTER_DATETIME.parse(created_on);
        } catch (ParseException e) {
            date = null;
        } catch (NullPointerException e) {
            date = null;
        }

        return date;
    }

    public String getCreatedDateString() {
        return created_on;
    }

    public String getEmailAddress() {
        return mail;
    }

    public List<Email> getEmails() {
        return mails != null ?
                new ArrayList<Email>(mails) :
                new ArrayList<Email>();
    }

    public int getId() {
        return Utils.getNative(user_id, -1);
    }

    public String getLocale() {
        return locale;
    }

    public Status getStatus() {
        return status != null ? status : Status.undefined;
    }

    public String getTimezone() {
        return timezone;
    }

    public boolean hasFlags(Flag... flags) {
        if (this.flags != null) {
            for (Flag flag : flags) {
                if (!this.flags.contains(flag)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
