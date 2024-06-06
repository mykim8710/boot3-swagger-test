package com.test.swagger.global.response.model;

import com.test.swagger.global.response.enums.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommonResponse<T> {
    @Schema(description = "Http Status", nullable = false, example = "[200]")
    private HttpStatus httpStatus;

    @Schema(description = "상태코드", nullable = false, example = "200")
    private int statusCode;

    @Schema(description = "상태 메세지", nullable = false, example = "success")
    private String message;

    @Schema(description = "데이터")
    private T data;

    public CommonResponse(ResponseCode responseCode) {
        this.httpStatus = responseCode.getHttpStatus();
        this.statusCode = responseCode.getHttpStatus().value();
        this.message = responseCode.getMessage();
    }

    public CommonResponse(ResponseCode responseCode, T data) {
        this.httpStatus = responseCode.getHttpStatus();
        this.statusCode = responseCode.getHttpStatus().value();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public void updateData(T data) {
        this.data = data;
    }
}
