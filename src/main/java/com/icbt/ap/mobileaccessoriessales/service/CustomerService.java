package com.icbt.ap.mobileaccessoriessales.service;

import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.CustomerLoginRequest;
import com.icbt.ap.mobileaccessoriessales.entity.Customer;
import com.icbt.ap.mobileaccessoriessales.service.main.CrudService;

public interface CustomerService extends CrudService<String, Customer> {
    Customer authenticate(CustomerLoginRequest loginRequest);
}
