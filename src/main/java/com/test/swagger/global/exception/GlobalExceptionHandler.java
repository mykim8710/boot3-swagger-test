package com.test.swagger.global.exception;

import com.test.swagger.global.response.enums.ResponseCode;
import com.test.swagger.global.response.model.CommonResponse;
import com.test.swagger.global.response.model.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.error("exception = {}" , e);

        List<ValidationError> validationErrors = new ArrayList<>();

        if(e.hasErrors()) {
            List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                String fieldName = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();
                log.error("field name = {}", fieldName);
                log.error("error message = {}", errorMessage);

                validationErrors.add(ValidationError.builder()
                        .fieldName(fieldName)
                        .errorMessage(errorMessage)
                        .build());
            }
        }

        CommonResponse errorResponse = new CommonResponse(ResponseCode.ERROR_VALIDATION, validationErrors);

        return ResponseEntity
                .status(errorResponse.getStatusCode())
                .body(errorResponse);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonResponse> businessExceptionHandler(BusinessException e) {
        log.error("exception = {}", e);
        log.info("Class = {}", e.getClass());
        log.info("ResponseCode = {}", e.getResponseCode());

        CommonResponse commonResponse = new CommonResponse(e.getResponseCode());

        return ResponseEntity
                .status(commonResponse.getStatusCode())
                .body(commonResponse);
    }

}
