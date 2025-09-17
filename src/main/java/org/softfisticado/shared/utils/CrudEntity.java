package org.softfisticado.shared.utils;

import javax.sql.RowSet;
import java.util.List;
import java.util.Map;

public class CrudEntity {
    private String nameFieldIdJoinTable;
    private String nameJoinTable;
    private String nameFieldsJoinColumnName;
    private List<Map<Class<?>,String>> joinTableTypeName;
    private RowSet rowSet;

    public CrudEntity() {
    }

    public void setRowSet(RowSet rowSet) {
        this.rowSet = rowSet;
    }

    public void setNameFieldIdJoinTable(String nameFieldIdJoinTable) {
        this.nameFieldIdJoinTable = nameFieldIdJoinTable;
    }

    public void setNameJoinTable(String nameJoinTable) {
        this.nameJoinTable = nameJoinTable;
    }

    public void setNameFieldsJoinColumnName(String nameFieldsJoinColumnName) {
        this.nameFieldsJoinColumnName = nameFieldsJoinColumnName;
    }

    public void setJoinTableTypeName(List<Map<Class<?>, String>> joinTableTypeName) {
        this.joinTableTypeName = joinTableTypeName;
    }

    public String getNameFieldIdJoinTable() {
        return nameFieldIdJoinTable;
    }

    public String getNameJoinTable() {
        return nameJoinTable;
    }

    public String getNameFieldsJoinColumnName() {
        return nameFieldsJoinColumnName;
    }

    public List<Map<Class<?>, String>> getJoinTableTypeName() {
        return joinTableTypeName;
    }
}
