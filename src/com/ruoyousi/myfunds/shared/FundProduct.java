package com.ruoyousi.myfunds.shared;

import java.io.Serializable;
import java.util.Date;

public class FundProduct implements Serializable {

	private static final long serialVersionUID = 3071439266280683660L;
	
	public static final String OBJECT = "FundProduct";
	public static final String PRODUCT_ID = "ProductId";
	public static final String PRODUCT_NAME = "ProductName";
	public static final String CREATE_DATE = "CreateDate";
	public static final String FUND_TYPE = "FundType";
	public static final String FUND_STATUS = "FundStatus";

	private String productId;

	private String productName;

	private Date createDate;

	private int fundType;

	private long companyId;
	
	private int fundStatus;

	public FundProduct() {

	}

	public FundProduct(String productId, String productName, Date createDate,
			int fundType,int fundStatus, long companyId) {
		this.productId = productId;
		this.productName = productName;
		setCreateDate(createDate);
		this.fundType = fundType;
		this.fundStatus = fundStatus;
		this.companyId = companyId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = new Date(createDate.getYear(), createDate.getMonth(),
				createDate.getDate(), 0, 0, 0);
	}

	public int getFundType() {
		return fundType;
	}

	public void setFundType(int fundType) {
		this.fundType = fundType;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	
	public void setFundStatus(int fundStatus) {
		this.fundStatus = fundStatus;
	}

	public int getFundStatus() {
		return fundStatus;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("{").append("Id:").append(productId)
				.append(", Name:").append(productName).append(", Create Date:")
				.append(createDate).append("}").toString();
	}

}
