package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.MeetingDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterBeanMapper(MeetingDBModel.class)
public interface MeetingDAO {

    @SqlQuery("SELECT * FROM \"meetings\" WHERE \"creatorEmail\" = :email")
    List<MeetingDBModel> findByCreatorEmail(@Bind("email") String email);

    @SqlQuery("SELECT m.* FROM \"meetings\" m JOIN \"projects\" p ON m.\"meetingRefId\" = p.\"id\" WHERE p.\"code\" = :projectCode AND m.\"meetingRefType\" = 'PROJECT'")
    List<MeetingDBModel> findByProjectCode(@Bind("projectCode") String projectCode);
}
