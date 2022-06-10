package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.BranchSaveRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.BranchUpdateRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.BranchResponse;
import com.icbt.ap.mobileaccessoriessales.dto.CommonResponseDTO;
import com.icbt.ap.mobileaccessoriessales.dto.ContentResponseDTO;
import com.icbt.ap.mobileaccessoriessales.entity.Branch;
import com.icbt.ap.mobileaccessoriessales.enums.BranchStatus;
import com.icbt.ap.mobileaccessoriessales.enums.BranchType;
import com.icbt.ap.mobileaccessoriessales.exception.CustomServiceException;
import com.icbt.ap.mobileaccessoriessales.service.BranchService;
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
@RequestMapping(value = ApiConstant.VERSION + "/branch")
@Slf4j
@RequiredArgsConstructor
public class BranchController implements CommonController {

    private final BranchService branchService;

    private final MessageSource messageSource;

    @GetMapping(path = "")
    public ResponseEntity<ContentResponseDTO<List<BranchResponse>>> getBranchess() {
        log.info("Get all branches");
        return getAllBranchs();
    }

    @GetMapping(path = "/{branchId}")
    public ResponseEntity<ContentResponseDTO<BranchResponse>> getBranch(
            @PathVariable(name = "branchId") String branchId) {

        log.info("Get branch by id, Branch id: {}", branchId);
        return getBranchById(branchId);
    }

    @PostMapping(path = "")
    public ResponseEntity<CommonResponseDTO> saveBranch(@Valid @RequestBody BranchSaveRequest request) {
        log.info("Add new branch, Branch: {}", request);
        return addNewBranch(request);
    }

    @PutMapping(path = "")
    public ResponseEntity<CommonResponseDTO> updateBranch(@Valid @RequestBody BranchUpdateRequest request) {
        log.info("Update branch, Branch: {}", request);
        return modifyBranch(request);
    }

    @DeleteMapping(path = "/{branchId}")
    public ResponseEntity<CommonResponseDTO> deleteBranch(@PathVariable(name = "branchId") String branchId) {
        log.info("Delete branch by id, Branch id: {}", branchId);
        return deleteBranchTmp(branchId);
    }

    /*Internal functions*/

    private ResponseEntity<ContentResponseDTO<List<BranchResponse>>> getAllBranchs() {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getBranchResponseList(branchService.getAll())));
    }

    private ResponseEntity<ContentResponseDTO<BranchResponse>> getBranchById(String branchId) {
        return ResponseEntity.ok(new ContentResponseDTO<>(true,
                getBranchResponse(branchService.getById(branchId))));
    }

    private ResponseEntity<CommonResponseDTO> addNewBranch(BranchSaveRequest request) {
        branchService.add(getBranchSaveEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.added.code"),
                getMessage("success.confirmation.branch.added.message")),
                HttpStatus.CREATED);
    }

    private ResponseEntity<CommonResponseDTO> modifyBranch(BranchUpdateRequest request) {
        validateUpdateBranchRequest(request);
        branchService.update(getBranchUpdateEntity(request));
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.branch.updated.message")),
                HttpStatus.OK);
    }

    private ResponseEntity<CommonResponseDTO> deleteBranchTmp(String branchId) {
        branchService.delete(branchId);
        return new ResponseEntity<>(new CommonResponseDTO(true,
                getCode("success.confirmation.common.updated.code"),
                getMessage("success.confirmation.branch.deleted.message")),
                HttpStatus.OK);
    }

    private List<BranchResponse> getBranchResponseList(List<Branch> branches) {
        return branches
                .stream()
                .map(this::getBranchResponse)
                .collect(Collectors.toList());
    }

    private BranchResponse getBranchResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .address(branch.getAddress())
                .tel(branch.getTel())
                .status(branch.getStatus().getDescription())
                .statusId(branch.getStatus().getId())
                .type(branch.getType().getDescription())
                .typeId(branch.getType().getId())
                .createdAt(getFormattedDateTime(branch.getCreatedAt()))
                .build();
    }

    private Branch getBranchSaveEntity(BranchSaveRequest request) {
        return Branch.builder()
                .name(request.getName())
                .address(request.getAddress())
                .tel(request.getTel())
                .type(BranchType.BRANCH)
                .status(BranchStatus.ACTIVE)
                .build();
    }

    private Branch getBranchUpdateEntity(BranchUpdateRequest request) {
        return Branch.builder()
                .id(request.getId())
                .name(request.getName())
                .address(request.getAddress())
                .tel(request.getTel())
                .status(BranchStatus.getById(request.getStatusId()))
                .build();
    }

    private void validateUpdateBranchRequest(BranchUpdateRequest request) {
        if ((request.getStatusId() != null) && (BranchStatus.getById(request.getStatusId()) == null))
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
