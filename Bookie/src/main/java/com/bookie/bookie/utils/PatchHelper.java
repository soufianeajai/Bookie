package com.bookie.bookie.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;


@Component
public class PatchHelper {

    private final Validator validator;
    private final ObjectMapper objectMapper;

    public PatchHelper(Validator validator, ObjectMapper objectMapper) {
        this.validator = validator;
        this.objectMapper = objectMapper.copy()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T mergeAndValidate(T dtoToPatch, Map<String, Object> patchValues) {

        try {
            objectMapper.updateValue(dtoToPatch, patchValues);
        } catch (JsonMappingException e) {
            throw new HttpMessageNotReadableException("Invalid patch data: " + e.getMessage(), e);        }
        Set<ConstraintViolation<T>> violations = validator.validate(dtoToPatch);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return dtoToPatch;
    }
}