package ngo.nabarun.test.ngo_nabarun_test.repository;

import ngo.nabarun.test.ngo_nabarun_test.models.db.WorkflowInstanceDBModel;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterBeanMapper(WorkflowInstanceDBModel.class)
public interface WorkflowDAO {

    @SqlQuery("SELECT count(*) FROM \"task_assignments\" ta " +
            "JOIN \"workflow_tasks\" wt ON ta.\"taskId\" = wt.id " +
            "JOIN \"workflow_instances\" wi ON wt.\"workflowId\" = wi.id " +
            "WHERE (wi.\"id\" = :id) AND ta.\"assignedToId\" = :userId AND ta.\"status\" = 'PENDING'")
    int getAssignmentCountByWorkflowId(@Bind("id") String workflowId, @Bind("userId") String userId);

    @SqlQuery("SELECT * FROM \"workflow_instances\" WHERE \"id\" = :id LIMIT 1")
    WorkflowInstanceDBModel findWorkflowByIdOrName(@Bind("id") String id);
}
