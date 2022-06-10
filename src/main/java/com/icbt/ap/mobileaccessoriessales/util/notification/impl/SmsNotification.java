package com.icbt.ap.mobileaccessoriessales.util.notification.impl;

import com.icbt.ap.mobileaccessoriessales.util.notification.NotificationSender;
import com.icbt.ap.mobileaccessoriessales.util.notification.dto.NotificationDTO;

public class SmsNotification implements NotificationSender {
    private static SmsNotification smsNotification;

    /**
     * private constrictor to initialize properties related to email configurations.
     */
    private SmsNotification() {
    }


    /**
     * @return the SmsNotification instance which is initialized only once.
     */
    public static SmsNotification getInstance() {
        if (smsNotification == null) {
            smsNotification = new SmsNotification();
        }
        return smsNotification;
    }

    /**
     * @param notificationDTO send notification details
     */
    @Override
    public void sendNotification(NotificationDTO notificationDTO) {
        throw new UnsupportedOperationException("SMS notification is not implemented yet!");
    }
}
