package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.SalesAgentRepository;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
import com.icbt.ap.mobileaccessoriessales.service.SalesAgentService;
import com.icbt.ap.mobileaccessoriessales.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SalesAgentServiceImpl implements SalesAgentService {
    private final SalesAgentRepository salesAgentRepository;
    private final BranchService branchService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void add(User user) {
        /*checks whether the sales agent name already exists*/
        final User userByUserName = salesAgentRepository.findByUserName(user.getUsername());
        if (userByUserName != null) throwUserNameAlreadyExistException();
        /*checks whether the given branch id exists and sets explicitly*/
        final Branch branch = branchService.getById(user.getBranchId());
        user.setBranchId(branch.getId());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        salesAgentRepository.save(user);
    }

    @Override
    public void update(User user) {
        /*validates the incoming data*/
        final User userById = getById(user.getId());
        /*checks whether the sales agent name already exists*/
        final User userByUserName = salesAgentRepository.findByUserName(user.getUsername());
        if ((userByUserName != null) && (!userByUserName.getId().equals(user.getId())))
            throwUserNameAlreadyExistException();
        if (StringUtil.isNotBlank(user.getBranchId())) {
            final Branch branch = branchService.getById(user.getBranchId());
            user.setBranchId(branch.getId());
        }

        userById.setUsername(user.getUsername());
        userById.setPassword(passwordEncoder.encode(user.getPassword()));
        userById.setUserRole(user.getUserRole());
        userById.setBranchId(user.getBranchId());

        salesAgentRepository.update(userById);
    }

    @Override
    public void delete(String id) {
        final User user = getById(id);
        salesAgentRepository.delete(user.getId());
    }

    @Override
    public User getById(String id) {
        return salesAgentRepository.findById(id).orElseThrow(() -> new CustomServiceException(
                "error.validation.common.not.found.code",
                "error.validation.userid.not.found.message",
                new String[]{id}
        ));
    }

    @Override
    public List<User> getAll() {
        return salesAgentRepository.findAll();
    }

    /*Internal functions below*/

    private void throwUserNameAlreadyExistException() {
        throw new CustomServiceException(
                "error.validation.common.already.exist.code",
                "error.validation.user.name.already.exist.message"
        );
    }
}
