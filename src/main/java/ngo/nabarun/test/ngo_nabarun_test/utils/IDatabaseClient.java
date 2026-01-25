package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.util.List;
import java.util.Map;

public interface IDatabaseClient {
    <T> T findFirst(Class<T> clazz, List<DBFilter> filters);
    <T> List<T> findMany(Class<T> clazz, List<DBFilter> filters);
    <T> boolean delete(Class<T> clazz, List<DBFilter> filters);
    List<Map<String, Object>> executeQuery(String sql, List<Object> params);
    boolean executeUpdate(String sql, List<Object> params);
}
