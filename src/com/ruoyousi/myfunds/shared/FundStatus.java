package com.ruoyousi.myfunds.shared;

import java.io.Serializable;

public class FundStatus implements Serializable {

	private static final long serialVersionUID = -6226694813580969557L;

	public static final FundStatus Normal = new FundStatus(0, "normal");
	public static final FundStatus Only_Redeem = new FundStatus(1, "onlyRedeem");
	public static final FundStatus Closing = new FundStatus(2, "closing");
	public static final FundStatus Only_Transaction = new FundStatus(3,
			"onlyTransaction");
	public static final FundStatus Not_LargeAmount = new FundStatus(4,
			"notLargeAmount");

	private int index = 0;
	private String name;

	private FundStatus(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public static FundStatus[] Types() {
		FundStatus[] types = new FundStatus[5];
		types[0] = Normal;
		types[1] = Only_Redeem;
		types[2] = Closing;
		types[3] = Only_Transaction;
		types[4] = Not_LargeAmount;
		return types;
	}

	public static FundStatus Type(int index) {
		if (index == Only_Redeem.index)
			return Only_Redeem;
		else if (index == Closing.index)
			return Closing;
		else if (index == Only_Transaction.index)
			return Only_Transaction;
		else if (index == Not_LargeAmount.index)
			return Not_LargeAmount;
		return Normal;
	}

	public static FundStatus Type(String name) {
		if (name.equals(Only_Redeem.name))
			return Only_Redeem;
		else if (name.equals(Closing.name))
			return Closing;
		else if (name.equals(Only_Transaction.name))
			return Only_Transaction;
		else if (name.equals(Not_LargeAmount.name))
			return Not_LargeAmount;
		return Normal;
	}

}
