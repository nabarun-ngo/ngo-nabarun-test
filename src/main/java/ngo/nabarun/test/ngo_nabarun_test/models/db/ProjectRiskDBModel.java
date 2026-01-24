package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("project_risks")
public class ProjectRiskDBModel {
    private String id;
    private String projectId;
    private String title;
    private String description;
    private String category;
    private String severity;
    private String probability;
    private String status;
    private String impact;
    private String mitigationPlan;
    private String ownerId;
    private Date identifiedDate;
    private Date resolvedDate;
    private String notes;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
