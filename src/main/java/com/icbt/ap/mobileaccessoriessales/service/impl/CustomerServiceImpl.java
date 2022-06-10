package com.icbt.ap.mobileaccessoriessales.service.impl;

import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.CustomerLoginRequest;
import com.icbt.ap.mobileaccessoriessales.entity.Customer;
import com.icbt.ap.mobileaccessoriessales.exception.CustomAuthException;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.repository.CustomerRepository;
import com.icbt.ap.mobileaccessoriessales.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer authenticate(CustomerLoginRequest loginRequest) {
        final Customer customer = customerRepository.findByCustomerName(loginRequest.getUsername());
        if (customer == null) throw new CustomAuthException(
                "error.validation.unauthorized.code",
                "error.validation.username.not.found.message",
                new String[]{loginRequest.getUsername()}
        );
        if (!passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword()))
            throw new CustomAuthException(
                    "error.validation.unauthorized.code",
                    "error.validation.invalid.password.message",
                    new String[]{}
            );
        customer.setPassword(null);
        return customer;
    }

    @Override
    public void add(Customer customer) {
        /*checks whether the user name already exists*/
        final Customer customerByUserName = customerRepository.findByCustomerName(customer.getUsername());
        if (customerByUserName != null) throwUserNameAlreadyExistException();
        /*checks whether the given branch id exists and sets explicitly*/
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
    }

    @Override
    public void update(Customer customer) {
        /*validates the incoming data*/
        final Customer customerById = getById(customer.getId());
        /*checks whether the user name already exists*/
        final Customer customerByUserName = customerRepository.findByCustomerName(customer.getUsername());
        if ((customerByUserName != null) && (!customerByUserName.getId().equals(customer.getId())))
            throwUserNameAlreadyExistException();

        customerById.setName(customer.getName());
        customerById.setMobile(customer.getMobile());
        customerById.setAddress(customer.getAddress());
        customerById.setUsername(customer.getUsername());
        customerById.setPassword(passwordEncoder.encode(customer.getPassword()));

        customerRepository.update(customerById);
    }

    @Override
    public void delete(String id) {
        final Customer customer = getById(id);
        customerRepository.delete(customer.getId());
    }

    @Override
    public Customer getById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new CustomServiceException(
                "error.validation.common.not.found.code",
                "error.validation.userid.not.found.message",
                new String[]{id}
        ));
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    /*Internal functions below*/

    private void throwUserNameAlreadyExistException() {
        throw new CustomServiceException(
                "error.validation.common.already.exist.code",
                "error.validation.user.name.already.exist.message"
        );
    }
}
