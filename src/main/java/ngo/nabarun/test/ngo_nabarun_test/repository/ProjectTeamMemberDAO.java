package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.ProjectTeamMemberDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(ProjectTeamMemberDBModel.class)
public interface ProjectTeamMemberDAO {

    @SqlQuery("SELECT * FROM \"project_team_members\" WHERE \"projectId\" = :projectId")
    List<ProjectTeamMemberDBModel> findByProjectId(@Bind("projectId") String projectId);

    @SqlQuery("SELECT ptm.* FROM \"project_team_members\" ptm JOIN \"user_profiles\" u ON ptm.\"userId\" = u.\"id\" WHERE u.\"email\" = :email")
    List<ProjectTeamMemberDBModel> findByUserEmail(@Bind("email") String email);
}
