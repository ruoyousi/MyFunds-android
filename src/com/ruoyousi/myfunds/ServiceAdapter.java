package com.ruoyousi.myfunds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.ruoyousi.common.lang.DateUtil;
import com.ruoyousi.common.net.HttpUtil;
import com.ruoyousi.myfunds.shared.FundCompany;
import com.ruoyousi.myfunds.shared.FundProduct;
import com.ruoyousi.myfunds.shared.FundStatus;
import com.ruoyousi.myfunds.shared.FundType;
import com.ruoyousi.myfunds.shared.FundValue;
import com.ruoyousi.myfunds.shared.UserFundData;

public class ServiceAdapter {

	private static final String LOG_TAG = ServiceAdapter.class.getName();

	private ServiceAdapter() {
	}

	private final static String ServerUrl = "http://myfunds.sinaapp.com/data";

	private static String makeRequest(String url) {
		try {
			return HttpUtil.makeRequest(url);
		} catch (Exception e) {
			Log.e(LOG_TAG, "makeRequest(" + url + ") error", e);
			return null;
		}
	}

	public static List<FundCompany> getAllFundCompany() {
		try {
			String url = String.format("%s/companies", ServerUrl);
			String result = makeRequest(url);
			if (result == null) {
				return null;
			}

			JSONArray json = new JSONArray(result);
			List<FundCompany> l = new ArrayList<FundCompany>(json.length());

			for (int i = 0; i < json.length(); i++) {
				JSONObject o = json.getJSONObject(i);
				l.add(jsonToFundCompany(o));
			}

			return l;

		} catch (Exception e) {
			Log.e(LOG_TAG, "getAllFundCompany() error", e);
			return null;
		}
	}

	private static FundCompany jsonToFundCompany(JSONObject o) throws Exception {
		return new FundCompany(o.getLong(FundCompany.COMPANY_ID), o
				.getString(FundCompany.COMPANY_NAME), o
				.getString(FundCompany.WEBSITE), o
				.getString(FundCompany.TELEPHONE));
	}

	public static List<FundProduct> getFundProducts(long companyId) {
		try {
			String url = String.format("%s/companies/%s/products", ServerUrl,
					companyId);
			String result = makeRequest(url);
			if (result == null) {
				return null;
			}

			JSONArray json = new JSONArray(result);
			List<FundProduct> l = new ArrayList<FundProduct>(json.length());

			for (int i = 0; i < json.length(); i++) {
				JSONObject o = json.getJSONObject(i);
				l.add(jsonToFundProduct(o));
			}
			return l;
		} catch (Exception e) {
			Log.e(LOG_TAG, "getFundProducts() error", e);
			return null;
		}

	}

	private static FundProduct jsonToFundProduct(JSONObject o) throws Exception {
		Date createDate = DateUtil.parse(o.getString(FundProduct.CREATE_DATE));
		FundType fundType = FundType.Type(o.getString(FundProduct.FUND_TYPE));
		FundStatus fundStatus = FundStatus.Type(o
				.getString(FundProduct.FUND_STATUS));

		return new FundProduct(o.getString(FundProduct.PRODUCT_ID), o
				.getString(FundProduct.PRODUCT_NAME), createDate, fundType
				.getIndex(), fundStatus.getIndex(), o
				.getLong(FundCompany.COMPANY_ID));
	}

	public static FundValue getFundValue(String productId) {
		try {
			String url = String.format("%s/products/%s/value", ServerUrl,
					productId);
			String result = makeRequest(url);
			if (result == null) {
				return null;
			}
			JSONObject o = new JSONObject(result);

			if (o == null || !o.has(FundProduct.PRODUCT_ID))
				return null;

			return jsonToFundValue(o);

		} catch (Exception e) {
			Log.e(LOG_TAG, "getFundValue() error", e);
			return null;
		}
	}

	private static FundValue jsonToFundValue(JSONObject o) throws Exception {
		Date theDate = DateUtil.parse(o.getString(FundValue.THE_DATE));
		FundValue value = new FundValue(o.getString(FundProduct.PRODUCT_ID), o
				.getString(FundProduct.PRODUCT_NAME), theDate, o
				.getDouble(FundValue.VALUE), o
				.getDouble(FundValue.ACCUMULATION), o
				.getDouble(FundValue.DIVIDEND), o
				.getLong(FundCompany.COMPANY_ID));

		if (o.has(FundValue.LAST_DATE)) {
			value.setLastDate(DateUtil.parse(o.getString(FundValue.LAST_DATE)));
		}

		if (o.has(FundValue.LAST_VALUE)) {
			value.setLastValue(o.getDouble(FundValue.LAST_VALUE));
		}

		return value;
	}

}
