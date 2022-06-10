package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.SalesAgentResponse;
import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import com.icbt.ap.mobileaccessoriessales.dto.CommonResponseDTO;
import com.icbt.ap.mobileaccessoriessales.dto.ContentResponseDTO;
import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.SalesAgentSaveRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.SalesAgentUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.enums.UserRole;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.service.SalesAgentService;
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

@RestController
@RequestMapping(value = ApiConstant.VERSION + "/sales_agent")
@Slf4j
@RequiredArgsConstructor
public class SalesAgentController implements CommonController {
    private final SalesAgentService salesAgentService;
    private final MessageSource messageSource;

    @GetMapping(path = "")
    public ResponseEntity<ContentResponseDTO<List<SalesAgentResponse>>> getSalesAgentss() {
        log.info("Get all sales Agents");
        return getAllSalesAgents();
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<ContentResponseDTO<SalesAgentResponse>> getSalesAgent(
            @PathVariable(name = "userId") String userId) {

        log.info("Get sales agent by id, User id: {}", userId);
        return getSalesAgentById(userId);
    }

    @PostMapping(path = "")
    public ResponseEntity<CommonResponseDTO> saveSalesAgent(@Valid @RequestBody SalesAgentSaveRequest request) {
        log.info("Add new sales agent, Agent: {}", request);
        return addNewSalesAgent(request);
    }

    @PutMapping(path = "")
    public ResponseEntity<CommonResponseDTO> updateSalesAgent(@Valid @RequestBody SalesAgentUpdateRequest request) {
        log.info("Update sales agent, Agent: {}", request);
        return modifySalesAgent(request);
    }

    @DeleteMapping(path = "/{userId}")
    public ResponseEntity<CommonResponseDTO> deleteSalesAgent(@PathVariable(name = "userId") String userId) {
        log.info("Delete sales agent by id, Agent id: {}", userId);
        return deleteSalesAgentTmp(userId);
    }

    /*Internal functions*/

    private ResponseEntity<ContentResponseDTO<List<SalesAgentResponse>>> getAllSalesAgents() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getSalesAgentResponseList(salesAgentService.getAll())));
    }

    private ResponseEntity<ContentResponseDTO<SalesAgentResponse>> getSalesAgentById(String userId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getSalesAgentResponse(salesAgentService.getById(userId))));
    }

    private ResponseEntity<CommonResponseDTO> addNewSalesAgent(SalesAgentSaveRequest request) {
        salesAgentService.add(getSalesAgentSaveEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.added.code"),
                getMessage("success.confirmation.branch.added.message")),
                HttpStatus.CREATED);
    }

    private ResponseEntity<CommonResponseDTO> modifySalesAgent(SalesAgentUpdateRequest request) {
        validateUpdateSalesAgentRequest(request);
        salesAgentService.update(getSalesAgentUpdateEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.branch.updated.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<CommonResponseDTO> deleteSalesAgentTmp(String userId) {
        salesAgentService.delete(userId);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.branch.deleted.message")),
                HttpStatus.OK);
    }

    private List<SalesAgentResponse> getSalesAgentResponseList(List<User> users) {
        return users
                .stream()
                .map(this::getSalesAgentResponse)
                .collect(Collectors.toList());
    }

    private SalesAgentResponse getSalesAgentResponse(User user) {
        return SalesAgentResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .branchId(user.getBranchId())
                .build();
    }

    private User getSalesAgentSaveEntity(SalesAgentSaveRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .userRole(UserRole.BRANCH_ADMIN)
                .branchId(request.getBranchId())
                .build();
    }

    private User getSalesAgentUpdateEntity(SalesAgentUpdateRequest request) {
        return User.builder()
                .id(request.getId())
                .username(request.getUsername())
                .password(request.getPassword())
                .userRole(UserRole.BRANCH_ADMIN)
                .branchId(request.getBranchId())
                .build();
    }

    private void validateUpdateSalesAgentRequest(SalesAgentUpdateRequest request) {
        if ((request.getUsername() == null) && (request.getPassword() == null) && (request.getBranchId() == null))
            throw new CustomServiceException(
                    "error.validation.common.not.found.code",
                    "error.validation.common.status.not.found.message"
            );
    }

    private String getFormattedDateTime(LocalDateTime dateTime) {
        return ApiConstant.DATE_TIME_FORMATTER.format(dateTime);
    }

    private String getCode(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, new Object[0], Locale.getDefault());
    }
}
