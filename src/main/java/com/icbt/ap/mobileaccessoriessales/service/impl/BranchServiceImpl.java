package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.BranchRepository;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BranchServiceImpl implements BranchService {

    private final BranchRepository branchRepository;

    @Override
    public void add(Branch branch) {
        /*checks whether the branch name already exists*/
        final Branch branchByName = branchRepository.findByName(branch.getName());
        if (branchByName != null) throwBranchNameAlreadyExistException();
        final Branch branchByTel = branchRepository.findByTel(branch.getTel());
        if (branchByTel != null) throwBranchTelAlreadyExistException();

        branchRepository.save(branch);
    }

    @Override
    public void update(Branch branch) {
        /*validates the incoming data*/
        final Branch branchById = getById(branch.getId());
        /*checks whether the branch name already exists*/
        final Branch branchByName = branchRepository.findByName(branch.getName());
        if ((branchByName != null) && (!branchByName.getId().equals(branch.getId())))
            throwBranchNameAlreadyExistException();
        final Branch branchByTel = branchRepository.findByTel(branch.getTel());
        if ((branchByTel != null) && (!branchByTel.getId().equals(branch.getId())))
            throwBranchTelAlreadyExistException();

        branchById.setName(branch.getName());
        branchById.setAddress(branch.getAddress());
        branchById.setTel(branch.getTel());
        if (branch.getStatus() != null)
            branchById.setStatus(branch.getStatus());

        branchRepository.update(branchById);
    }

    @Override
    public void delete(String id) {
        final Branch branch = getById(id);
        branchRepository.delete(branch.getId());
    }

    @Override
    public Branch getById(String id) {
        return branchRepository.findById(id).orElseThrow(() -> new CustomServiceException(
                "error.validation.common.not.found.code",
                "error.validation.branch.not.found.message"
        ));
    }

    @Override
    public List<Branch> getAll() {
        return branchRepository.findAll();
    }


    /*Internal functions below*/

    private void throwBranchNameAlreadyExistException() {
        throw new CustomServiceException(
                "error.validation.common.already.exist.code",
                "error.validation.branch.name.already.exist.message"
        );
    }

    private void throwBranchTelAlreadyExistException() {
        throw new CustomServiceException(
                "error.validation.common.already.exist.code",
                "error.validation.branch.tel.already.exist.message"
        );
    }
}
