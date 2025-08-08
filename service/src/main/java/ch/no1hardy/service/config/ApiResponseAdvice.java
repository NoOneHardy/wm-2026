package ch.no1hardy.service.config;

import ch.no1hardy.service.exception.ApiError;
import ch.no1hardy.service.front.BaseResponse;
import ch.no1hardy.service.front.GlobalData;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(
            @NonNull MethodParameter returnType,
            @NonNull Class<? extends HttpMessageConverter<?>> converterType
    ) {

        if (returnType.getMethod() == null) return false;

        return !returnType.getMethod().isAnnotationPresent(RawResponse.class);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            @NonNull MethodParameter returnType,
            @NonNull MediaType selectedContentType,
            @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            @NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response
    ) {
        if (body instanceof ApiError || body instanceof BaseResponse<?>) return body;

        GlobalData globalData = (GlobalData) ((ServletServerHttpRequest) request).getServletRequest()
                .getAttribute(GlobalKey.GLOBAL_DATA.getKey());
        return BaseResponse.builder()
                .data(body)
                .globalData(globalData)
                .build();
    }
}
