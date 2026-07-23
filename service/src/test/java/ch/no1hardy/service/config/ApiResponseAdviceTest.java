package ch.no1hardy.service.config;

import ch.no1hardy.service.controller.TeamController;
import ch.no1hardy.service.front.GlobalData;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class ApiResponseAdviceTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("beforeBodyWrite(Object, MethodParameter, MediaType, Class, ServerHttpRequest, ServerHttpResponse) - should serialize wrapped string responses as JSON")
    void shouldSerializeWrappedStringResponsesAsJson() throws Exception {
        ApiResponseAdvice advice = new ApiResponseAdvice(objectMapper);
        Method method = TeamController.class.getMethod("uploadFlag", MultipartFile.class);
        MethodParameter returnType = new MethodParameter(method, -1);

        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setAttribute(GlobalKey.GLOBAL_DATA.getKey(), GlobalData.builder()
                .id("admin-1")
                .notifications(List.of())
                .build());

        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);
        Object body = advice.beforeBodyWrite(
                "/cdn/flags/flag.png",
                returnType,
                MediaType.TEXT_PLAIN,
                StringHttpMessageConverter.class,
                new ServletServerHttpRequest(servletRequest),
                response
        );

        String json = assertInstanceOf(String.class, body);
        JsonNode payload = objectMapper.readTree(json);

        assertEquals("/cdn/flags/flag.png", payload.get("data").asText());
        assertEquals("admin-1", payload.get("globalData").get("id").asText());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
    }
}
