package uk.nhs.digital.test.util;

import org.apache.commons.lang3.reflect.FieldUtils;

import static uk.nhs.digital.test.util.ExceptionTestUtils.wrapCheckedException;

public class ReflectionTestUtils {

    @SuppressWarnings("unchecked")
    public static <T> T readField(final Object targetObject, final String fieldName) {

        return (T) wrapCheckedException(()-> FieldUtils.readField(targetObject, fieldName, true));
    }
}
