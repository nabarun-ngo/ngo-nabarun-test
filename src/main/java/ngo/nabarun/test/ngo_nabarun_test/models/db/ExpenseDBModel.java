package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("expenses")
public class ExpenseDBModel {
    private String id;
    private String title;
    private String items;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String description;
    private String referenceId;
    private String referenceType;
    private Boolean isDelegated;
    private String createdById;
    private String paidById;
    private Date expenseDate;
    private String submittedById;
    private Date submittedOn;
    private String finalizedById;
    private Date finalizedOn;
    private String settledById;
    private Date settledOn;
    private String rejectedById;
    private Date rejectedOn;
    private String updatedById;
    private Date updatedOn;
    private String accountId;
    private String accountName;
    private String transactionRef;
    private String remarks;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
    private String userProfileId;
}
