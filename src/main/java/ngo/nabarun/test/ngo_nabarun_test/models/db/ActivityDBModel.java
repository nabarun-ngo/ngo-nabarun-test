package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("activities")
public class ActivityDBModel {
    private String id;
    private String projectId;
    private String name;
    private String description;
    private String scale;
    private String type;
    private String status;
    private String priority;
    private Date startDate;
    private Date endDate;
    private Date actualStartDate;
    private Date actualEndDate;
    private String location;
    private String venue;
    private String assignedTo;
    private String organizerId;
    private String parentActivityId;
    private Integer expectedParticipants;
    private Integer actualParticipants;
    private BigDecimal estimatedCost;
    private BigDecimal actualCost;
    private String currency;
    private List<String> tags;
    private Map<String, Object> metadata;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
