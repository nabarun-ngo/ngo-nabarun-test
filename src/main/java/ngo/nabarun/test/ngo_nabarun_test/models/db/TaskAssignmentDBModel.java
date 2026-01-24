package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("task_assignments")
public class TaskAssignmentDBModel {
    private String id;
    private String taskId;
    private String assignedToId;
    private String roleName;
    private String assignedBy;
    private String status;
    private Date acceptedAt;
    private Date completedAt;
    private String notes;
    private Date createdAt;
    private Date updatedAt;
}
