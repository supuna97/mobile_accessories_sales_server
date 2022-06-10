package com.icbt.ap.mobileaccessoriessales.util.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@Data
@SuperBuilder
public class NotificationDTO {
    private String message;
    private String topic;
    private String destination;
}
