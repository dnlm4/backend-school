package org.softfisticado.shared.utils;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;

import java.lang.reflect.Field;

public class ReflectionUtils {

    private static Object extractValue(Object value) throws NoSuchFieldException, IllegalAccessException {
        Field idField = value.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        return idField.get(value);
    }
    public static String getAttributeColumnNames(Field field) {
        System.out.println("getAttributeColumnNames"+field.getClass().getName());
        if(field.isAnnotationPresent(JoinColumn.class)) {
            return field.getAnnotation(JoinColumn.class).name();
        }else if(field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).name();
        }else{
            return field.getName();
        }
    }
    public static Object validationAttributeValue(Field field,Object entity) {
        try {
            field.setAccessible(true);
            Object value = field.get(entity);
            if(field.isAnnotationPresent(JoinColumn.class)) {
                if (value == null) return null;
                return extractValue(value);
            }else{
                return value;
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
