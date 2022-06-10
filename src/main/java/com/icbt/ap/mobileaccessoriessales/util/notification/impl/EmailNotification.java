package com.icbt.ap.mobileaccessoriessales.util.notification.impl;

import com.icbt.ap.mobileaccessoriessales.util.notification.NotificationSender;
import com.icbt.ap.mobileaccessoriessales.util.notification.dto.NotificationDTO;


public class EmailNotification implements NotificationSender {
    private static EmailNotification emailNotification;

    /**
     * private constrictor to initialize properties related to email configurations.
     */
    private EmailNotification() {
        /*TODO
         *Initialize the java mail sender*/
    }

    /**
     * @return the EmailNotification instance which is initialized only once.
     */
    public static EmailNotification getInstance() {
        if (emailNotification == null) {
            emailNotification = new EmailNotification();
        }
        return emailNotification;
    }

    /**
     * @param notificationDTO send notification details
     */
    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        throw new UnsupportedOperationException("Email notification is not implemented yet!");
    }
}
