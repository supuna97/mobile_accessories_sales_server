package com.icbt.ap.mobileaccessoriessales.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString(callSuper = true)
public class ContentResponseDTO<T> {
    private boolean success;
    private T data;
}