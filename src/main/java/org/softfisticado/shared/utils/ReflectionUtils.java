package org.softfisticado.shared.utils;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;

import java.lang.reflect.Field;
import java.util.*;

public class ReflectionUtils {

    private static Object extractValue(Object value) throws NoSuchFieldException, IllegalAccessException {
        Field idField = value.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        return idField.get(value);
    }
    public static String getAttributeColumnNames(Field field) {
        if(field.isAnnotationPresent(JoinColumn.class)) {
            return field.getAnnotation(JoinColumn.class).name();
        }else if(field.isAnnotationPresent(Column.class)) {
            return field.getAnnotation(Column.class).name();
        }else{
            return field.getName();
        }
    }
    public static CrudEntity getJoinTableConfig(Field field) {
        StringBuilder listFields = new StringBuilder();
        CrudEntity  crudEntity = new CrudEntity();
        Map<Class<?>,String> mapTypeNameField=new HashMap<>();
        List<Map<Class<?>,String>> listMapTypeNameField=new ArrayList<>();
        if(field.isAnnotationPresent(JoinColumn.class)) {
            try{
                Class<?> classEntityJoin = Class.forName(field.getType().getName());
                Field[] fields=classEntityJoin.getDeclaredFields();
                for (Field f:fields){
                    if(!f.isSynthetic() && f.getName().startsWith("$")) {
                        continue;
                    }
                    mapTypeNameField.put(f.getType(),f.getName());
                    listFields.append(f.getName()).append(",");
                }
                listFields.deleteCharAt(listFields.length()-1);
                listMapTypeNameField.add(mapTypeNameField);
            }catch (ClassNotFoundException e){
                throw new RuntimeException(e);
            }
                crudEntity.setNameFieldIdJoinTable(field.getAnnotation(JoinColumn.class).name());
                crudEntity.setNameJoinTable(field.getName());
                crudEntity.setNameFieldsJoinColumnName(listFields.toString());
                crudEntity.setJoinTableTypeName(listMapTypeNameField);
            return crudEntity;
        }
        return null;
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
