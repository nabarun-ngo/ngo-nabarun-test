package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.ExpenseDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(ExpenseDBModel.class)
public interface ExpenseDAO {

    @SqlQuery("SELECT * FROM \"expenses\" WHERE \"status\" = :status")
    List<ExpenseDBModel> findByStatus(@Bind("status") String status);

    @SqlQuery("SELECT e.* FROM \"expenses\" e JOIN \"accounts\" a ON e.\"accountId\" = a.\"id\" WHERE a.\"name\" = :accountName")
    List<ExpenseDBModel> findByAccountName(@Bind("accountName") String accountName);

    @SqlQuery("SELECT e.* FROM \"expenses\" e JOIN \"projects\" p ON e.\"referenceId\" = p.\"id\" WHERE p.\"code\" = :projectCode AND e.\"referenceType\" = 'PROJECT'")
    List<ExpenseDBModel> findByProjectCode(@Bind("projectCode") String projectCode);
}
