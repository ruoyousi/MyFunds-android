package com.ruoyousi.myfunds.shared;

public class FormatUtils {

	private FormatUtils() {

	}

	public static String formatNetValue(double value) {
		return String.format("%.4f", value);
	}

	public static String formatMoney(double value) {
		return String.format("%.2f", value);
	}

	public static String formatQuantity(double value) {
		return String.format("%.2f", value);
	}

	public static String formatEarningRate(double value) {
		return String.format("%.2f", value) + "%";
	}

}
