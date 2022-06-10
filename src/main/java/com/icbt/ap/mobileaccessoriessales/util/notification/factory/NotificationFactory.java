package com.icbt.ap.mobileaccessoriessales.util.notification.factory;

import com.icbt.ap.mobileaccessoriessales.util.notification.NotificationSender;
import com.icbt.ap.mobileaccessoriessales.util.notification.impl.EmailNotification;
import com.icbt.ap.mobileaccessoriessales.util.notification.impl.SmsNotification;

public class NotificationFactory {
    private static NotificationFactory notificationFactory;

    private NotificationFactory() {
    }

    public static NotificationFactory getInstance() {
        if (notificationFactory == null) {
            notificationFactory = new NotificationFactory();
        }
        return notificationFactory;
    }

    /**
     * @param type the notification type
     * @return the notification concrete instance of the given type.
     */
    public NotificationSender getNotificationSender(NotificationType type) {
        switch (type) {
            case SMS:
                return SmsNotification.getInstance();
            case EMAIL:
                return EmailNotification.getInstance();
            default:
                throw new UnsupportedOperationException("Given type is not implemented yet");
        }
    }

    public enum NotificationType {
        SMS, EMAIL
    }
}
