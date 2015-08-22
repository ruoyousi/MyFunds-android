package com.ruoyousi.myfunds;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.ruoyousi.common.io.BackupUtil;
import com.ruoyousi.common.lang.DateUtil;
import com.ruoyousi.myfunds.shared.UserFundData;

public class ExportAdapter {

	private static final String DATA_FILE_NAME = "/com.ruoyousi/myfunds/myfunds.dat";
	private static final String LOG_TAG = ExportAdapter.class.getName();

	private ExportAdapter() {
	}

	public static String getImpExpFilePath() throws Exception {
		return DATA_FILE_NAME;
	}

	public static void importData(DbAdapter db) throws Exception {
		String data = BackupUtil.readExternalFile(DATA_FILE_NAME);
		Log.i(LOG_TAG, data);
		db.deleteAllUserFund();
		List<UserFundData> fundList = fromJSON(data);
		for (UserFundData f : fundList) {
			db.insertUserFund(f);
		}
	}

	public static void exportData(DbAdapter db) throws Exception {
		List<UserFundData> fundList = db.loadAllUserFund();
		String data = toJSON(fundList);
		Log.i(LOG_TAG, data);
		BackupUtil.writeExternalFile(DATA_FILE_NAME, data);
	}

	private static List<UserFundData> fromJSON(String data) throws Exception {
		JSONArray json = new JSONArray(data);
		List<UserFundData> fundList = new ArrayList<UserFundData>(json.length());
		for (int i = 0; i < json.length(); i++) {
			JSONObject jsonFund = json.getJSONObject(i);
			UserFundData f = new UserFundData(jsonFund.getString("productId"),
					jsonFund.getString("productName"), DateUtil.parse(jsonFund
							.getString("theDate")),
					jsonFund.getDouble("value"), jsonFund.has("lastDate") ? DateUtil.parse(jsonFund
							.getString("lastDate")) : null, jsonFund
							.getDouble("lastValue"), jsonFund
							.getDouble("quantity"), jsonFund
							.getDouble("principal"));
			fundList.add(f);
		}
		return fundList;
	}

	private static String toJSON(List<UserFundData> fundList) throws Exception {
		JSONArray json = new JSONArray();
		for (UserFundData f : fundList) {
			JSONObject jsonFund = new JSONObject();
			jsonFund.put("productId", f.getProductId());
			jsonFund.put("productName", f.getProductName());
			jsonFund.put("theDate", DateUtil.format(f.getTheDate()));
			jsonFund.put("value", f.getValue());
			jsonFund.put("lastDate", DateUtil.format(f.getLastDate()));
			jsonFund.put("lastValue", f.getLastValue());
			jsonFund.put("quantity", f.getQuantity());
			jsonFund.put("principal", f.getPrincipal());
			json.put(jsonFund);
		}

		return json.toString();
	}

}
