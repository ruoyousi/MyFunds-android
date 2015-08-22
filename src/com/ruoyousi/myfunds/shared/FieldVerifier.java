package com.ruoyousi.myfunds.shared;

public class FieldVerifier {

	private FieldVerifier() {

	}

	public static boolean isValidProductId(String text) {
		return text.matches("^[0-9]{6}$");
	}

	public static boolean isValidQuantity(String text) {
		return text.matches("^\\d+([.]\\d{0,2})?$");
	}
	
	public static boolean isValidMoney(String text) {
		return text.matches("^\\d+([.]\\d{0,2})?$");
	}

}
