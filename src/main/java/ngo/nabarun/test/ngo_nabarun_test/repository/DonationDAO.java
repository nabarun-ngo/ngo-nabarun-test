package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.DonationDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;
import java.util.List;

@RegisterBeanMapper(DonationDBModel.class)
public interface DonationDAO {

    @SqlQuery("SELECT * FROM \"donations\" WHERE \"raisedOn\" >= :startDate AND \"raisedOn\" <= :endDate AND \"donorId\" = :profileId AND \"type\" = :type")
    List<DonationDBModel> findBetweenDates(@Bind("startDate") Date startDate, @Bind("endDate") Date endDate,
            @Bind("profileId") String profileId, @Bind("type") String type);

    @SqlUpdate("DELETE FROM \"donations\" WHERE \"id\" = :id")
    int deleteById(@Bind("id") String id);
}
