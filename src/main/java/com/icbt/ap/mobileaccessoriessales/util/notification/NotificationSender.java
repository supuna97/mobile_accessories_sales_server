package com.icbt.ap.mobileaccessoriessales.util.notification;

import com.icbt.ap.mobileaccessoriessales.util.notification.dto.NotificationDTO;

public interface NotificationSender {
    void sendNotification(NotificationDTO notificationDTO);
}
