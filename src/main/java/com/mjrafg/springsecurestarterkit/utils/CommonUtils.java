package com.mjrafg.springsecurestarterkit.utils;

import java.util.Collection;
import java.util.Map;

public class CommonUtils {

    /**
     * Checks if the provided object is empty. Supports String, Collection, Map, and Array types.
     * @param obj the object to check for emptiness
     * @return true if the object is empty or null, false otherwise
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).toString().trim().isEmpty();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(obj) == 0;
        }
        return false;
    }

    /**
     * Checks if the provided object is not empty.
     * @param obj the object to check
     * @return true if the object is not empty, false if it is empty or null
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
