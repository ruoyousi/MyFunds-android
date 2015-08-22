package com.ruoyousi.common.net;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	
	private HttpUtil() {
	}
	
	public static String makeRequest(String url) throws Exception {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
		HttpConnectionParams.setSoTimeout(params, 10 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 40 * 1024);

		HttpGet request = new HttpGet(url);
		HttpResponse response = new DefaultHttpClient(params).execute(request);

		if (response.getStatusLine().getStatusCode() != 200)
			return null;
		return EntityUtils.toString(response.getEntity());
	}
}
