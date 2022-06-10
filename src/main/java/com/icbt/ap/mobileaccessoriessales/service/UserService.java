package com.icbt.ap.mobileaccessoriessales.service;

import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.UserLoginRequest;
import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.service.main.CrudService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends CrudService<String, User> {

    User authenticate(UserLoginRequest loginRequest);
}
