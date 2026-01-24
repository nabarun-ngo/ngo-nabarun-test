package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("transactions")
public class TransactionDBModel {
    private String id;
    private String type;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String fromAccountId;
    private String toAccountId;
    private BigDecimal fromAccountBalance;
    private BigDecimal toAccountBalance;
    private String referenceId;
    private String referenceType;
    private String description;
    private Map<String, Object> metadata;
    private Date transactionDate;
    private String particulars;
    private String createdById;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
    private String donationId;
}
