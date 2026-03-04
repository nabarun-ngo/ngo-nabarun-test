package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.AccountDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(AccountDBModel.class)
public interface AccountDAO {

    @SqlQuery("SELECT * FROM \"accounts\"")
    List<AccountDBModel> findAll();

    @SqlQuery("SELECT * FROM \"accounts\" WHERE \"status\" = :status")
    List<AccountDBModel> findByStatus(@Bind("status") String status);

    @SqlQuery("SELECT a.* FROM \"accounts\" a JOIN \"user_profiles\" u ON a.\"accountHolderId\" = u.\"id\" WHERE u.\"email\" = :email")
    List<AccountDBModel> findByHolderEmail(@Bind("email") String email);
}
