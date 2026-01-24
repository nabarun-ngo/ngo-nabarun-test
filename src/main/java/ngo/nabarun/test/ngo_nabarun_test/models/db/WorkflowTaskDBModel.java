package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("workflow_tasks")
public class WorkflowTaskDBModel {
    private String id;
    private String stepId;
    private String taskId;
    private String workflowId;
    private String name;
    private String description;
    private String type;
    private String status;
    private String handler;
    private String checklist;
    private Boolean autoCloseable;
    private String autoCloseRefId;
    private String jobId;
    private String resultData;
    private Date completedAt;
    private String completedById;
    private String remarks;
    private Date createdAt;
    private Date updatedAt;
}
