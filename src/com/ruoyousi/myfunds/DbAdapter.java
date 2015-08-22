package com.ruoyousi.myfunds;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.ruoyousi.common.db.DatabaseSession;
import com.ruoyousi.common.db.Row;
import com.ruoyousi.common.db.RowHandler;
import com.ruoyousi.common.lang.DateUtil;
import com.ruoyousi.common.util.MathUtil;
import com.ruoyousi.myfunds.shared.UserFundsSummary;
import com.ruoyousi.myfunds.shared.UserFundData;

public class DbAdapter {

	protected static final String LOG_TAG = DbAdapter.class.getName();

	private static final int DB_VERSION = 2;
	private static final String DB_NAME = "myfund.db";
	private static final String[] TABLE_NAMES = { "t_fund", "t_favorite" };

	private static final String[] CREATE_TABLES = {
			"CREATE TABLE t_fund (" + "productId TEXT" + ",productName TEXT"
					+ ",lastDate TEXT" + ",lastValue FLOAT" + ",theDate TEXT"
					+ ",value FLOAT" + ",quantity FLOAT" + ",principal FLOAT"
					+ ");",
			"CREATE TABLE t_favorite (" + "productId TEXT"
					+ ",productName TEXT" + ",lastDate TEXT"
					+ ",lastValue FLOAT" + ",theDate TEXT" + ",value FLOAT"
					+ ");" };

	private DatabaseSession session = null;

	public DbAdapter(Context _context) {

		session = new DatabaseSession(_context, DB_NAME, DB_VERSION,
				TABLE_NAMES, CREATE_TABLES);

		Log.v(LOG_TAG, "db path=" + session.getDBPath());
	}

	public void close() {
		if (session != null)
			session.close();
	}

	public int userFundCount() {
		return session.queryScalar("SELECT count(*) FROM t_fund", null, 0);
	}

	public long insertUserFund(UserFundData fund) {
		try {
			ContentValues values = new ContentValues();
			values.put("productId", fund.getProductId());
			values.put("productName", fund.getProductName());
			values.put("lastDate", DateUtil.format(fund.getLastDate()));
			values.put("lastValue", fund.getLastValue());
			values.put("theDate", DateUtil.format(fund.getTheDate()));
			values.put("value", fund.getValue());
			values.put("quantity", fund.getQuantity());
			values.put("principal", fund.getPrincipal());
			long rowId = session.insert("t_fund", values);
			Log.v(LOG_TAG, "insert Table t_fund ok");
			return rowId;
		} catch (Exception e) {
			Log.e(LOG_TAG, "insert Table t_fund error: " + fund);
			return -1;
		}
	}

	public int updateUserFund(UserFundData fund) {
		try {
			ContentValues values = new ContentValues();
			values.put("productName", fund.getProductName());
			values.put("lastDate", DateUtil.format(fund.getLastDate()));
			values.put("lastValue", fund.getLastValue());
			values.put("theDate", DateUtil.format(fund.getTheDate()));
			values.put("value", fund.getValue());
			values.put("quantity", fund.getQuantity());
			values.put("principal", fund.getPrincipal());
			return session.update("t_fund", values, "productId=?",
					new String[] { fund.getProductId() });
		} catch (Exception ex) {
			Log.e(LOG_TAG, "update data of Table t_user error: " + fund);
			return -1;
		}
	}

	public int deleteUserFund(String productId) {
		try {
			return session.delete("t_fund", "productId=?",
					new String[] { productId });
		} catch (Exception ex) {
			Log.e(LOG_TAG, "delete data from Table t_user error ,productId: "
					+ productId);
			return -1;
		}
	}

	public int deleteAllUserFund() {
		try {
			return session.delete("t_fund", null, null);
		} catch (Exception ex) {
			Log.e(LOG_TAG, "delete data from Table t_user error.");
			return -1;
		}
	}

	public List<UserFundData> loadAllUserFund() {
		final List<UserFundData> l = new ArrayList<UserFundData>();

		session.queryCursor("t_fund", new String[] { "productId",
				"productName", "theDate", "value", "lastDate", "lastValue",
				"quantity", "principal" }, null, null, null, null, null,
				new RowHandler() {
					public void handle(Row row) {
						l.add(new UserFundData(row.getString(0), row
								.getString(1),

						DateUtil.parse(row.getString(2)), row.getDouble(3),
								DateUtil.parse(row.getString(4)), row
										.getDouble(5), row.getDouble(6), row
										.getDouble(7)));
					}
				});

		return l;
	}

	public static UserFundsSummary sumUserFund(List<UserFundData> funds) {
		double totalPrincial = 0;
		double totalMoney = 0;
		for (UserFundData f : funds) {
			totalPrincial = MathUtil.add(totalPrincial, f.getPrincipal());
			totalMoney = MathUtil.add(totalMoney, f.getTotal());
		}

		return new UserFundsSummary(totalPrincial, totalMoney);
	}

}