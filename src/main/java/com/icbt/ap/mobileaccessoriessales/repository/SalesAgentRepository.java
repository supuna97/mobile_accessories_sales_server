package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

public interface SalesAgentRepository extends CrudRepository<String, User> {
    User findByUserName(String username);
}
