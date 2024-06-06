package com.test.swagger.global.config;

import com.test.swagger.global.response.enums.ResponseCode;
import com.test.swagger.global.response.model.CommonResponse;
import com.test.swagger.global.response.model.ValidationError;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.FieldError;
import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@OpenAPIDefinition(info = @Info(title = "Swagger Test API 명세서", description = "Swagger Test API 명세서", version = "v1"))
@Configuration
public class SwaggerConfig {
    // jwt
    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                                .in(SecurityScheme.In.HEADER).name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                    .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                    .security(Arrays.asList(securityRequirement));
    }

    @Bean
    public OperationCustomizer customize() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            CustomApiResponses customApiResponses = handlerMethod.getMethodAnnotation(CustomApiResponses.class);

            // @CustomApiResponses 어노테이션이 붙어있다면
            if (customApiResponses != null) {
                generateErrorCodeResponseExample(operation, customApiResponses.value());
            } else {
                CustomApiResponse customApiResponse = handlerMethod.getMethodAnnotation(CustomApiResponse.class);

                // @CustomApiResponses 어노테이션이 붙어있지 않고 @CustomApiResponse 어노테이션이 붙어있다면
                if (customApiResponse != null) {
                    generateErrorCodeResponseExample(operation, customApiResponse.value());
                }
            }

            return operation;
        };
    }

    // 여러 개의 에러 응답값 추가
    private void generateErrorCodeResponseExample(Operation operation, ResponseCode[] responseCodes) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder(에러 응답값) 객체를 만들고 에러 코드별로 그룹화
        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(responseCodes)
                                                                                        .map(responseCode -> ExampleHolder.builder()
                                                                                                                            .holder(getSwaggerExample(responseCode))
                                                                                                                            .code(responseCode.getHttpStatus().value())
                                                                                                                            .name(responseCode.name())
                                                                                                                            .build()
                                                                                        )
                                                                                        .collect(Collectors.groupingBy(ExampleHolder::getCode));

        // ExampleHolders를 ApiResponses에 추가
        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    // 단일 에러 응답값 예시 추가
    private void generateErrorCodeResponseExample(Operation operation, ResponseCode responseCode) {
        ApiResponses responses = operation.getResponses();

        // ExampleHolder 객체 생성 및 ApiResponses에 추가
        ExampleHolder exampleHolder = ExampleHolder.builder()
                                                    .holder(getSwaggerExample(responseCode))
                                                    .name(responseCode.name())
                                                    .code(responseCode.getHttpStatus().value())
                                                    .build();

        addExamplesToResponses(responses, exampleHolder);
    }

    // ErrorResponseDto 형태의 예시 객체 생성
    private Example getSwaggerExample(ResponseCode responseCode) {
        CommonResponse commonResponse = new CommonResponse(responseCode);

        if (responseCode.equals(ResponseCode.ERROR_VALIDATION)) {
            List<ValidationError> validationErrors = new ArrayList<>();
            IntStream.range(1, 3)
                    .forEach(i -> {
                        FieldError fieldError = new FieldError("objectName_"+i, "field_"+i, "errorMessage_"+i);
                        ValidationError validationError = ValidationError.builder()
                                                                            .fieldName(fieldError.getField())
                                                                            .errorMessage(fieldError.getDefaultMessage())
                                                                            .build();
                        validationErrors.add(validationError);
                    });

            commonResponse.updateData(validationErrors);
        }

        Example example = new Example();
        example.setValue(commonResponse);
        return example;
    }

    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach(
                (status, v) -> {
                    Content content = new Content();
                    MediaType mediaType = new MediaType();
                    ApiResponse apiResponse = new ApiResponse();

                    v.forEach(
                            exampleHolder -> mediaType.addExamples(
                                    exampleHolder.getName(),
                                    exampleHolder.getHolder()
                            )
                    );
                    content.addMediaType("application/json", mediaType);
                    apiResponse.setContent(content);
                    responses.addApiResponse(String.valueOf(status), apiResponse);
                }
        );
    }

    private void addExamplesToResponses(ApiResponses responses, ExampleHolder exampleHolder) {
        Content content = new Content();
        MediaType mediaType = new MediaType();
        ApiResponse apiResponse = new ApiResponse();

        mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder());
        content.addMediaType("application/json", mediaType);
        apiResponse.content(content);
        responses.addApiResponse(String.valueOf(exampleHolder.getCode()), apiResponse);
    }

}
