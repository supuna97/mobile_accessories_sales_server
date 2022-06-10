package com.icbt.ap.mobileaccessoriessales.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(callSuper = true)
public class CommonResponseDTO {
    private boolean success;
    private String code;
    private String message;
}
