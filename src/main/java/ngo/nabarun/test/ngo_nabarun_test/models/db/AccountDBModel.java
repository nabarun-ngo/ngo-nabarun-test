package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("accounts")
public class AccountDBModel {
    private String id;
    private String name;
    private String type;
    private BigDecimal balance;
    private String currency;
    private String status;
    private String description;
    private String accountHolderName;
    private String accountHolderId;
    private Date activatedOn;
    private String bankDetail;
    private String upiDetail;
    private String createdById;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
