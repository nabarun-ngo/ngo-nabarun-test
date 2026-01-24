package ngo.nabarun.test.ngo_nabarun_test.utils;

import java.util.List;

public interface IDatabaseClient {
    <T> T findFirst(Class<T> clazz, List<DBFilter> filters);
    <T> List<T> findMany(Class<T> clazz, List<DBFilter> filters);
    <T> boolean delete(Class<T> clazz, List<DBFilter> filters);
}
