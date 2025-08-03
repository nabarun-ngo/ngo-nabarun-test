package ngo.nabarun.test.ngo_nabarun_test.models.db;

import java.util.Date;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("user_profiles")
public class UserDBModel {

	@Id
	private String id;
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String avatarUrl;
	private Date dateOfBirth;
	private String gender;
	private String about;

	private String roleNames;
	private String roleCodes;
	private String email;
	private String phoneNumber;
	private String altPhoneNumber;

	private Date createdOn;
	private String createdBy;

	private String userId;
	private Boolean activeContributor;
	private Boolean publicProfile;
	private String status;

	private String addressLine1;
	private String addressLine2;
	private String addressLine3;
	private String hometown;
	private String district;
	private String state;
	private String country;

	private String facebookLink;
	private String instagramLink;
	private String twitterLink;
	private String linkedInLink;
	private String whatsappLink;

	private boolean deleted;

	private String permanentAddressLine1;
	private String permanentAddressLine2;
	private String permanentAddressLine3;
	private String permanentHometown;
	private String permanentDistrict;
	private String permanentState;
	private String permanentCountry;
	private Boolean presentPermanentSame;
	private Date donationPauseStartDate;
	private Date donationPauseEndDate;
	private String loginMethods;

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public String getRoleCodes() {
		return roleCodes;
	}

	public void setRoleCodes(String roleCodes) {
		this.roleCodes = roleCodes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAltPhoneNumber() {
		return altPhoneNumber;
	}

	public void setAltPhoneNumber(String altPhoneNumber) {
		this.altPhoneNumber = altPhoneNumber;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getActiveContributor() {
		return activeContributor;
	}

	public void setActiveContributor(Boolean activeContributor) {
		this.activeContributor = activeContributor;
	}

	public Boolean getPublicProfile() {
		return publicProfile;
	}

	public void setPublicProfile(Boolean publicProfile) {
		this.publicProfile = publicProfile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine3() {
		return addressLine3;
	}

	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFacebookLink() {
		return facebookLink;
	}

	public void setFacebookLink(String facebookLink) {
		this.facebookLink = facebookLink;
	}

	public String getInstagramLink() {
		return instagramLink;
	}

	public void setInstagramLink(String instagramLink) {
		this.instagramLink = instagramLink;
	}

	public String getTwitterLink() {
		return twitterLink;
	}

	public void setTwitterLink(String twitterLink) {
		this.twitterLink = twitterLink;
	}

	public String getLinkedInLink() {
		return linkedInLink;
	}

	public void setLinkedInLink(String linkedInLink) {
		this.linkedInLink = linkedInLink;
	}

	public String getWhatsappLink() {
		return whatsappLink;
	}

	public void setWhatsappLink(String whatsappLink) {
		this.whatsappLink = whatsappLink;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getPermanentAddressLine1() {
		return permanentAddressLine1;
	}

	public void setPermanentAddressLine1(String permanentAddressLine1) {
		this.permanentAddressLine1 = permanentAddressLine1;
	}

	public String getPermanentAddressLine2() {
		return permanentAddressLine2;
	}

	public void setPermanentAddressLine2(String permanentAddressLine2) {
		this.permanentAddressLine2 = permanentAddressLine2;
	}

	public String getPermanentAddressLine3() {
		return permanentAddressLine3;
	}

	public void setPermanentAddressLine3(String permanentAddressLine3) {
		this.permanentAddressLine3 = permanentAddressLine3;
	}

	public String getPermanentHometown() {
		return permanentHometown;
	}

	public void setPermanentHometown(String permanentHometown) {
		this.permanentHometown = permanentHometown;
	}

	public String getPermanentDistrict() {
		return permanentDistrict;
	}

	public void setPermanentDistrict(String permanentDistrict) {
		this.permanentDistrict = permanentDistrict;
	}

	public String getPermanentState() {
		return permanentState;
	}

	public void setPermanentState(String permanentState) {
		this.permanentState = permanentState;
	}

	public String getPermanentCountry() {
		return permanentCountry;
	}

	public void setPermanentCountry(String permanentCountry) {
		this.permanentCountry = permanentCountry;
	}

	public Boolean getPresentPermanentSame() {
		return presentPermanentSame;
	}

	public void setPresentPermanentSame(Boolean presentPermanentSame) {
		this.presentPermanentSame = presentPermanentSame;
	}

	public Date getDonationPauseStartDate() {
		return donationPauseStartDate;
	}

	public void setDonationPauseStartDate(Date donationPauseStartDate) {
		this.donationPauseStartDate = donationPauseStartDate;
	}

	public Date getDonationPauseEndDate() {
		return donationPauseEndDate;
	}

	public void setDonationPauseEndDate(Date donationPauseEndDate) {
		this.donationPauseEndDate = donationPauseEndDate;
	}

	public String getLoginMethods() {
		return loginMethods;
	}

	public void setLoginMethods(String loginMethods) {
		this.loginMethods = loginMethods;
	}
}
