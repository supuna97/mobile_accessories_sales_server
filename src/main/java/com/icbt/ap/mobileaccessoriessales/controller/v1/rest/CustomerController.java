package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.CustomerLoginRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.CustomerSaveRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.CustomerUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.CustomerLoginResponse;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.CustomerResponse;
import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import com.icbt.ap.mobileaccessoriessales.dto.CommonResponseDTO;
import com.icbt.ap.mobileaccessoriessales.dto.ContentResponseDTO;
import com.icbt.ap.mobileaccessoriessales.entity.Customer;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.DATE_TIME_FORMATTER;

@RestController
@RequestMapping(value = ApiConstant.VERSION + "/customer")
@Slf4j
@RequiredArgsConstructor
public class CustomerController implements CommonController {
    private final CustomerService customerService;

    private final MessageSource messageSource;

    @PostMapping(path = "/customer_auth")
    public ResponseEntity<CustomerLoginResponse> authenticate(@Valid @RequestBody CustomerLoginRequest request) {
        log.info("Customer login request, Username: {}", request.getUsername());
        return authenticateUser(request);
    }

    private ResponseEntity<CustomerLoginResponse> authenticateUser(CustomerLoginRequest request) {
        final Customer customer = customerService.authenticate(request);
        return ResponseEntity.ok(CustomerLoginResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobile(customer.getMobile())
                .address(customer.getAddress())
                .username(customer.getUsername())
                .userRole(customer.getUserRole())
                .build()
        );
    }

    @GetMapping(path = "")
    public ResponseEntity<ContentResponseDTO<List<CustomerResponse>>> getCustomerss() {
        log.info("Get all customers");
        return getAllCustomers();
    }

    @GetMapping(path = "/{customerId}")
    public ResponseEntity<ContentResponseDTO<CustomerResponse>> getCustomer(
            @PathVariable(name = "customerId") String customerId) {

        log.info("Get customer by id, customer id: {}", customerId);
        return getCustomerById(customerId);
    }

    @PostMapping(path = "")
    public ResponseEntity<CommonResponseDTO> saveCustomer(@Valid @RequestBody CustomerSaveRequest request) {
        log.info("Add new customer, Customer: {}", request);
        return addNewCustomer(request);
    }

    @PutMapping(path = "")
    public ResponseEntity<CommonResponseDTO> updateCustomer(@Valid @RequestBody CustomerUpdateRequest request) {
        log.info("Update customer, Customer: {}", request);
        return modifyCustomer(request);
    }

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<CommonResponseDTO> deleteCustomer(@PathVariable(name = "customerId") String customerId) {
        log.info("Delete customer by id, Customer id: {}", customerId);
        return deleteCustomerTmp(customerId);
    }


    /*Internal functions*/

    private ResponseEntity<ContentResponseDTO<List<CustomerResponse>>> getAllCustomers() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getCustomerResponseList(customerService.getAll())));
    }

    private ResponseEntity<ContentResponseDTO<CustomerResponse>> getCustomerById(String customerId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getCustomerResponse(customerService.getById(customerId))));
    }

    private ResponseEntity<CommonResponseDTO> addNewCustomer(CustomerSaveRequest request) {
        customerService.add(getCustomerSaveEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.added.code"),
                getMessage("success.confirmation.branch.added.message")),
                HttpStatus.CREATED);
    }

    private ResponseEntity<CommonResponseDTO> modifyCustomer(CustomerUpdateRequest request) {
        validateUpdateCustomerRequest(request);
        customerService.update(getCustomerUpdateEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.branch.updated.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<CommonResponseDTO> deleteCustomerTmp(String customerId) {
        customerService.delete(customerId);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.branch.deleted.message")),
                HttpStatus.OK);
    }

    private List<CustomerResponse> getCustomerResponseList(List<Customer> customers) {
        return customers
                .stream()
                .map(this::getCustomerResponse)
                .collect(Collectors.toList());
    }

    private CustomerResponse getCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .mobile(customer.getMobile())
                .address(customer.getAddress())
                .username(customer.getUsername())
                .build();
    }

    private Customer getCustomerSaveEntity(CustomerSaveRequest request) {
        return Customer.builder()
                .name(request.getName())
                .mobile(request.getMobile())
                .address(request.getAddress())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    private Customer getCustomerUpdateEntity(CustomerUpdateRequest request) {
        return Customer.builder()
                .id(request.getId())
                .name(request.getName())
                .mobile(request.getMobile())
                .address(request.getAddress())
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }

    private void validateUpdateCustomerRequest(CustomerUpdateRequest request) {
        if ((request.getName() == null)
                && (request.getMobile() == null) && (request.getAddress() == null) && (request.getUsername() == null)
                && (request.getPassword() == null))
            throw new CustomServiceException(
                    "error.validation.common.not.found.code",
                    "error.validation.common.status.not.found.message"
            );
    }

    private String getFormattedDateTime(LocalDateTime dateTime) {
        return DATE_TIME_FORMATTER.format(dateTime);
    }

    private String getCode(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }
}
