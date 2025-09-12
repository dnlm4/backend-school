package org.softfisticado.infrastructure.persistence.mapper;

import io.vertx.mutiny.sqlclient.Row;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class RowValueExtractor {
    private static final Map<Class<?>, BiFunction<Row, String, Object>> extractors = new HashMap<>();
    static {
        extractors.put(String.class, Row::getString);
        extractors.put(Long.class, Row::getLong);
        extractors.put(long.class, Row::getLong);
        extractors.put(Integer.class, Row::getInteger);
        extractors.put(int.class, Row::getInteger);
        extractors.put(Double.class, Row::getDouble);
        extractors.put(double.class, Row::getDouble);
        extractors.put(Boolean.class, Row::getBoolean);
        extractors.put(boolean.class, Row::getBoolean);
        extractors.put(Float.class, Row::getFloat);
        extractors.put(float.class, Row::getFloat);
        extractors.put(Short.class, Row::getShort);
        extractors.put(short.class, Row::getShort);
        extractors.put(java.time.LocalDate.class, Row::getLocalDate);
        extractors.put(java.time.LocalDateTime.class, Row::getLocalDateTime);
        extractors.put(java.util.UUID.class, Row::getUUID);
    }
    public static Object getValue(Row row, String fieldName, Class<?> type) {
        BiFunction<Row, String, Object> extractor = extractors.get(type);
        if (extractor == null) {
            throw new IllegalArgumentException("Unsupported type: " + type.getName());
        }
        return extractor.apply(row, fieldName);
    }
}
