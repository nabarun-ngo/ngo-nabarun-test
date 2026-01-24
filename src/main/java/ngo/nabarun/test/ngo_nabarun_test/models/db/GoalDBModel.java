package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("goals")
public class GoalDBModel {
    private String id;
    private String projectId;
    private String title;
    private String description;
    private BigDecimal targetValue;
    private String targetUnit;
    private BigDecimal currentValue;
    private Date deadline;
    private String priority;
    private String status;
    private BigDecimal weight;
    private List<String> dependencies;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
