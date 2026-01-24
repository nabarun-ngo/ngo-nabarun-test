package ngo.nabarun.test.ngo_nabarun_test.utils;

public class DBFilter {
    public enum Operator {
        EQ("="), GTE(">="), LTE("<="), IN("IN"), NE("<>");

        private final String sqlOperator;

        Operator(String sqlOperator) {
            this.sqlOperator = sqlOperator;
        }

        public String getSqlOperator() {
            return sqlOperator;
        }
    }

    private String field;
    private Operator operator;
    private Object value;

    public DBFilter(String field, Operator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public static DBFilter eq(String field, Object value) {
        return new DBFilter(field, Operator.EQ, value);
    }

    public static DBFilter gte(String field, Object value) {
        return new DBFilter(field, Operator.GTE, value);
    }

    public static DBFilter lte(String field, Object value) {
        return new DBFilter(field, Operator.LTE, value);
    }

    public static DBFilter ne(String field, Object value) {
        return new DBFilter(field, Operator.NE, value);
    }

    public String getField() {
        return field;
    }

    public Operator getOperator() {
        return operator;
    }

    public Object getValue() {
        return value;
    }
}
