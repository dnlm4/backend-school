package org.softfisticado.shared.utils;

import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.enterprise.context.Dependent;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.lang.reflect.Field;
import java.util.*;


@Dependent
public class FieldProcess<E> {

    private List<Object> attributeValues = new ArrayList<>();
    private List<Map<Class<?>,Object>> listAttributeMapTypeName = new ArrayList<>(List.of());
    private StringBuilder attributesNames = new StringBuilder();
    private StringBuilder attributesPositions = new StringBuilder();
    private StringBuilder attributesNamePosition = new StringBuilder();
    private String attributeIdPos="";

    public void insert(Field[] fields, E entity) {
        attributesNames= new StringBuilder();
        attributesPositions = new StringBuilder();
        attributeValues =  new ArrayList<>(List.of());
        int posValue = 0;
        for (Field field : fields) {
            if(!field.isSynthetic() && field.getName().startsWith("$")) {
                continue;
            }
            if(field.isAnnotationPresent(Id.class)){
                continue;
            }
            if (posValue>0){
                attributesNames.append(",");
                attributesPositions.append(",");
            }
            posValue++;
            attributesNames.append(ReflectionUtils.getAttributeColumnNames(field));
            attributesPositions.append("$").append(posValue);
            this.attributeValues.add(ReflectionUtils.validationAttributeValue(field,entity));
        }
    }
    public void update(Field[] fields,E entity) {
        attributeValues = new ArrayList<>(List.of());
        attributesNamePosition = new StringBuilder();
        attributeIdPos="";
        Object id=null;
        int posValue = 0;
        for (Field field : fields) {
            if(!field.isSynthetic() && field.getName().startsWith("$")) {
                continue;
            }
            if(field.isAnnotationPresent(Id.class)){
                id =ReflectionUtils.validationAttributeValue(field,entity);
                continue;
            }
            if (posValue>0){
                attributesNamePosition.append(",");
            }
            posValue++;
            attributesNamePosition.append(ReflectionUtils.getAttributeColumnNames(field))
                    .append("=")
                    .append("$").append(posValue);
            this.attributeValues.add(ReflectionUtils.validationAttributeValue(field,entity));
        }
        this.attributeValues.add(id);
        attributeIdPos= String.valueOf((posValue+1));
    }
    public void select(Field[] fields) {
        listAttributeMapTypeName = new ArrayList<>(List.of());
        attributesNames = new StringBuilder();
        int posValue = 0;
        for (Field field : fields) {
            if(!field.isSynthetic() && field.getName().startsWith("$")) {
                continue;
            }
            Map<Class<?>,Object> mapTypeValue = new HashMap<>();
            String attributeName=ReflectionUtils.getAttributeColumnNames(field);
            mapTypeValue.put(field.getType(),attributeName);
            listAttributeMapTypeName.add(mapTypeValue);
            if (posValue>0){
                attributesNames.append(",");
            }
            attributesNames.append(attributeName);
            posValue++;
        }
    }
    public String getAttributesPositions(){
        return attributesPositions.toString();
    }
    public List<Map<Class<?>,Object>> getListAttributeMapTypeName(){
        return listAttributeMapTypeName;
    }
    public String getAttributesNames(){
        return attributesNames.toString();
    }
    public String getAttributesNamePosition(){
        return attributesNamePosition.toString();
    }
    public String getAttributeIdPos(){
        return attributeIdPos;
    }
    public Tuple getAttributeValues(){
        Tuple valuesTuple = Tuple.tuple();
        for (Object attributeValue : (List<Object>) attributeValues) {
            valuesTuple.addValue(attributeValue);
        }
        return valuesTuple;
    }
    public Tuple getAttributeValueDelete(Long id){
        Tuple valuesTuple = Tuple.tuple();
        return valuesTuple.addValue(id);
    }
    public String getTableName(Object clazz) {
        Table tableAnnotation = clazz.getClass().getAnnotation(Table.class);
        return tableAnnotation.name().toLowerCase();
    }
    public String getJoinTableName(Class<?> clazz) {
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        return tableAnnotation.name().toLowerCase();
    }
    public Field[] getFields(Object clazz) {
        return clazz.getClass().getDeclaredFields();
    }

}
