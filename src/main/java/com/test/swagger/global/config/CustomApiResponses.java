package com.test.swagger.global.config;

import com.test.swagger.global.response.enums.ResponseCode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomApiResponses {
    ResponseCode[] value();
}
