package com.sfpy.yuye.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.util.Collection;

public final class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
    }

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String jsonString, Class<T> valueType) {
        Assert.notNull(valueType, "valueType is null ");
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(InputStream is, Class<T> valueType) {
        Assert.notNull(valueType, "valueType is null ");
        Assert.notNull(is, "inputStream is null");
        try {
            return mapper.readValue(is, valueType);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T extends Collection<S>, S> T fromJson(String jsonString, Class<T> collectionType, Class<S> elementType) {
        Assert.notNull(collectionType, "collectionType is null");
        Assert.notNull(elementType, "elementType is null");
        if (StringUtils.isBlank(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(collectionType, elementType));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String jsonString, TypeReference<T> typeReference) {
        Assert.notNull(typeReference, "typeReference is null");
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return mapper.readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
