package ngo.nabarun.test.ngo_nabarun_test.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class PostgresDatabaseClient implements IDatabaseClient {
    private static final Logger logger = LogManager.getLogger(PostgresDatabaseClient.class);
    private final HikariDataSource dataSource;
    private final ObjectMapper objectMapper;

    public PostgresDatabaseClient() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(Configs.DB_URL);
        config.setUsername(Configs.DB_USERNAME);
        config.setPassword(Configs.DB_PASSWORD);
        config.setDriverClassName("org.postgresql.Driver");
        this.dataSource = new HikariDataSource(config);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String getTableName(Class<?> clazz) {
        DbEntity entity = clazz.getAnnotation(DbEntity.class);
        String name = entity != null ? entity.value() : clazz.getSimpleName().toLowerCase();
        return "\"" + name + "\"";
    }

    private void setParameters(PreparedStatement stmt, List<Object> params) throws SQLException {
        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            if (param instanceof Date) {
                stmt.setTimestamp(i + 1, new Timestamp(((Date) param).getTime()));
            } else {
                stmt.setObject(i + 1, param);
            }
        }
    }

    @Override
    public <T> T findFirst(Class<T> clazz, List<DBFilter> filters) {
        List<T> results = findMany(clazz, filters);
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public <T> List<T> findMany(Class<T> clazz, List<DBFilter> filters) {
        String tableName = getTableName(clazz);
        StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableName);
        List<Object> params = new ArrayList<>();

        if (filters != null && !filters.isEmpty()) {
            query.append(" WHERE ");
            String whereClause = filters.stream()
                    .map(filter -> {
                        params.add(filter.getValue());
                        return "\"" + filter.getField() + "\" " + filter.getOperator().getSqlOperator() + " ?";
                    })
                    .collect(Collectors.joining(" AND "));
            query.append(whereClause);
        }

        List<T> results = new ArrayList<>();
        logger.info("Executing query: " + query);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = metaData.getColumnName(i);
                        row.put(columnName, rs.getObject(i));
                    }
                    results.add(objectMapper.convertValue(row, clazz));
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing postgres query: " + query, e);
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public <T> boolean delete(Class<T> clazz, List<DBFilter> filters) {
        String tableName = getTableName(clazz);
        StringBuilder query = new StringBuilder("DELETE FROM ").append(tableName);
        List<Object> params = new ArrayList<>();

        if (filters != null && !filters.isEmpty()) {
            query.append(" WHERE ");
            String whereClause = filters.stream()
                    .map(filter -> {
                        params.add(filter.getValue());
                        return "\"" + filter.getField() + "\" " + filter.getOperator().getSqlOperator() + " ?";
                    })
                    .collect(Collectors.joining(" AND "));
            query.append(whereClause);
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            setParameters(stmt, params);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error executing postgres delete: " + query, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql, List<Object> params) {
        List<Map<String, Object>> results = new ArrayList<>();
        logger.info("Executing raw query: " + sql);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (params != null) {
                setParameters(stmt, params);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    results.add(row);
                }
            }
        } catch (SQLException e) {
            logger.error("Error executing raw postgres query: " + sql, e);
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public boolean executeUpdate(String sql, List<Object> params) {
        logger.info("Executing raw update: " + sql);
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (params != null) {
                setParameters(stmt, params);
            }
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error("Error executing raw postgres update: " + sql, e);
            throw new RuntimeException(e);
        }
    }
}
