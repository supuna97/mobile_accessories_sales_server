package com.icbt.ap.mobileaccessoriessales.controller.v1.rest;

import com.icbt.ap.mobileaccessoriessales.controller.CommonController;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.request.UserLoginRequest;
import com.icbt.ap.mobileaccessoriessales.controller.v1.model.response.UserLoginResponse;
import com.icbt.ap.mobileaccessoriessales.entity.User;
import com.icbt.ap.mobileaccessoriessales.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Locale;

import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.DATE_TIME_FORMATTER;
import static com.icbt.ap.mobileaccessoriessales.controller.v1.util.ApiConstant.VERSION;

@RestController
@RequestMapping(value = VERSION + "/user")
@Slf4j
@RequiredArgsConstructor
public class UserController implements CommonController {

    private final UserService userService;

    private final MessageSource messageSource;

    @PostMapping(path = "/auth")
    public ResponseEntity<UserLoginResponse> authenticate(@Valid @RequestBody UserLoginRequest request) {
        log.info("User login request, Username: {}", request.getUsername());
        return authenticateUser(request);
    }

    private ResponseEntity<UserLoginResponse> authenticateUser(UserLoginRequest request) {
        final User user = userService.authenticate(request);
        return ResponseEntity.ok(UserLoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userRole(user.getUserRole())
                .branchId(user.getBranchId())
                .build()
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
