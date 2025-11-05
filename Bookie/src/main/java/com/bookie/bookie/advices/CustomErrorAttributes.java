package com.bookie.bookie.advices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> defaultAttributes = super.getErrorAttributes(webRequest, options);
        log.warn("No handler for this route: {}", defaultAttributes.get("path"));
        Map<String, Object> response = new HashMap<>();
        response.put("data", null);
        response.put("error", Map.of(
                "code", defaultAttributes.get("status"),
                "message", defaultAttributes.get("error")
        ));
        response.put("success", false);
        response.put("timestamp", defaultAttributes.get("timestamp"));
        response.put("path", defaultAttributes.get("path"));
        return response;
    }
}
