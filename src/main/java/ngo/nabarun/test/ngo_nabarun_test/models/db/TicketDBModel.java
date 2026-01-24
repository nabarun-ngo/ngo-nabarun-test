package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;

import ngo.nabarun.test.ngo_nabarun_test.utils.DbEntity;

@DbEntity("tickets")
public class TicketDBModel {

	private String id;
	private String type;
	private String scope;

	private String forUserId;
	private String name;
	private String email;
	private String mobileNumber;
	private String communicationMethod;
	private String refId;


	private String oneTimePassword;
	private int incorrectOTPCount;
	private String token;
	private String baseTicketUrl;
	
	private String acceptCode;
	private String declineCode;


	private String status;

	private Date expireOn;
	private Date createdOn;
	private String createdBy;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getForUserId() {
		return forUserId;
	}
	public void setForUserId(String forUserId) {
		this.forUserId = forUserId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCommunicationMethod() {
		return communicationMethod;
	}
	public void setCommunicationMethod(String communicationMethod) {
		this.communicationMethod = communicationMethod;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getOneTimePassword() {
		return oneTimePassword;
	}
	public void setOneTimePassword(String oneTimePassword) {
		this.oneTimePassword = oneTimePassword;
	}
	public int getIncorrectOTPCount() {
		return incorrectOTPCount;
	}
	public void setIncorrectOTPCount(int incorrectOTPCount) {
		this.incorrectOTPCount = incorrectOTPCount;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getBaseTicketUrl() {
		return baseTicketUrl;
	}
	public void setBaseTicketUrl(String baseTicketUrl) {
		this.baseTicketUrl = baseTicketUrl;
	}
	public String getAcceptCode() {
		return acceptCode;
	}
	public void setAcceptCode(String acceptCode) {
		this.acceptCode = acceptCode;
	}
	public String getDeclineCode() {
		return declineCode;
	}
	public void setDeclineCode(String declineCode) {
		this.declineCode = declineCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getExpireOn() {
		return expireOn;
	}
	public void setExpireOn(Date expireOn) {
		this.expireOn = expireOn;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}
