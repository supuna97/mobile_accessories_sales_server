package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.entity.Vehicle;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.VehicleRepository;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import com.icbt.ap.mobileaccessoriessales.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final BranchService branchService;

    @Override
    public void add(Vehicle vehicle) {
        /*checks whether the vehicle reg no already exists*/
        final Vehicle vehicleByNo = vehicleRepository.findByRegNo(vehicle.getRegNo());
        if (vehicleByNo != null) throwVehicleNameAlreadyExistException();
        /*validates the branch and explicitly sets the queried branch id*/
        final Branch branch = branchService.getById(vehicle.getBranchId());
        vehicle.setBranchId(branch.getId());
        vehicleRepository.save(vehicle);
    }

    @Override
    public void update(Vehicle vehicle) {
        /*validates the incoming data*/
        final Vehicle vehicleById = getById(vehicle.getId());
        /*checks whether the vehicle name already exists*/
        final Vehicle vehicleByNo = vehicleRepository.findByRegNo(vehicle.getRegNo());
        if ((vehicleByNo != null) && (!vehicleByNo.getId().equals(vehicle.getId())))
            throwVehicleNameAlreadyExistException();

        vehicleById.setRegNo(vehicle.getRegNo());
        vehicleById.setBranchId(vehicle.getBranchId());
        vehicleById.setDriverId(vehicle.getDriverId());
        vehicleById.setBranchId(vehicle.getBranchId());

        vehicleRepository.update(vehicleById);
    }

    @Override
    public void delete(String id) {
        final Vehicle vehicle = getById(id);
        vehicleRepository.delete(vehicle.getId());
    }

    @Override
    public Vehicle getById(String id) {
        return vehicleRepository.findById(id).orElseThrow(() -> new CustomServiceException(
                "error.validation.common.not.found.code",
                "error.validation.vehicle.not.found.message"
        ));
    }

    @Override
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }


    /*Internal functions below*/

    private void throwVehicleNameAlreadyExistException() {
        throw new CustomServiceException(
                "error.validation.common.already.exist.code",
                "error.validation.vehicle.regno.already.exist.message"
        );
    }

}
