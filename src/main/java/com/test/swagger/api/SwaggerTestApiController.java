package com.test.swagger.api;

import com.test.swagger.dto.TestRequestDto;
import com.test.swagger.global.config.CustomApiResponses;
import com.test.swagger.global.response.enums.ResponseCode;
import com.test.swagger.global.response.model.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Slf4j
@Tag(name = "Swagger API Controller", description = "Swagger Test API입니다.")
@RestController
@RequestMapping("/api/swagger-test")
public class SwaggerTestApiController {

    @Operation(summary = "get 조회 api",
            description = "get api에 대한 테스트입니다.",
            parameters = {@Parameter(name = "param", description = "PathVariable")}
    )
    @CustomApiResponses({ResponseCode.ERROR_ACCESS_DENIED, ResponseCode.ERROR_INTERNAL_SERVER_ERROR, ResponseCode.ERROR_VALIDATION})
    @GetMapping("/get/{param}")
    public ResponseEntity<CommonResponse> getApi(@PathVariable String param) {
        log.info("[GET] /api/swagger-test/get/{}", param);
        CommonResponse commonResponse = new CommonResponse(ResponseCode.SUCCESS, new ArrayList<>());
        return ResponseEntity.status(commonResponse.getStatusCode()).body(commonResponse);
    }

    @Operation(summary = "post insert api",
            description = "post api에 대한 테스트입니다.",
            parameters = {@Parameter(name = "testRequestDto", description = "testRequestDto 요청객체")}
    )
    @PostMapping("/post")
    public ResponseEntity<CommonResponse> postApi(@RequestBody TestRequestDto testRequestDto) {
        log.info("[POST] /api/swagger-test/post  {}", testRequestDto);
        CommonResponse commonResponse = new CommonResponse(ResponseCode.SUCCESS, "success");
        return ResponseEntity.status(commonResponse.getStatusCode()).body(commonResponse);

    }
}
