package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("earnings")
public class EarningDBModel {
    private String id;
    private String category;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String description;
    private String source;
    private String referenceId;
    private String referenceType;
    private String accountId;
    private String transactionId;
    private Date earningDate;
    private Date receivedDate;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
