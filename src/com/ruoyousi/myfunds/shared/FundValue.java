package com.ruoyousi.myfunds.shared;

import java.io.Serializable;
import java.util.Date;

import com.ruoyousi.common.util.MathUtil;


public class FundValue implements Serializable {

	private static final long serialVersionUID = 6120246271364769862L;

	public static final String OBJECT = "FundValue";
	public static final String VIEW_HISTORY_VALUE = "FundHistoryValue";
	public static final String THE_DATE = "TheDate";
	public static final String VALUE = "Value";
	public static final String LAST_DATE = "LastDate";
	public static final String LAST_VALUE = "LastValue";
	public static final String DIVIDEND = "Dividend";
	public static final String ACCUMULATION = "Accumulation";

	private String productId;

	private String productName;

	private java.util.Date theDate;

	private double value;

	private double accumulation;

	private double dividend;

	private long companyId;

	public FundValue() {

	}

	public FundValue(String productId, String productName, Date theDate,
			double value, double accumulation, double dividend, long companyId) {
		this.productId = productId;
		this.productName = productName;
		this.theDate = theDate;
		this.companyId = companyId;
		this.value = value;
		this.accumulation = accumulation;
		this.dividend = dividend;
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

	public java.util.Date getTheDate() {
		return theDate;
	}

	public void setTheDate(java.util.Date theDate) {
		this.theDate = theDate;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getAccumulation() {
		return accumulation;
	}

	public void setAccumulation(double accumulation) {
		this.accumulation = accumulation;
	}

	public double getDividend() {
		return dividend;
	}

	public void setDividend(double dividend) {
		this.dividend = dividend;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
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
				.append(", Name:").append(productName).append(", Date:")
				.append(theDate).append(", Value:").append(value).append(
						", Accumulation:").append(accumulation).append(
						", Dividend:").append(dividend).append("}").toString();
	}
}
