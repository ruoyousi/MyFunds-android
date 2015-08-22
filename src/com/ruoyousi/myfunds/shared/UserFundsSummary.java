package com.ruoyousi.myfunds.shared;

public class UserFundsSummary {

	private double totalPrincial = 0;
	private double totalMoney = 0;

	public UserFundsSummary(double totalPrincial, double totalMoney) {
		this.totalPrincial = totalPrincial;
		this.totalMoney = totalMoney;
	}

	public double getTotalPrincial() {
		return totalPrincial;
	}

	public void setTotalPrincial(double totalPrincial) {
		this.totalPrincial = totalPrincial;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

}
