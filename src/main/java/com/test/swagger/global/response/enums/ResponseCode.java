package com.test.swagger.global.response.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ResponseCode {
    SUCCESS(HttpStatus.OK, "[SUCCESS] logic."),



    ERROR_VALIDATION(HttpStatus.BAD_REQUEST,  "[FAIL] validation error."),
    ERROR_BAD_CLIENT_REQUEST(HttpStatus.BAD_REQUEST, "[FAIL] bad request."),
    ERROR_UN_AUTHENTICATE(HttpStatus.UNAUTHORIZED, "[FAIL] un authenticate."),
    ERROR_ACCESS_DENIED(HttpStatus.FORBIDDEN,  "[FAIL] access denied."),
    ERROR_NOT_FOUND(HttpStatus.NOT_FOUND, "[FAIL] not found resource."),
    ERROR_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "[FAIL] Internal Server Error."),
    ;

    private HttpStatus httpStatus;
    private String message;

    ResponseCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
