package com.icbt.ap.mobileaccessoriessales.exception;

import com.icbt.ap.mobileaccessoriessales.dto.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class AppExceptionHandler {
    private final MessageSource messageSource;

    @ExceptionHandler(value = {CustomServiceException.class})
    public ResponseEntity<Object> handleInvalidInputException(CustomServiceException ex, Locale locale) {
        log.error("Business Exception: " + ex.getMessage(), ex);
        return getBusinessError(ex, ex.getCode(), locale);
    }

    @ExceptionHandler(value = {CustomAuthException.class})
    public ResponseEntity<Object> handleAuthException(CustomAuthException ex, Locale locale) {
        log.error("Business Exception: " + ex.getMessage(), ex);
        return getAuthError(ex, ex.getCode(), locale);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<CommonResponseDTO> handleInvalidInputException(Exception ex, Locale locale) {
        log.error("Server Exception: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponseDTO(false, HttpStatus.INTERNAL_SERVER_ERROR.name(),
                        "Something went wrong! "));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, Locale locale) {

        return getBadRequestError(ex, HttpStatus.BAD_REQUEST.name(), locale);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleArgumentTypeMismatchException(
            HttpMessageNotReadableException ex, Locale locale) {

        return getBadRequestError(ex, HttpStatus.BAD_REQUEST.name(), locale);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        log.error("Method arguments are not valid", ex);
        BindingResult result = ex.getBindingResult();

        List<String> errorList = new ArrayList<>();
        result.getFieldErrors().forEach(fieldError -> errorList.add(fieldError.getField() + " : " + fieldError.getDefaultMessage()
                + " : rejected value: " + fieldError.getRejectedValue() + ""));

        result.getGlobalErrors().forEach(fieldError -> errorList.add(fieldError.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseDTO(false, HttpStatus.BAD_REQUEST.name(),
                        errorList.isEmpty() ? "Invalid request data" : errorList.get(0)));
    }

    private ResponseEntity<Object> getBusinessError(CustomServiceException ex, String code, Locale locale) {
        log.error("Bad request Exception: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseDTO(false,
                        messageSource.getMessage(code, ex.getArgs(), locale),
                        messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale))
                );
    }

    private ResponseEntity<Object> getAuthError(CustomAuthException ex, String code, Locale locale) {
        log.error("Auth Exception: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponseDTO(false,
                        messageSource.getMessage(code, ex.getArgs(), locale),
                        messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale))
                );
    }

    private ResponseEntity<Object> getBadRequestError(Exception ex, String code, Locale locale) {
        log.error("Bad request Exception: " + ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponseDTO(false, code, ex.getMessage()));
    }
}
