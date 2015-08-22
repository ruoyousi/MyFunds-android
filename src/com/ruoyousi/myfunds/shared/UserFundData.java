package com.ruoyousi.myfunds.shared;

import java.io.Serializable;
import java.util.Date;

import com.ruoyousi.common.util.MathUtil;

public class UserFundData implements Serializable {

	private static final long serialVersionUID = 3389974160026953060L;

	public static final String OBJECT = "UserFundData";

	public static final String QUANTITY = "Quantity";
	public static final String TOTAL = "Total";
	public static final String PRINCIPAL = "Principal";
	public static final String GROWTH = "Growth";

	public static final String EARNING = "Earning";
	public static final String EARNING_RATE = "EarningRate";

	private String productId;
	private java.util.Date theDate = null;
	private String productName = null;
	private double quantity = 0;
	private double value = 0;

	public UserFundData(String productId, String productName, Date theDate,
			double value, Date lastDate, double lastValue, double quantity,
			double principal) {
		this.productId = productId;
		this.productName = productName;
		this.theDate = theDate;
		this.value = value;
		this.lastDate = lastDate;
		this.lastValue = lastValue;
		this.quantity = quantity;
		this.principal = principal;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public java.util.Date getTheDate() {
		return theDate;
	}

	public void setTheDate(java.util.Date theDate) {
		this.theDate = theDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getTotal() {
		return MathUtil.round(MathUtil.multiply(quantity, value), 2);
	}

	private double principal = 0;

	public void setPrincipal(double principal) {
		this.principal = principal;
	}

	public double getPrincipal() {
		return principal;
	}

	public double getGrowth() {
		if (principal == 0)
			return 0;
		return MathUtil.growthRate(getTotal(), principal);
	}

	private double lastValue = 0;

	public void setLastValue(double lastValue) {
		this.lastValue = lastValue;
	}

	public double getLastValue() {
		return lastValue;
	}

	private Date lastDate;

	public java.util.Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(java.util.Date lastDate) {
		this.lastDate = lastDate;
	}

	public double getEarningRate() {
		if (lastValue <= 0)
			return 0;
		return MathUtil.growthRate(value, lastValue);
	}

	@Override
	public String toString() {
		return new StringBuilder().append("{").append("Id:").append(productId)
				.append(", Date:").append(theDate).append(", Quantity:")
				.append(quantity).append(", Value:").append(value).append("}")
				.toString();
	}
}
