package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("workflow_instances")
public class WorkflowInstanceDBModel {
    private String id;
    private String name;
    private String type;
    private String description;
    private String status;
    private String currentStepId;
    private String data;
    private String initiatedById;
    private String initiatedForId;
    private Date completedAt;
    private String remarks;
    private Boolean delegated;
    private Boolean isExtUser;
    private String extUserEmail;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
}
