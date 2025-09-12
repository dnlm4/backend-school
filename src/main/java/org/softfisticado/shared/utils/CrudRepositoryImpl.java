package org.softfisticado.shared.utils;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Inject;
import jakarta.persistence.Table;
import jakarta.ws.rs.core.Response;
import org.softfisticado.infrastructure.persistence.mapper.RowValueExtractor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class CrudRepositoryImpl<E> implements CrudRepository<E> {

    @Inject
    PgPool pgPool;
    @Inject
    FieldProcess<E> fieldProcess;

    @Override
    public Uni save(E entity) {
        String table = fieldProcess.getTableName(entity);
        fieldProcess.insert(fieldProcess.getFields(entity),entity);
        String sqlQuery = "INSERT INTO school." + table + "("+fieldProcess.getAttributesNames()+") VALUES ("+fieldProcess.getAttributesPositions()+") RETURNING id";

        return pgPool.preparedQuery(sqlQuery)
                .execute(fieldProcess.getAttributeValues())
                .onItem().transform(pgRow->{
                    Row row = pgRow.iterator().next();
                    return row.getLong("id");
                });
    }

    @Override
    public Uni update(E entity) {
        String table = fieldProcess.getTableName(entity);
        fieldProcess.update(fieldProcess.getFields(entity),entity);
        System.out.println("fieldProcess.getAttributeIdPos()  "+fieldProcess.getAttributeIdPos());
        String sqlQuery = "UPDATE school." + table + " SET " + fieldProcess.getAttributesNamePosition() + " WHERE id = $" + fieldProcess.getAttributeIdPos();
        System.out.println(sqlQuery);
        return pgPool.preparedQuery(sqlQuery)
                .execute(fieldProcess.getAttributeValues())
                .onItem().transform(pgRowSet -> {
                    if (pgRowSet.rowCount() == 1) {
                        return entity;
                    } else {
                        return Response.status(Response.Status.NOT_FOUND).entity("update not applied").build();
                    }
                });
    }

    @Override
    public Uni delete(Long id,E entity) {
        String table = fieldProcess.getTableName(entity);
        String sqlQuery = "DELETE FROM school."+ table +" WHERE id = $1";
        return pgPool
                .preparedQuery(sqlQuery)
                .execute(fieldProcess.getAttributeValueDelete(id))  // cityId es un Long o Integer
                .onItem().transform(pgRowSet -> {
                    if (pgRowSet.rowCount() == 1) {
                        return Response.status(Response.Status.NOT_FOUND).entity(id).build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND).entity("deleted not applied").build();
                    }
                });
    }

    @Override
    public Uni findById(Long id,E entity) {
        String table = fieldProcess.getTableName(entity);
        Field[] fields = fieldProcess.getFields(entity);
        fieldProcess.select(fields);
        String sqlQuery = "SELECT " + fieldProcess.getAttributesNames()+ " FROM school."+ table +" WHERE id = $1";
        System.out.println("sqlQuery"+sqlQuery);
        return pgPool
                .preparedQuery(sqlQuery)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet->{
                    Row row = pgRowSet.iterator().hasNext() ? pgRowSet.iterator().next() : null;

                    if(row==null)return null;
                    Map<String,Object> map = new HashMap<>();
                    for(Map<Class<?>,Object> field: fieldProcess.getListAttributeMapTypeName()){
                        Class<?> key=null;
                        String nameField="";
                        for (Map.Entry<Class<?>, Object> entry : field.entrySet()) {
                            key = entry.getKey();
                            if(!key.getName().startsWith("java")){
                                try {
                                    Class<?> clazz = Class.forName(key.getName());
                                    System.out.println("------------------------ ");
                                    System.out.println("xxxxx   "+ Arrays.toString(clazz.getAnnotations()));
                                    System.out.println("wwwwwww   "+ clazz.getAnnotation(Table.class));
                                    System.out.println("ggggggggg   "+ clazz.getAnnotation(Table.class).name());
                                    System.out.println("------------------------ ");
                                    FieldProcess<E> joinFieldProcess = new FieldProcess<E>();
                                    String joinTable = joinFieldProcess.getJoinTableName(clazz);
                                    System.out.println("joinTable "+joinTable);
                                    Field[] joinFields = joinFieldProcess.getFields(clazz);
                                    joinFieldProcess.select(joinFields);
                                    String sqlQueryJoin = "SELECT " + joinFieldProcess.getAttributesNames()+ " FROM school."+ joinTable +" WHERE id = $1";
                                    System.out.println("sqlQueryJoin"+sqlQueryJoin);
                                    for (Field joinField : joinFields) {
                                        System.out.println("Campo: " + joinField.getName() + " - Tipo: " + joinField.getType().getSimpleName());
                                    }
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            nameField = entry.getValue().toString();

                        }
                        map.put(nameField,RowValueExtractor.getValue(row,nameField,key));
                    }
                    System.out.println("RowValueExtractor    "+map);

                    return map;
                });
    }



}
