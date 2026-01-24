package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("workflow_steps")
public class WorkflowStepDBModel {
    private String id;
    private String instanceId;
    private String stepId;
    private String name;
    private String description;
    private String status;
    private Integer orderIndex;
    private String onSuccessStepId;
    private String onFailureStepId;
    private String remarks;
    private Date startedAt;
    private Date completedAt;
    private Date createdAt;
    private Date updatedAt;
}
