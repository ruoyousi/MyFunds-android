package com.ruoyousi.myfunds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ruoyousi.R;
import com.ruoyousi.myfunds.shared.FundCompany;
import com.ruoyousi.myfunds.shared.FundProduct;

public class SelectCompanyActivity extends ListActivity {

	private ProgressDialog mProgressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		query();
	}

	private QueryTask mTask = null;

	private class QueryTask extends AsyncTask<Void, Void, List<FundCompany>> {
		@Override
		protected List<FundCompany> doInBackground(Void... voids) {
			return makeRequest();
		}

		protected void onPostExecute(List<FundCompany> result) {
			mProgressDialog.dismiss();
			updateListView(result);
		}
	};

	private void query() {
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
							mTask.cancel(true);
						}
					});
		}
		mProgressDialog.show();

		(mTask = new QueryTask()).execute();
	}

	private void updateListView(List<FundCompany> fundData) {
		if (fundData == null) {
			Toast.makeText(this, R.string.connectServerFailed,
					Toast.LENGTH_LONG).show();
		} else {
			setListAdapter(new SimpleAdapter(this, getListData(fundData),
					android.R.layout.simple_list_item_1,
					new String[] { "text1" }, new int[] { android.R.id.text1 }));
			getListView().setTextFilterEnabled(true);
		}
	}

	private List<Map<String, Object>> getListData(List<FundCompany> fundData) {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
		if (fundData != null) {
			for (FundCompany company : fundData) {
				addListItem(myData, company);
			}
		}
		return myData;
	}

	private void addListItem(List<Map<String, Object>> data,
			FundCompany fundCompany) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("text1", fundCompany.getCompanyName());
		temp.put(FundCompany.OBJECT, fundCompany);
		data.add(temp);
	}

	private List<FundCompany> makeRequest() {
		return ServiceAdapter.getAllFundCompany();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			String productId = data.getStringExtra(FundProduct.PRODUCT_ID);
			String productName = data.getStringExtra(FundProduct.PRODUCT_NAME);
			Intent result = new Intent();
			result.putExtra(FundProduct.PRODUCT_ID, productId);
			result.putExtra(FundProduct.PRODUCT_NAME, productName);
			setResult(RESULT_OK, result);
			finish();
			break;
		case RESULT_CANCELED:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		FundCompany company = (FundCompany) map.get(FundCompany.OBJECT);

		Intent request = new Intent();
		request.setClass(this, SelectProductActivity.class);
		request.putExtra(FundCompany.COMPANY_ID, company.getCompanyId());
		startActivityForResult(request, 0);
	}

}