package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import lombok.Data;
import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@Data
@DbEntity("donations")
public class DonationDBModel {
    private String id;
    private String type;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String donorId;
    private String donorName;
    private String donorEmail;
    private String donorPhone;
    private Boolean isGuest;
    private Date startDate;
    private Date endDate;
    private Date raisedOn;
    private Date paidOn;
    private String confirmedById;
    private Date confirmedOn;
    private String paymentMethod;
    private String paidToAccountId;
    private String forEventId;
    private String paidUsingUPI;
    private Boolean isPaymentNotified;
    private String transactionRef;
    private String remarks;
    private String cancelletionReason; // typo preserved from prisma
    private String laterPaymentReason;
    private String paymentFailureDetail;
    private Map<String, Object> additionalFields;
    private Date createdAt;
    private Date updatedAt;
    private Integer version;
    private Date deletedAt;
}
