package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.TicketDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterBeanMapper(TicketDBModel.class)
public interface TicketDAO {

    @SqlQuery("SELECT * FROM \"tickets\" WHERE \"email\" = :email LIMIT 1")
    TicketDBModel findFirstByEmail(@Bind("email") String email);
}
