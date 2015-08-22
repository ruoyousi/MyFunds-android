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

public class SelectProductActivity extends ListActivity {

	private ProgressDialog mProgressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if (intent.hasExtra(FundCompany.COMPANY_ID)) {
			final long companyId = intent.getLongExtra(FundCompany.COMPANY_ID,
					-1);
			if (companyId > 0) {
				query(companyId);
			}
		}
	}

	private QueryTask mTask = null;

	private class QueryTask extends AsyncTask<Long, Void, List<FundProduct>> {
		@Override
		protected List<FundProduct> doInBackground(Long... companyIds) {
			return makeRequest(companyIds[0]);
		}

		protected void onPostExecute(List<FundProduct> result) {
			mProgressDialog.dismiss();
			updateListView(result);
		}
	};

	private void query(long companyId) {
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

		(mTask = new QueryTask()).execute(companyId);
	}

	private void updateListView(List<FundProduct> fundData) {
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

	private List<Map<String, Object>> getListData(List<FundProduct> fundData) {
		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();
		if (fundData != null) {
			for (FundProduct product : fundData) {
				addListItem(myData, product);
			}
		}
		return myData;
	}

	private void addListItem(List<Map<String, Object>> data,
			FundProduct fundProduct) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("text1", fundProduct.getProductId() + " "
				+ fundProduct.getProductName());
		temp.put(FundProduct.OBJECT, fundProduct);
		data.add(temp);
	}

	private List<FundProduct> makeRequest(long companyId) {
		return ServiceAdapter.getFundProducts(companyId);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);
		FundProduct fundProduct = (FundProduct) map.get(FundProduct.OBJECT);

		Intent result = new Intent();
		result.putExtra(FundProduct.PRODUCT_ID, fundProduct.getProductId());
		result.putExtra(FundProduct.PRODUCT_NAME, fundProduct.getProductName());

		setResult(RESULT_OK, result);
		finish();
	}

}