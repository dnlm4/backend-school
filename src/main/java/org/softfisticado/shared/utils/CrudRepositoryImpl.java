package org.softfisticado.shared.utils;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.softfisticado.infrastructure.persistence.mapper.RowValueExtractor;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


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
        String tableName = fieldProcess.getTableName(entity);
        Field[] fieldsTable = fieldProcess.getFields(entity);
        fieldProcess.select(fieldsTable);
        String sqlQueryTable = "SELECT "+fieldProcess.getAttributesNames()+" FROM school."+ tableName +" WHERE id = $1";
        return pgPool
                .preparedQuery(sqlQueryTable)
                .execute(Tuple.of(id))
                .onItem().transformToUni(pgRowSet->{
                    Row rowTable = pgRowSet.iterator().hasNext() ? pgRowSet.iterator().next() : null;
                    if(rowTable==null)return null;
                    Map<String,Object> returnMap = new HashMap<>();
                    String joinTableName="";
                    Object joinTableIdValue = null;
                    String joinColumnName="";
                    List<Map<Class<?>,Object>> listJointTableAttributeMapTypeName = new ArrayList<>(List.of());
                    tableFor:
                    for(Map<Class<?>,Object> fieldTable:fieldProcess.getListAttributeMapTypeName()){
                        Class<?> keyTable = null;
                        String nameFieldTable = "";
                        Object valueFieldTable = null;
                        for(Map.Entry<Class<?>,Object> entryTable:fieldTable.entrySet()){
                            keyTable = entryTable.getKey();
                            nameFieldTable = entryTable.getValue().toString();
                            if(!keyTable.getName().startsWith("java")){
                                try {
                                    Class<?> classEntityJoin = Class.forName(keyTable.getName());
                                    FieldProcess<E> fieldProcessJoin = new FieldProcess<>();
                                    joinTableName = fieldProcessJoin.getJoinTableName(classEntityJoin);
                                    Field[] fieldsJoinTable = fieldProcessJoin.getJoinFields(classEntityJoin);
                                    fieldProcessJoin.selectJoinTable(fieldsJoinTable);
                                    joinColumnName = fieldProcessJoin.getJointTableAttributesNames();
                                    listJointTableAttributeMapTypeName = fieldProcessJoin.getListJointTableAttributeMapTypeName();
                                    forJoinTable:
                                    for (Map<Class<?>,Object> joinField : listJointTableAttributeMapTypeName) {
                                        for (Map.Entry<Class<?>, Object> entryJoin : joinField.entrySet()) {
                                            if(Objects.equals(entryJoin.getValue().toString(), "id")){
                                                joinTableIdValue = RowValueExtractor.getValue(rowTable,nameFieldTable,entryJoin.getKey());
                                                break forJoinTable;
                                            }
                                        }
                                    }
                                    continue tableFor;
                                }catch (ClassNotFoundException e){
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        valueFieldTable=RowValueExtractor.getValue(rowTable,nameFieldTable,keyTable);
                        returnMap.put(nameFieldTable,valueFieldTable);
                    }
                    String sqlQueryJoin = "SELECT " + joinColumnName + " FROM school."+ joinTableName +" WHERE id = $1";
                    Map<String,Object> mapJoin = new HashMap<>();
                    List<Map<Class<?>, Object>> finalListJointTableAttributeMapTypeName = listJointTableAttributeMapTypeName;
                    String finalJoinTableName = joinTableName;
                    return pgPool.preparedQuery(sqlQueryJoin)
                            .execute(Tuple.of(joinTableIdValue))
                            .onItem()
                            .transform(joinTableSet->{
                                Row rowTableJoin = joinTableSet.iterator().hasNext() ? joinTableSet.iterator().next() : null;
                                if(rowTableJoin==null)return null;
                                String nameFieldTableJoin ="";
                                Object valueFieldTableJoin = null;
                                for (Map<Class<?>, Object> joinField : finalListJointTableAttributeMapTypeName) {
                                    nameFieldTableJoin = "";
                                    Class<?> joinKey = null;
                                    for (Map.Entry<Class<?>, Object> entryJoin : joinField.entrySet()) {
                                        joinKey = entryJoin.getKey();
                                        nameFieldTableJoin = entryJoin.getValue().toString();

                                    }
                                    valueFieldTableJoin = RowValueExtractor.getValue(rowTableJoin, nameFieldTableJoin, joinKey);
                                    mapJoin.put(nameFieldTableJoin, valueFieldTableJoin);
                                }
                                returnMap.put(finalJoinTableName,mapJoin);
                                return returnMap;
                            });



                });
    }



    private Uni<Map<String,RowSet>> selectFromTable(List<CrudEntity> listTables){
        Map<String,RowSet> results = new HashMap<>();
        Uni<Map<String,RowSet>> uni = Uni.createFrom().item(results);
        for(CrudEntity entity:listTables){
            String sqlQueryTable ="SELECT "+entity.getNameFieldsJoinColumnName()+" FROM school."+ entity.getNameJoinTable();
            System.out.println(sqlQueryTable);
            uni = uni.onItem()
                    .transformToUni(map->
                        pgPool.preparedQuery(sqlQueryTable)
                                .execute()
                                .onItem().invoke(rowSet-> {
                                    map.put(entity.getNameJoinTable(), rowSet);
                                })
                                .replaceWith(map));
        }
        return uni;
    }

    private Multi<Row>  selectTable(String fieldsTable,String tableName){
        String sqlQueryTable ="SELECT "+fieldsTable+" FROM school."+ tableName;
        return  pgPool.preparedQuery(sqlQueryTable)
                .execute()
                .onItem().transformToMulti(rows -> Multi.createFrom().iterable(rows)
                        .onOverflow().buffer(100));

    }

    @Override
    public Multi findAll(E entity){
        String tableName = fieldProcess.getTableName(entity);
        Field[] fieldsTable = entity.getClass().getDeclaredFields();
        String tableFieldNames = fieldProcess.getAttributesNames();
        fieldProcess.select(fieldsTable);
        String sqlQueryTable ="SELECT "+tableFieldNames+" FROM school."+ tableName;
        Multi<Row> smtp = selectTable(tableFieldNames,tableName);
        List<CrudEntity> listCrudEntity=fieldProcess.getListCrudEntity();
        Map<String,RowSet> mapCrudEntity = new HashMap<>();
        for(CrudEntity crudEntity:listCrudEntity){
            Multi<Row> smtpJoin =selectTable(crudEntity.getNameFieldsJoinColumnName(), crudEntity.getNameJoinTable());
            mapCrudEntity.put(crudEntity.getNameJoinTable(),smtpJoin);
        }


    }

    /*public Multi findAll(E entity){
        String tableName = fieldProcess.getTableName(entity);
        Field[] fieldsTable = fieldProcess.getFields(entity);
        fieldProcess.select(fieldsTable);
        String sqlQueryTable = "SELECT "+fieldProcess.getAttributesNames()+" FROM school."+ tableName;
        Multi<Row> smtp = pgPool.preparedQuery(sqlQueryTable).execute()
                .onItem()
                .transformToMulti(rows->Multi.createFrom().iterable(rows))
                .invoke(rows->{
                    System.out.println("Execute smtp");
                });
        List<CrudEntity> listCrudEntity=fieldProcess.getListCrudEntity();
        Uni<Map<String,RowSet>> dataJoinTable=this.selectFromTable(listCrudEntity);

        return smtp.onItem()
                .transformToUniAndConcatenate(tableRow->{
                    Class<?> keyTable = null;
                    Map<String,Object> mapReturn = new HashMap<>();
                    String nameFieldTable = "";
                    for(Map<Class<?>,Object> fieldTable:fieldProcess.getListAttributeMapTypeName()){
                        for(Map.Entry<Class<?>,Object> entryTable:fieldTable.entrySet()){
                            keyTable = entryTable.getKey();
                            if(!keyTable.getName().startsWith("java")){
                                continue;
                            }
                            nameFieldTable= entryTable.getValue().toString();
                            mapReturn.put(nameFieldTable,RowValueExtractor.getValue(tableRow,nameFieldTable,keyTable));
                        }
                    }
                    return dataJoinTable.onItem()
                            .transform(jointTableRows -> {
                                RowSet<Row> rowSet = null;
                                String joinTableName = "";
                                for (Map.Entry<String, RowSet> entryMapJoinTable : jointTableRows.entrySet()) {
                                    rowSet = entryMapJoinTable.getValue();
                                    joinTableName = entryMapJoinTable.getKey();
                                }
                                Map<String, Object> mapJoin = new HashMap<>();
                                Long idJoinTable = 0L;
                                assert rowSet != null;
                                rowFor:
                                for (Row row : rowSet) {
                                    Long idTable = row.getLong("id");
                                    for (CrudEntity crudEntity : listCrudEntity) {
                                        if (crudEntity.getNameJoinTable().equals(joinTableName)) {
                                            idJoinTable = (Long) RowValueExtractor.getValue(tableRow, crudEntity.getNameFieldIdJoinTable(), Long.class);
                                            if (Objects.equals(idTable, idJoinTable)) {
                                                for (Map<Class<?>, String> mapTypeName : crudEntity.getJoinTableTypeName()) {
                                                    for (Map.Entry<Class<?>, String> entryMapJoin : mapTypeName.entrySet()) {
                                                        mapJoin.put(entryMapJoin.getValue(), RowValueExtractor.getValue(row, entryMapJoin.getValue(), entryMapJoin.getKey()));
                                                    }
                                                }
                                                break rowFor;
                                            }
                                        }
                                    }
                                }
                                mapReturn.put(joinTableName, mapJoin);

                                System.out.println("vvvvvvv");

                                return mapReturn;
                            });
                });

    }*/
    /*public Multi findAll(E entity){
        String tableName = fieldProcess.getTableName(entity);
        Field[] fieldsTable = fieldProcess.getFields(entity);
        fieldProcess.select(fieldsTable);
        String sqlQueryTable = "SELECT "+fieldProcess.getAttributesNames()+" FROM school."+ tableName;
        Multi<Row> smtp = pgPool.preparedQuery(sqlQueryTable).execute()
                .onItem()
                .transformToMulti(rows->Multi.createFrom().iterable(rows));
        return smtp.onItem()
                .transformToUniAndConcatenate(tableRow->{
                    List<CrudEntity> listCrudEntity=fieldProcess.getListCrudEntity();
                    Long idTableJoin = 0L;
                    String tableJoinName = "";
                    String joinTableFieldsName = "";
                    Class<?> keyTable = null;
                    Map<String,Object> mapReturn = new HashMap<>();
                    String nameFieldTable = "";
                    for(Map<Class<?>,Object> fieldTable:fieldProcess.getListAttributeMapTypeName()){
                        for(Map.Entry<Class<?>,Object> entryTable:fieldTable.entrySet()){
                            keyTable = entryTable.getKey();
                            if(!keyTable.getName().startsWith("java")){
                                continue;
                            }
                            nameFieldTable= entryTable.getValue().toString();
                            mapReturn.put(nameFieldTable,RowValueExtractor.getValue(tableRow,nameFieldTable,keyTable));
                        }
                    }
                    if(listCrudEntity!=null){
                        for(CrudEntity mapJoinTable:listCrudEntity){
                            tableJoinName = mapJoinTable.getNameJoinTable();
                            joinTableFieldsName = mapJoinTable.getNameFieldsJoinColumnName();
                            idTableJoin = (Long) RowValueExtractor.getValue(tableRow,mapJoinTable.getNameFieldIdJoinTable(),long.class);
                        }
                        String sqlQueryTableJoin= "SELECT " + joinTableFieldsName + " FROM school."+ tableJoinName +" WHERE id = $1";
                        Uni<RowSet<Row>> joinTableUni = pgPool.preparedQuery(sqlQueryTableJoin)
                                .execute(Tuple.of(idTableJoin));
                        String finalTableJoinName = tableJoinName;
                        return  joinTableUni.onItem()
                                .transform(joinTableRows->{
                                    Map<String,Object> mapJoinTable = new HashMap<>();
                                    for(CrudEntity crudEntity:listCrudEntity){
                                        for(Map<Class<?>, String> fieldJoinTable:crudEntity.getJoinTableTypeName()){
                                            for(Map.Entry<Class<?>, String> entryJoinTable:fieldJoinTable.entrySet()){
                                                for (Row row : joinTableRows) {
                                                    mapJoinTable.put(entryJoinTable.getValue(),RowValueExtractor.getValue(row,entryJoinTable.getValue(),entryJoinTable.getKey()));
                                                }
                                            }
                                        }
                                    }
                                    mapReturn.put(finalTableJoinName,mapJoinTable);
                                    return mapReturn;
                                });
                    }
                    return Uni.createFrom().item(tableRow);
                });
    }*/
}
