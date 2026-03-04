package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.ProjectDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(ProjectDBModel.class)
public interface ProjectDAO {

    @SqlQuery("SELECT * FROM \"projects\"")
    List<ProjectDBModel> findAll();

    @SqlQuery("SELECT * FROM \"projects\" WHERE \"id\" = :id")
    ProjectDBModel findById(@Bind("id") String id);

    @SqlQuery("SELECT * FROM \"projects\" WHERE \"status\" = :status")
    List<ProjectDBModel> findByStatus(@Bind("status") String status);

    @SqlQuery("SELECT p.* FROM \"projects\" p JOIN \"user_profiles\" u ON p.\"managerId\" = u.\"id\" WHERE u.\"email\" = :managerEmail")
    List<ProjectDBModel> findByManagerEmail(@Bind("managerEmail") String managerEmail);
}
