package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("project_team_members")
public class ProjectTeamMemberDBModel {
    private String id;
    private String projectId;
    private String userId;
    private String role;
    private String responsibilities;
    private Date startDate;
    private Date endDate;
    private BigDecimal hoursAllocated;
    private Boolean isActive;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
}
