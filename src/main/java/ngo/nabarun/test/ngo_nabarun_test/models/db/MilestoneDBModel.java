package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;
import java.util.List;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("milestones")
public class MilestoneDBModel {
    private String id;
    private String projectId;
    private String name;
    private String description;
    private Date targetDate;
    private Date actualDate;
    private String status;
    private String importance;
    private List<String> dependencies;
    private String notes;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
