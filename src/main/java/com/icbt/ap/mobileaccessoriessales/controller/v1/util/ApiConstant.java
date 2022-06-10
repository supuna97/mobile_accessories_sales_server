package com.icbt.ap.mobileaccessoriessales.controller.v1.util;

import java.time.format.DateTimeFormatter;

public final class ApiConstant {
    private ApiConstant() {
    }

    public static final String VERSION = "/v1";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    public static final class Validation {
        public static final String ID_REQUIRED = "Id is mandatory";
        public static final String NAME_REQUIRED = "Name is mandatory";
        public static final String ADDRESS_REQUIRED = "Address is mandatory";
        public static final String TEL_REQUIRED = "Tel is mandatory";
        public static final String QTY_REQUIRED = "QTY is mandatory";
        public static final String PRICE_REQUIRED = "Price is mandatory";
        public static final String BRANCH_REQUIRED = "Branch is mandatory";
        public static final String PRODUCT_REQUIRED = "Product is mandatory";
        public static final String BY_BRANCH_REQUIRED = "By branch is mandatory";
        public static final String FOR_BRANCH_REQUIRED = "By branch is mandatory";
        public static final String PRODUCT_DETAILS_REQUIRED = "Product details are mandatory";
        public static final String PRODUCT_ID_REQUIRED = "Product id is mandatory";
        public static final String USERNAME_REQUIRED = "Username is mandatory";
        public static final String PASSWORD_REQUIRED = "Password is mandatory";

        public static final String PATTERN_NUMBER = "^[0-9]*$";
        public static final String PATTERN_DECIMAL = "\\d*\\.?\\d+";

    }
}
