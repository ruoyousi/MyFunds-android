package com.ruoyousi.myfunds;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.ruoyousi.R;
import com.ruoyousi.common.lang.DateUtil;
import com.ruoyousi.common.util.MathUtil;
import com.ruoyousi.myfunds.shared.FieldVerifier;
import com.ruoyousi.myfunds.shared.FormatUtils;
import com.ruoyousi.myfunds.shared.FundProduct;
import com.ruoyousi.myfunds.shared.FundValue;
import com.ruoyousi.myfunds.shared.UserFundData;

public class MainActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener {

	private DbAdapter db;

	private List<UserFundData> mUserData = new ArrayList<UserFundData>();

	private ListView mListView;

	private TextView mTotalTextView;

	private ProgressDialog mProgressDialog = null;

	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	private static final String TAG = MainActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		setResult(RESULT_CANCELED);

		setContentView(R.layout.userfund);

		mListView = (ListView) findViewById(R.id.fundListView);
		mListView.setEmptyView(findViewById(R.id.emptyView));

		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);

		mTotalTextView = (TextView) findViewById(R.id.txtSumTotal);

		db = new DbAdapter(this);

		refresh();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			updateWidget();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}

	private void updateSumTotal() {
		double totalPrincial = 0;
		double totalMoney = 0;
		for (UserFundData f : mUserData) {
			totalPrincial = MathUtil.add(totalPrincial, f.getPrincipal());
			totalMoney = MathUtil.add(totalMoney, f.getTotal());
		}
		mTotalTextView.setText(getString(R.string.userFund_title, FormatUtils
				.formatMoney(totalPrincial), FormatUtils
				.formatMoney(totalMoney), FormatUtils
				.formatEarningRate(MathUtil.growthRate(totalMoney,
						totalPrincial))));
	}

	private QueryTask queryTask = null;

	private class QueryTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... voids) {
			mUserData = db.loadAllUserFund();
			updateFundData();
			return true;
		}

		protected void onPostExecute(Boolean result) {
			mProgressDialog.dismiss();

			updateSumTotal();
			updateWidget();
			updateListView();
			if (mUserData.size() == 0) {
				noFundData();
			}
		}
	};

	private void refresh() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage(getString(R.string.loading));
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setButton(getString(R.string.cancel),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int i) {
							dialog.cancel();
						}
					});
			mProgressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							dialog.dismiss();
							queryTask.cancel(true);
						}
					});
		}
		mProgressDialog.show();

		(queryTask = new QueryTask()).execute();
	}

	private void updateListView() {
		List<Map<String, Object>> data = getListData();
		mListView.setAdapter(new SimpleAdapter(this, data,
				R.layout.userfund_listitem, new String[] {
						FundProduct.PRODUCT_ID, FundProduct.PRODUCT_NAME,
						FundValue.LAST_DATE, FundValue.LAST_VALUE,
						FundValue.THE_DATE, FundValue.VALUE,
						UserFundData.EARNING_RATE, UserFundData.GROWTH,
						UserFundData.QUANTITY, UserFundData.TOTAL,
						UserFundData.PRINCIPAL }, new int[] {
						R.id.txtProductId, R.id.txtProductName,
						R.id.txtLastDate, R.id.txtLastValue, R.id.txtTheDate,
						R.id.txtValue, R.id.txtEarningRate, R.id.txtGrowth,
						R.id.txtQuantity, R.id.txtTotal, R.id.txtPrincipal }));
		mListView.setTextFilterEnabled(true);
		updateSumTotal();
		updateWidget();

		if (data.size() == 0) {
			noFundData();
		}
	}

	private void noFundData() {
		Toast.makeText(this, R.string.noFundData, Toast.LENGTH_LONG).show();
	}

	private void updateFundData() {
		for (UserFundData f : mUserData) {
			updateFundData(f);
			db.updateUserFund(f);
		}

	}

	private void updateWidget() {
		AppWidgetManager gm = AppWidgetManager.getInstance(MainActivity.this);
		if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
			MyWidgetProvider.updateAppWidget(MainActivity.this, gm,
					mAppWidgetId);
		} else {
			int[] appWidgetIds = gm.getAppWidgetIds(new ComponentName(
					MainActivity.this, MyWidgetProvider.class));
			final int N = appWidgetIds.length;
			for (int i = 0; i < N; i++) {
				MyWidgetProvider.updateAppWidget(MainActivity.this, gm,
						appWidgetIds[i]);
			}
		}
	}

	private void updateFundData(UserFundData fundData) {
		try {
			FundValue value = ServiceAdapter.getFundValue(fundData
					.getProductId());
			fundData.setProductName(value.getProductName());
			fundData.setTheDate(value.getTheDate());
			fundData.setValue(value.getValue());
			fundData.setLastDate(value.getLastDate());
			fundData.setLastValue(value.getLastValue());
		} catch (Exception e) {
			// skip
		}
	}

	private List<Map<String, Object>> getListData() {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
		for (UserFundData f : mUserData) {
			addListItem(myData, f);
		}
		return myData;
	}

	private void addListItem(List<Map<String, Object>> data,
			UserFundData fundData) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put(FundProduct.PRODUCT_ID, fundData.getProductId());
		temp.put(FundProduct.PRODUCT_NAME, fundData.getProductName());
		temp
				.put(
						FundValue.LAST_DATE,
						(fundData.getLastDate() == null ? getString(R.string.lastDate_label)
								: DateUtil.format(fundData.getLastDate())));
		temp.put(FundValue.LAST_VALUE, (fundData.getLastValue()) <= 0 ? "-"
				: FormatUtils.formatNetValue(fundData.getLastValue()));
		temp.put(FundValue.THE_DATE, DateUtil.format(fundData.getTheDate()));
		temp.put(FundValue.VALUE, FormatUtils.formatNetValue(fundData
				.getValue()));
		temp.put(UserFundData.EARNING_RATE, FormatUtils
				.formatEarningRate(fundData.getEarningRate()));
		temp.put(UserFundData.GROWTH, FormatUtils.formatEarningRate(fundData
				.getGrowth()));
		temp.put(UserFundData.QUANTITY, FormatUtils.formatQuantity(fundData
				.getQuantity()));
		temp.put(UserFundData.PRINCIPAL, FormatUtils.formatMoney(fundData
				.getPrincipal()));
		temp.put(UserFundData.TOTAL, FormatUtils.formatMoney(fundData
				.getTotal()));
		data.add(temp);
	}

	private UserFundData newFundData(String productId, String productName) {
		return new UserFundData(productId, productName, new Date(), 1,
				new Date(), 1, 1000, 1000);
	}

	private UserFundData getFundDataByProductId(String productId) {
		for (UserFundData f : mUserData) {
			if (f.getProductId().equals(productId)) {
				return f;
			}
		}
		return null;
	}

	private void addFund() {
		startActivityForResult(new Intent(this, SelectCompanyActivity.class), 0);
	}

	public void removeFund(int position) {
		if (position >= 0) {
			UserFundData fund = mUserData.get(position);
			if (db.deleteUserFund(fund.getProductId()) >= 0) {
				mUserData.remove(position);
				updateListView();
			}
		}
	}

	public void editFund(int position) {
		if (position >= 0) {
			inputPrincipalAndQuantity(mUserData.get(position));
		}
	}

	private void inputPrincipalAndQuantityDone(UserFundData fundData,
			double principal, double quantity) {
		fundData.setPrincipal(principal);
		fundData.setQuantity(quantity);
		if (db.updateUserFund(fundData) > 0)
			updateListView();
	}

	private void inputPrincipalAndQuantity(final UserFundData fundData) {
		final EditText textPrincipal = new EditText(this);
		textPrincipal.setImeOptions(EditorInfo.IME_ACTION_DONE);
		textPrincipal.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		textPrincipal.setText(String.valueOf(fundData.getPrincipal()));
		textPrincipal.setHint(R.string.editPrincipal_title);

		final EditText textQuantity = new EditText(this);
		textQuantity.setImeOptions(EditorInfo.IME_ACTION_DONE);
		textQuantity.setInputType(InputType.TYPE_CLASS_NUMBER
				| InputType.TYPE_NUMBER_FLAG_DECIMAL);
		textQuantity.setText(String.valueOf(fundData.getQuantity()));
		textQuantity.setHint(R.string.editQuantity_title);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(textPrincipal);
		layout.addView(textQuantity);
		layout.setPadding(5, 5, 5, 5);

		new AlertDialog.Builder(this).setIcon(R.drawable.ic_button_edit)
				.setTitle(R.string.editFund_title).setView(layout)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String principal = textPrincipal.getText()
										.toString();
								String quantity = textQuantity.getText()
										.toString();
								if (FieldVerifier.isValidMoney(principal)
										&& FieldVerifier
												.isValidQuantity(quantity)) {
									inputPrincipalAndQuantityDone(fundData,
											Double.parseDouble(principal),
											Double.parseDouble(quantity));
								}
							}
						}).setNegativeButton(android.R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			String productId = data.getStringExtra(FundProduct.PRODUCT_ID);
			String productName = data.getStringExtra(FundProduct.PRODUCT_NAME);
			if (productId != null) {
				if (getFundDataByProductId(productId) != null) {
					Toast.makeText(this, R.string.existsFundData,
							Toast.LENGTH_LONG).show();
				} else {
					UserFundData fund = newFundData(productId, productName);
					if (db.insertUserFund(fund) >= 0) {
						mUserData.add(fund);
						refresh();
					}
				}
			}
			break;
		case RESULT_CANCELED:
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.menuitem_add) {
			addFund();
		} else if (itemId == R.id.menuitem_refresh) {
			refresh();
		} else if (itemId == R.id.menuitem_import) {
			importData();
		} else if (itemId == R.id.menuitem_export) {
			exportData();
		} else if (itemId == R.id.menuitem_about) {
			startActivity(new Intent().setClass(this, AboutActivity.class));
		}

		return super.onOptionsItemSelected(item);
	}

	private void importData() {
		try {
			ExportAdapter.importData(db);
			refresh();
			Toast
					.makeText(this, R.string.ImportData_success,
							Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.e(TAG, "import data error", e);
			Toast.makeText(this, R.string.ImportData_fail, Toast.LENGTH_LONG)
					.show();
		}
	}

	private void exportData() {
		try {
			ExportAdapter.exportData(db);
			Toast.makeText(
					this,
					getString(R.string.ExportData_success, ExportAdapter
							.getImpExpFilePath()), Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.e(TAG, "export data error", e);
			Toast.makeText(this, R.string.ExportData_fail, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		editFund(position);
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view,
			final int position, long id) {
		new AlertDialog.Builder(this).setMessage(R.string.askBeforeRemove)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								removeFund(position);
							}
						}).setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
		return true;
	}

}