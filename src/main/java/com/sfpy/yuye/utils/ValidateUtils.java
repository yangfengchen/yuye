package com.sfpy.yuye.utils;


import org.apache.commons.lang3.StringUtils;

import javax.validation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ValidateUtils {

    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public static <T> void validate(T t) {
        validate(validator.validate(t));
    }

    private static <T> void validate(Set<ConstraintViolation<T>> constraintViolations) {
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            Optional<ConstraintViolation<T>> optional = constraintViolations.stream().findFirst();
            if (optional.isPresent()) {
                ConstraintViolation<T> constraintViolation = optional.get();
                throw new ValidationException(constraintViolation.getPropertyPath() + " error: " + constraintViolation.getMessage());
            }
        }
    }

    public static <T> void validate(T t, String... fields) {
        for (String field : fields) {
            validate(validator.validateProperty(t, field));
        }
    }

    public static <T> void validate(Class<T> beanType, String field, Object value) {
        validate(validator.validateValue(beanType, field, value));
    }

    public static <T> void validate(boolean flag, String message) {
        if (!flag) {
            throw new ValidationException(message);
        }
    }

    public static <T> void validate(T t, Predicate<T> predicate, String message) {
        if (!predicate.test(t)) {
            throw new ValidationException(message);
        }
    }

    public static Predicate<Object> NOT_NULL = v -> v != null;
    public static Predicate<Optional> OPTIONAL_NOT_NULL = Optional::isPresent;
    public static Predicate<Object> NULL = v -> v == null;
    public static Predicate<String> NOT_BLANK = v -> StringUtils.isNotBlank(v);
    public static Predicate<String> PHONE = v -> v.matches("1[\\d]{10}");
    public static Predicate<Object> NOT_EMPTY = v -> {
        if (v == null) {
            return false;
        } else {
            Class<?> clazz = v.getClass();
            if (clazz.isArray()) {
                return ((Object[]) v).length != 0;
            } else if (Iterable.class.isAssignableFrom(clazz)) {
                return ((Iterable<?>) v).iterator().hasNext();
            } else if (Map.class.isAssignableFrom(clazz)) {
                return !((Map<?, ?>) v).isEmpty();
            }
        }
        return true;
    };

    public static Predicate<Object> IS_EMPTY = v -> {
        if (v != null) {
            return false;
        } else {
            Class<?> clazz = v.getClass();
            if (clazz.isArray()) {
                return ((Object[]) v).length == 0;
            } else if (Iterable.class.isAssignableFrom(clazz)) {
                return !((Iterable<?>) v).iterator().hasNext();
            } else if (Map.class.isAssignableFrom(clazz)) {
                return !((Map<?, ?>) v).isEmpty();
            }
        }
        return true;
    };

    public static Function<String, Predicate<String>> PATTERN = t -> r -> r.matches(t);
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Function<Number, Predicate<Number>> LESS_THAN = t -> r -> ((Comparable) r).compareTo((Comparable) t) < 0;
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Function<Number, Predicate<Number>> GREATER_THAN = t -> r -> ((Comparable) r).compareTo((Comparable) t) > 0;

    public static Predicate<Boolean> TRUE = v -> v.equals(Boolean.TRUE);
    public static Predicate<Boolean> FALSE = v -> v.equals(Boolean.FALSE);

}
