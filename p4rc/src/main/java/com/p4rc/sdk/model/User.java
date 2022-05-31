package com.p4rc.sdk.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User {

	private String offerCountry;
	private String email;
	private Integer totalPoints;
	private Integer userType;
	private String image;
	private Integer totalLifeTimePoints;
	private Integer userSource;
	private String firstName;
	private String lastName;
	private Boolean userAcceptedTerms;
	private String userAvatarURL;
	private Integer facebookId;
	private Integer profileCompletionPercent;
	private Boolean fbPublishAllowed;
	private Boolean admin;
	private Integer id;

	public String getOfferCountry() {
		return offerCountry;
	}

	public void setOfferCountry(String offerCountry) {
		this.offerCountry = offerCountry;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getTotalLifeTimePoints() {
		return totalLifeTimePoints;
	}

	public void setTotalLifeTimePoints(Integer totalLifeTimePoints) {
		this.totalLifeTimePoints = totalLifeTimePoints;
	}

	public Integer getUserSource() {
		return userSource;
	}

	public void setUserSource(Integer userSource) {
		this.userSource = userSource;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getUserAcceptedTerms() {
		return userAcceptedTerms;
	}

	public void setUserAcceptedTerms(Boolean userAcceptedTerms) {
		this.userAcceptedTerms = userAcceptedTerms;
	}

	public String getUserAvatarURL() {
		return userAvatarURL;
	}

	public void setUserAvatarURL(String userAvatarURL) {
		this.userAvatarURL = userAvatarURL;
	}

	public Integer getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(Integer facebookId) {
		this.facebookId = facebookId;
	}

	public Integer getProfileCompletionPercent() {
		return profileCompletionPercent;
	}

	public void setProfileCompletionPercent(Integer profileCompletionPercent) {
		this.profileCompletionPercent = profileCompletionPercent;
	}

	public Boolean getFbPublishAllowed() {
		return fbPublishAllowed;
	}

	public void setFbPublishAllowed(Boolean fbPublishAllowed) {
		this.fbPublishAllowed = fbPublishAllowed;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static User fromJSON(JSONObject json) {
		if (json == null) {
			return null;
		}
		User user = new User();
		user.setOfferCountry(json.optString("offerCountry"));
		user.setEmail(json.optString("email"));
		user.setTotalPoints(json.optInt("totalPoints"));
		user.setUserType(json.optInt("userType"));
		user.setImage(json.optString("image"));
		user.setTotalLifeTimePoints(json.optInt("totalLifeTimePoints"));
		user.setUserSource(json.optInt("userSource"));
		user.setFirstName(json.optString("firstName"));
		user.setLastName(json.optString("lastName"));
		user.setUserAcceptedTerms(json.optBoolean("userAcceptedTerms"));
		user.setUserAvatarURL(json.optString("userAvatarURL"));
		user.setFacebookId(json.optInt("facebookId"));
		user.setProfileCompletionPercent(json.optInt("profileCompletionPercent"));
		user.setFbPublishAllowed(json.optBoolean("fbPublishAllowed"));
		user.setAdmin(json.optBoolean("admin"));
		user.setId(json.optInt("id"));
		return user;
	}

	@Override
	public String toString() {
		return "User{" +
				"offerCountry='" + offerCountry + '\'' +
				", email='" + email + '\'' +
				", totalPoints=" + totalPoints +
				", userType=" + userType +
				", image='" + image + '\'' +
				", totalLifeTimePoints=" + totalLifeTimePoints +
				", userSource=" + userSource +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", userAcceptedTerms=" + userAcceptedTerms +
				", userAvatarURL='" + userAvatarURL + '\'' +
				", facebookId=" + facebookId +
				", profileCompletionPercent=" + profileCompletionPercent +
				", fbPublishAllowed=" + fbPublishAllowed +
				", admin=" + admin +
				", id=" + id +
				'}';
	}
}