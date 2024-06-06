package com.test.swagger.global.exception;

import com.test.swagger.global.response.enums.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ResponseCode responseCode;

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}
