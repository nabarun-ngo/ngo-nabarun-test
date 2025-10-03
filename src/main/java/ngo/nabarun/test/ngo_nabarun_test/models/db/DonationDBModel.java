package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("donations")
public class DonationDBModel {
    @Id
	private String id;
		
	private Double amount;
	
	private Date startDate;
	
	private Date endDate;
	
	private Date raisedOn;
	
	private String type;
	
	private String status;
	
	private Date paidOn;
	
	private String transactionRefNumber;
	
	private String paymentConfirmedBy;
	
	private Date paymentConfirmedOn;
	
	private String comment;
	
	private String donorName;
	
	private String donorEmailAddress;
	
	private String donorContactNumber;
	
	private boolean deleted;
		
	private String paymentMethod;
	
	private Boolean isGuest;
	
	private String accountId;
	private String accountName;

	private String profile;
	private String userId;
    
   	private String eventId;
   	
	private String paidUPIName;
	private Boolean isPaymentNotified;
	private Date notifiedOn;

	private String paymentConfirmedByName;


	private String cancelReason;
	private String payLaterReason;
	private String paymentFailDetail;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getRaisedOn() {
		return raisedOn;
	}
	public void setRaisedOn(Date raisedOn) {
		this.raisedOn = raisedOn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getPaidOn() {
		return paidOn;
	}
	public void setPaidOn(Date paidOn) {
		this.paidOn = paidOn;
	}
	public String getTransactionRefNumber() {
		return transactionRefNumber;
	}
	public void setTransactionRefNumber(String transactionRefNumber) {
		this.transactionRefNumber = transactionRefNumber;
	}
	public String getPaymentConfirmedBy() {
		return paymentConfirmedBy;
	}
	public void setPaymentConfirmedBy(String paymentConfirmedBy) {
		this.paymentConfirmedBy = paymentConfirmedBy;
	}
	public Date getPaymentConfirmedOn() {
		return paymentConfirmedOn;
	}
	public void setPaymentConfirmedOn(Date paymentConfirmedOn) {
		this.paymentConfirmedOn = paymentConfirmedOn;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDonorName() {
		return donorName;
	}
	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}
	public String getDonorEmailAddress() {
		return donorEmailAddress;
	}
	public void setDonorEmailAddress(String donorEmailAddress) {
		this.donorEmailAddress = donorEmailAddress;
	}
	public String getDonorContactNumber() {
		return donorContactNumber;
	}
	public void setDonorContactNumber(String donorContactNumber) {
		this.donorContactNumber = donorContactNumber;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public Boolean getIsGuest() {
		return isGuest;
	}
	public void setIsGuest(Boolean isGuest) {
		this.isGuest = isGuest;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public String getPaidUPIName() {
		return paidUPIName;
	}
	public void setPaidUPIName(String paidUPIName) {
		this.paidUPIName = paidUPIName;
	}
	public Boolean getIsPaymentNotified() {
		return isPaymentNotified;
	}
	public void setIsPaymentNotified(Boolean isPaymentNotified) {
		this.isPaymentNotified = isPaymentNotified;
	}
	public Date getNotifiedOn() {
		return notifiedOn;
	}
	public void setNotifiedOn(Date notifiedOn) {
		this.notifiedOn = notifiedOn;
	}
	public String getPaymentConfirmedByName() {
		return paymentConfirmedByName;
	}
	public void setPaymentConfirmedByName(String paymentConfirmedByName) {
		this.paymentConfirmedByName = paymentConfirmedByName;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getPayLaterReason() {
		return payLaterReason;
	}
	public void setPayLaterReason(String payLaterReason) {
		this.payLaterReason = payLaterReason;
	}
	public String getPaymentFailDetail() {
		return paymentFailDetail;
	}
	public void setPaymentFailDetail(String paymentFailDetail) {
		this.paymentFailDetail = paymentFailDetail;
	}
}
