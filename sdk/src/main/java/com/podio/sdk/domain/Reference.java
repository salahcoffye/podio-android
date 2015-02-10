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

import com.google.gson.annotations.SerializedName;
import com.podio.sdk.internal.Utils;

public class Reference {
    /**
     * The field type enumeration. Each field can have exactly one of these type values.
     */
    public static enum Type {
        app, app_revision, app_field, item, bulletin, comment,
        status, space_member, alert, item_revision, rating, task,
        task_action, space, org, conversation, message, notification,
        file, file_service, profile, user, widget, share, form,
        auth_client, connection, integration, share_install, icon,
        org_member, hook, tag, embed, question, question_answer,
        action, contract, invoice, payment, batch, system,
        space_member_request, linked_account, subscription,
        label, view, grant, flow, flow_effect, flow_condition,
        live, condition_set, condition, promotion, location,
        voting, answer, vote, item_participation, extension,
        item_transaction, extension_installation, partner, identity,
        undefined
    }

    private final Type type = null;
    @SerializedName("type_name")
    private final String typeName = null;
    private final Long id = null;
    private final String title = null;

    public Type getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    /**
     * @return returns the id of the reference or -1 if for some reason there is no id
     */
    public Long getId() {
        return Utils.getNative(id, -1);
    }

    public String getTitle() {
        return title;
    }

}
