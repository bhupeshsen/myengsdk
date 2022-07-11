package com.p4rc.sdk.model;

import com.p4rc.sdk.model.gamelist.GamePoints;
import com.p4rc.sdk.utils.Constants;

import org.json.JSONObject;

import java.io.Serializable;

public class CompanyDetails implements Serializable {

	private String domainName;
	private String firstName;
	private String lastName;
	private String name;


	// Getter Methods

	public String getDomainName() {
		return domainName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getName() {
		return name;
	}

	// Setter Methods

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static CompanyDetails fromJSON(JSONObject json) {
		CompanyDetails companyDetails = new CompanyDetails();
		companyDetails.setName(json.optString("name"));
		companyDetails.setDomainName(json.optString("domainName"));
		companyDetails.setFirstName(json.optString("firstName"));
		companyDetails.setLastName(json.optString("lastName"));

		return companyDetails;
	}
}
