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
package com.podio.sdk.domain.push;

import java.util.Date;

import com.podio.sdk.internal.Utils;

public class ConversationEvent extends Event {

    private static class Data {
        /**
         * The id of the conversation.
         */
        private final Long conversation_id = null;

        /**
         * The id of the event.
         */
        private final Long event_id = null;

        /**
         * The action that was performed.
         */
        private final String action = null;

        /**
         * A short excerpt of the event, e.g. a preview of the message.
         */
        private final String text = null;

        /**
         * The date and time when the event was created. This can be compared to
         * the "last_event_on" on the conversation to see if this is never than
         * the already known.
         */
        private final String created_on = null;

        /**
         * he byline of the user who created the event.
         */
        private final String created_by = null;

        /**
         * The number of unread events on the conversation.
         */
        private final Integer unread_count = null;

        /**
         * The total number of unread events on all conversations.
         */
        private final Integer total_unread_count = null;
    }

    private final Data data = null;

    public long conversationId() {
        return data != null ? Utils.getNative(data.conversation_id, -1L) : -1L;
    }

    public long eventId() {
        return data != null ? Utils.getNative(data.event_id, -1L) : -1L;
    }

    public String action() {
        return data != null ? data.action : null;
    }

    public String excerpt() {
        return data != null ? data.text : null;
    }

    public Date createdOnDateTime() {
        return data != null ? Utils.parseDateTime(data.created_on) : null;
    }

    public String createdOnDateTimeString() {
        return data != null ? data.created_on : null;
    }

    public String byline() {
        return data != null ? data.created_by : null;
    }

    public int unreadMessagesCountInConversation() {
        return data != null ? Utils.getNative(data.unread_count, -1) : -1;
    }

    public int unreadMessagesCountInTotal() {
        return data != null ? Utils.getNative(data.total_unread_count, -1) : -1;
    }

}
