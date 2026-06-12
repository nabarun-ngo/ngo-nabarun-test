package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utils.DBUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step definitions for asserting database state after API operations.
 *
 * Steps use JDBI mapToMap() so they work against any table without a
 * dedicated DAO. Column names are matched case-sensitively against the
 * quoted PostgreSQL column names returned by JDBI.
 *
 * Supported steps:
 *   Then The database table "<table>" record with id "<id>" should have:
 *        | Field  | Value |
 *        | status | PAID  |
 *
 *   Then The database table "<table>" should contain at least <n> records
 *
 *   Then The database table "<table>" record with id "<id>" should not exist
 */
public class DbStepDefinitions {

    private static final Logger logger = LogManager.getLogger(DbStepDefinitions.class);
    private final ScenarioContext scenarioContext;

    public DbStepDefinitions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    /**
     * Fetches a single row from {@code table} WHERE id = {@code idExpression},
     * then asserts each Field/Value pair in the data table.
     *
     * <p>The {@code idExpression} supports scenario-context variables, e.g. "{ExpenseId}".
     * The {@code Value} column in the data table also supports variable resolution.</p>
     *
     * <pre>
     * Then The database table "expenses" record with id "{ExpenseId}" should have:
     *   | Field       | Value       |
     *   | status      | FINALIZED   |
     *   | currency    | INR         |
     * </pre>
     */
    @Then("The database table {string} record with id {string} should have:")
    public void dbRecordShouldHave(String table, String idExpression, List<Map<String, String>> fields) {
        String id = DataUtils.resolveData(idExpression, scenarioContext);
        logger.info("[DB ASSERT] Querying table='{}' id='{}'", table, id);

        Map<String, Object> row = DBUtils.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM public.\"" + table + "\" WHERE id = :id")
                        .bind("id", id)
                        .mapToMap()
                        .findOne()
                        .orElse(null)
        );

        assertNotNull(row,
                "[DB ASSERT] No record found in table '" + table + "' with id '" + id + "'");
        logger.info("[DB ASSERT] Record found: {}", row);

        for (Map<String, String> field : fields) {
            String column = field.get("Field");
            String expectedValue = DataUtils.resolveData(field.get("Value"), scenarioContext);
            Object actualRaw = row.get(column);
            String actualValue = actualRaw == null ? null : actualRaw.toString();

            logger.info("[DB ASSERT] table='{}' id='{}' column='{}' expected='{}' actual='{}'",
                    table, id, column, expectedValue, actualValue);
            assertEquals(expectedValue, actualValue,
                    "[DB ASSERT] Mismatch in table '" + table + "' id='" + id + "' column='" + column + "'");
        }
    }

    /**
     * Asserts that the total row count in {@code table} is at least {@code minCount}.
     *
     * <pre>
     * Then The database table "earnings" should contain at least 1 records
     * </pre>
     */
    @Then("The database table {string} should contain at least {int} records")
    public void dbTableShouldContainAtLeast(String table, int minCount) {
        logger.info("[DB ASSERT] Counting rows in table='{}'", table);

        long count = DBUtils.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM public.\"" + table + "\"")
                        .mapTo(Long.class)
                        .one()
        );

        logger.info("[DB ASSERT] table='{}' rowCount={} minExpected={}", table, count, minCount);
        assertTrue(count >= minCount,
                "[DB ASSERT] Expected at least " + minCount + " records in table '" + table
                        + "' but found " + count);
    }

    /**
     * Asserts that no row exists in {@code table} with the given id.
     * Useful for verifying soft-delete or hard-delete side effects.
     *
     * <pre>
     * Then The database table "donations" record with id "{DonationId}" should not exist
     * </pre>
     */
    @Then("The database table {string} record with id {string} should not exist")
    public void dbRecordShouldNotExist(String table, String idExpression) {
        String id = DataUtils.resolveData(idExpression, scenarioContext);
        logger.info("[DB ASSERT] Verifying absence in table='{}' id='{}'", table, id);

        long count = DBUtils.getJdbi().withHandle(handle ->
                handle.createQuery("SELECT COUNT(*) FROM public.\"" + table + "\" WHERE id = :id")
                        .bind("id", id)
                        .mapTo(Long.class)
                        .one()
        );

        logger.info("[DB ASSERT] table='{}' id='{}' count={}", table, id, count);
        assertEquals(0L, count,
                "[DB ASSERT] Expected no record in table '" + table + "' with id '" + id
                        + "' but found " + count + " row(s)");
    }
}
