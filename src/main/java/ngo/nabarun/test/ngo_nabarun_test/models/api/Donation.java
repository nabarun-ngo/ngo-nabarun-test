package ngo.nabarun.test.ngo_nabarun_test.models.api;

import java.util.Date;

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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getIsGuest() {
		return isGuest;
	}
	public void setIsGuest(Boolean isGuest) {
		this.isGuest = isGuest;
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
	public String getDonationType() {
		return donationType;
	}
	public void setDonationType(String donationType) {
		this.donationType = donationType;
	}
	public String getDonationStatus() {
		return donationStatus;
	}
	public void setDonationStatus(String donationStatus) {
		this.donationStatus = donationStatus;
	}
	public Date getPaidOn() {
		return paidOn;
	}
	public void setPaidOn(Date paidOn) {
		this.paidOn = paidOn;
	}
	public User getPaymentConfirmedBy() {
		return paymentConfirmedBy;
	}
	public void setPaymentConfirmedBy(User paymentConfirmedBy) {
		this.paymentConfirmedBy = paymentConfirmedBy;
	}
	public Date getPaymentConfirmedOn() {
		return paymentConfirmedOn;
	}
	public void setPaymentConfirmedOn(Date paymentConfirmedOn) {
		this.paymentConfirmedOn = paymentConfirmedOn;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public User getDonorDetails() {
		return donorDetails;
	}
	public void setDonorDetails(User donorDetails) {
		this.donorDetails = donorDetails;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getPaidUsingUPI() {
		return paidUsingUPI;
	}
	public void setPaidUsingUPI(String paidUsingUPI) {
		this.paidUsingUPI = paidUsingUPI;
	}
	public boolean isPaymentNotified() {
		return isPaymentNotified;
	}
	public void setPaymentNotified(boolean isPaymentNotified) {
		this.isPaymentNotified = isPaymentNotified;
	}
	public String getTxnRef() {
		return txnRef;
	}
	public void setTxnRef(String txnRef) {
		this.txnRef = txnRef;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCancelletionReason() {
		return cancelletionReason;
	}
	public void setCancelletionReason(String cancelletionReason) {
		this.cancelletionReason = cancelletionReason;
	}
	public String getLaterPaymentReason() {
		return laterPaymentReason;
	}
	public void setLaterPaymentReason(String laterPaymentReason) {
		this.laterPaymentReason = laterPaymentReason;
	}
	public String getPaymentFailureDetail() {
		return paymentFailureDetail;
	}
	public void setPaymentFailureDetail(String paymentFailureDetail) {
		this.paymentFailureDetail = paymentFailureDetail;
	}

}
