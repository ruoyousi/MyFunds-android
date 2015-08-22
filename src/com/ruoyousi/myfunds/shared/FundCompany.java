package com.ruoyousi.myfunds.shared;

import java.io.Serializable;

public class FundCompany implements Serializable {

	private static final long serialVersionUID = 4682842876275511220L;
	
	public static final String OBJECT = "FundCompany";
	
	public static final String COMPANY_ID = "CompanyId";
	
	public static final String COMPANY_NAME = "CompanyName";
	
	public static final String WEBSITE = "Website";
	
	public static final String TELEPHONE = "Telephone";

	private long companyId;

	private String companyName;

	private String website;

	private String telephone;

	public FundCompany() {

	}

	public FundCompany(long companyId, String companyName, String website,
			String telephone) {
		this.companyId = companyId;
		this.companyName = companyName;
		this.website = website;
		this.telephone = telephone;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("{").append("Id:").append(companyId)
				.append(", Name:").append(companyName).append(", Website:")
				.append(website).append(", Telephone:").append(telephone)
				.append("}").toString();
	}

}
