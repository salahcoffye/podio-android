
package com.podio.sdk.provider;

import com.podio.sdk.Filter;
import com.podio.sdk.Provider;
import com.podio.sdk.Request;
import com.podio.sdk.domain.NotificationGroup;
import com.podio.sdk.domain.ReferenceType;
import com.podio.sdk.domain.notification.Notification;
import com.podio.sdk.domain.notification.NotificationInboxCount;

/**
 * Enables access to the NotificationGroup API end point.
 *
 */
public class NotificationProvider extends Provider {

    public static class NotificationFilter extends Filter {

        protected NotificationFilter() {
            super("notification");
        }

        public NotificationFilter withReferenceType(ReferenceType referenceType) {
            addPathSegment(referenceType.name());
            return this;
        }

        public NotificationFilter withId(long id) {
            addPathSegment(Long.toString(id, 10));
            return this;
        }

        public NotificationFilter withViewed() {
            addPathSegment("viewed");
            return this;
        }

        public NotificationFilter withInboxNewCount() {
            addPathSegment("inbox");
            addPathSegment("new");
            addPathSegment("count");
            return this;
        }
    }

    /**
     * This builder class is used in order to specify which parameters to send in our API request
     * for notifications
     *
     */
    public static class GetNotificationFilter extends Filter {

        public static enum Viewed {
            ALL, UNVIEWED_ONLY, VIEWED_ONLY;
        }

        public static enum Direction {
            INCOMING, OUTGOING;
        }

        public static enum Starred {
            STARRED, UNSTARRED, BOTH
        }

        public GetNotificationFilter() {
            super("notification");
        }

        /**
         * The type of the context to get notifications, e.g. "conversation", "item" or "task",
         * etc.
         *
         * @param referenceType
         *         the type of reference
         *
         * @return
         */
        public GetNotificationFilter contextType(ReferenceType referenceType) {
            addQueryParameter("context_type", referenceType.name());
            return this;
        }

        // TODO support SDK operation
        public GetNotificationFilter createdFrom() {
            throw new UnsupportedOperationException("not implemented yet");
        }

        // TODO support SDK operation
        public GetNotificationFilter createdTo() {
            throw new UnsupportedOperationException("not implemented yet");
        }

        /**
         * "incoming" to get incoming notifications, "outgoing" to get outgoing notifications.
         * Default value: incoming.
         *
         * @param direction
         */
        public GetNotificationFilter direction(Direction direction) {
            switch (direction) {
                case INCOMING:
                    addQueryParameter("direction", "incoming");
                    break;
                case OUTGOING:
                    addQueryParameter("direction", "outgoing");
                    break;
            }
            return this;
        }

        /**
         * Set the maximum number of notifications to return, maximum is "100". Default value: 20
         *
         * @param value
         *         the value of the limit
         *
         * @return
         */
        public GetNotificationFilter limit(int value) {
            addQueryParameter("limit", Integer.toString(value));
            return this;
        }

        /**
         * Set the offset into the returned notifications. Default value: 0
         *
         * @param value
         *         the value of the offset
         *
         * @return
         */
        public GetNotificationFilter offset(int value) {
            addQueryParameter("offset", Integer.toString(value));
            return this;
        }

        /**
         * @param starred
         *         default behaviour if not specified is BOTH, which means both starred and
         *         unstarred notifications is provided.
         *
         * @return
         */
        public GetNotificationFilter starred(Starred starred) {
            switch (starred) {
                case BOTH:
                    //do nothing - leave blank
                    break;
                case STARRED:
                    addQueryParameter("starred", "true");
                    break;
                case UNSTARRED:
                    addQueryParameter("starred", "false");
                    break;
            }
            return this;
        }

        public GetNotificationFilter type(Notification.NotificationType type) {
            addQueryParameter("type", type.name());
            return this;
        }

        /**
         * Set the user id of the other part of the notification
         *
         * @param userId
         *
         * @return
         */
        public GetNotificationFilter userId(long userId) {
            addQueryParameter("user_id", Long.toString(userId));
            return this;
        }

        /**
         * @param viewed
         *         default behaviour if not specified is ALL, which means both unread and read
         *         notifications is provided.
         *
         * @return
         */
        public GetNotificationFilter viewed(Viewed viewed) {
            switch (viewed) {
                case ALL:
                    //do nothing - leave blank
                    break;
                case UNVIEWED_ONLY:
                    addQueryParameter("viewed", "false");
                    break;
                case VIEWED_ONLY:
                    addQueryParameter("viewed", "true");
                    break;
            }
            return this;
        }

        // TODO support SDK operation
        public GetNotificationFilter viewedFrom() {
            throw new UnsupportedOperationException("not implemented yet");
        }

    }

    /**
     * Returns the number of unread notifications for the active user.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<NotificationInboxCount> getInboxNewCount() {
        NotificationFilter filter = new NotificationFilter().withInboxNewCount();
        return get(filter, NotificationInboxCount.class);
    }

    /**
     * Fetches the NotificationGroup with the given id.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<NotificationGroup> getNotification(long id) {
        NotificationFilter filter = new NotificationFilter().withId(id);
        return get(filter, NotificationGroup.class);
    }

    public Request<NotificationGroup[]> getNotifications(GetNotificationFilter filter) {
        return get(filter, NotificationGroup[].class);
    }

    /**
     * Marks all the users notifications as viewed.
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> markAllNotificationsAsViewed() {
        NotificationFilter filter = new NotificationFilter().withViewed();
        return post(filter, null, Void.class);
    }

    /**
     * Mark the notification as viewed. This will move the notification from the inbox to the viewed
     * archive.
     *
     * @param notificationId
     *         The ID of the notification
     *
     * @return A ticket which the caller can use to identify this request with.
     */
    public Request<Void> markNotificationAsViewed(long notificationId) {
        NotificationFilter filter = new NotificationFilter().withId(notificationId).withViewed();
        return post(filter, null, Void.class);

    }

    public Request<Void> markNotificationAsViewed(long referenceId, ReferenceType referenceType) {
        NotificationFilter filter = new NotificationFilter().withReferenceType(referenceType).withId(referenceId).withViewed();
        return post(filter, null, Void.class);
    }
}
