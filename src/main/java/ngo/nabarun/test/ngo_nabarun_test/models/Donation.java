package ngo.nabarun.test.ngo_nabarun_test.models;

import java.util.Date;
import lombok.Data;

@Data
public class Donation {
	private String id;
	private Boolean isGuest;
	private Double amount;
	private Date startDate;
	private Date endDate;
	private Date raisedOn;
	private String donationType;
	private String donationStatus;
	private Date paidOn;
	private User paymentConfirmedBy;
	private Date paymentConfirmedOn;
	private String paymentMethod;
	private User donorDetails;
	private String eventName;
	private String paidUsingUPI;
	private boolean isPaymentNotified;
	private String txnRef;
	private String remarks;
	private String cancelletionReason;
	private String laterPaymentReason;
	private String paymentFailureDetail;

}
