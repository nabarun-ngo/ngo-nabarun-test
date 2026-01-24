package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("activity_expenses")
public class ActivityExpenseDBModel {
    private String id;
    private String activityId;
    private String expenseId;
    private BigDecimal allocationPercentage;
    private BigDecimal allocationAmount;
    private String notes;
    private String createdBy;
    private Date createdAt;
    private String userProfileId;
}
