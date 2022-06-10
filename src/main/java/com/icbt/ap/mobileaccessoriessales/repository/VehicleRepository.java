package com.icbt.ap.mobileaccessoriessales.repository;

import com.icbt.ap.mobileaccessoriessales.entity.Vehicle;
import com.icbt.ap.mobileaccessoriessales.repository.main.CrudRepository;

public interface VehicleRepository extends CrudRepository<String, Vehicle> {
    Vehicle findByRegNo(String regNo);
}
