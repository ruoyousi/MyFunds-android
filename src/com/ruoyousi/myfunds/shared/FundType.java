package com.ruoyousi.myfunds.shared;

import java.io.Serializable;

public class FundType implements Serializable {

	private static final long serialVersionUID = -6226694813580969557L;

	public static final FundType STOCK = new FundType(0, "stock");
	public static final FundType HYPER = new FundType(1, "hyper");
	public static final FundType BOND = new FundType(2, "bond");
	public static final FundType BREAKEVEN = new FundType(4, "breakeven");
	public static final FundType LOF = new FundType(5, "lof");
	public static final FundType INDEX = new FundType(6, "index");
	public static final FundType ETF = new FundType(7, "etf");
	public static final FundType QDII = new FundType(8, "qdii");

	private int index = 0;
	private String name;

	private FundType(int index, String name) {
		this.index = index;
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public static FundType[] Types() {
		FundType[] types = new FundType[8];
		types[0] = STOCK;
		types[1] = HYPER;
		types[2] = BOND;
		types[3] = BREAKEVEN;
		types[4] = LOF;
		types[5] = INDEX;
		types[6] = ETF;
		types[7] = QDII;
		return types;
	}

	public static FundType Type(int index) {
		if (index == BOND.index)
			return BOND;
		else if (index == HYPER.index)
			return HYPER;
		else if (index == BREAKEVEN.index)
			return BREAKEVEN;
		else if (index == LOF.index)
			return LOF;
		else if (index == ETF.index)
			return ETF;
		else if (index == INDEX.index)
			return INDEX;
		else if (index == QDII.index)
			return QDII;
		return STOCK;
	}
	
	public static FundType Type(String name) {
		if (name.equals(BOND.name))
			return BOND;
		else if (name.equals(HYPER.name))
			return HYPER;
		else if (name.equals(BREAKEVEN.name))
			return BREAKEVEN;
		else if (name.equals(LOF.name))
			return LOF;
		else if (name.equals(ETF.name))
			return ETF;
		else if (name.equals(INDEX.name))
			return INDEX;
		else if (name.equals(QDII.name))
			return QDII;
		return STOCK;
	}

}
