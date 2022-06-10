package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.Customer;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

public interface CustomerRepository extends CrudRepository<String, Customer> {
    Customer findByCustomerName(String name);
}
