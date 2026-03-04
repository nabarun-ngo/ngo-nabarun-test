package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(UserDBModel.class)
public interface UserDAO {

    @SqlQuery("SELECT * FROM \"user_profiles\" WHERE \"firstName\" = :firstName AND \"lastName\" = :lastName LIMIT 1")
    UserDBModel findByName(@Bind("firstName") String firstName, @Bind("lastName") String lastName);

    @SqlQuery("SELECT * FROM \"user_profiles\" WHERE \"roles\" = :roleCode")
    List<UserDBModel> findByRole(@Bind("roleCode") String roleCode);
}
