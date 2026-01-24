package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("projects")
public class ProjectDBModel {
    private String id;
    private String name;
    private String description;
    private String code;
    private String category;
    private String status;
    private String phase;
    private Date startDate;
    private Date endDate;
    private Date actualEndDate;
    private BigDecimal budget;
    private BigDecimal spentAmount;
    private String currency;
    private String location;
    private Integer targetBeneficiaryCount;
    private Integer actualBeneficiaryCount;
    private String managerId;
    private String sponsorId;
    private List<String> tags;
    private Map<String, Object> metadata;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
