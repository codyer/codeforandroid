package com.pao123.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import android.util.Log;

@SuppressWarnings("deprecation")
public class PaoSdk {
	private static final String TAG = "PaoSdk";
	private String uri;
	private List<NameValuePair> params;
	private List<NameValuePair> files;
	private int code;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setParameter(String name, String val) {
		params.add(new BasicNameValuePair(name, val));
	}

	public void setFile(String name, String val) {
		files.add(new BasicNameValuePair(name, val));
	}

	public int getCode() {
		return code;
	}

	public PaoSdk(String uri) {
		this.uri = uri;
		this.params = new ArrayList<NameValuePair>();
		this.files = new ArrayList<NameValuePair>();
	}

	@SuppressWarnings({ "unused" })
	private String post() {
		try {
			String server = Constants.HTTP_ADDRESS;
			if (Constants.HTTP_PORT != "80") {
				server += ":" + Constants.HTTP_PORT;
			}

			String url = String.format("http://%s%s", server, this.uri);
			Log.d(TAG, "POST: " + url);

			HttpPost post = new HttpPost(url);
			String reqInfo;
			if (files.isEmpty()) {
				UrlEncodedFormEntity ue = new UrlEncodedFormEntity(params,
						HTTP.UTF_8);
				post.setEntity(ue);
				reqInfo = url + " " + EntityUtils.toString(ue, "utf-8");
			} else {
				MultipartEntity mpe = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				for (NameValuePair param : params) {
					mpe.addPart(param.getName(),
							new StringBody(param.getValue()));
				}
				for (NameValuePair file : files) {
					mpe.addPart(file.getName(),
							new FileBody(new File(file.getValue())));
				}
				post.setEntity(mpe);
				reqInfo = url + " ---<MultipartEntity>---";
			}
			Log.d(TAG, "POST: " + reqInfo);
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					Constants.HTTP_CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters,
					Constants.HTTP_SOCKET_TIMEOUT);
			HttpResponse response = new DefaultHttpClient(httpParameters)
					.execute(post);
			this.code = response.getStatusLine().getStatusCode();
			if (this.code == 200) {
				String s = EntityUtils.toString(response.getEntity());
				Log.d(TAG, "API " + this.uri + " Success: " + s);
				return s;
			} else {
				Log.d(TAG, "API " + this.uri + " Error: " + this.code);
				return null;
			}
		} catch (ClientProtocolException e) {
			Log.d(TAG, "API " + this.uri + " ClientProtocolException: " + e);
			return null;
		} catch (IOException e) {
			Log.d(TAG, "API " + this.uri + " IOException: " + e);
			return null;
		} catch (IllegalArgumentException e) {
			Log.d(TAG, "API " + this.uri + " IllegalArgumentException: " + e);
			return null;
		}
	}

	public <T> T postObject(Class<T> clazz) {
		String s = post();
//		Log.e("this.uri", "this.uri="+this.uri+"\nreturn="+s);
		JSONObject jsonObject = JSONObject.parseObject(s);
		if (jsonObject != null && jsonObject.containsKey("id")) {
			int id = jsonObject.getInteger("id");
			if (id == 0) {
				return null;
			}
		}else {
			return null;
		}
		if (s == null)
			return null;
		else
			return JSON.parseObject(s, clazz);
	}

	public <T> List<T> postArray(Class<T> clazz) {
		String s = post();
		Log.e("this.uri", "this.uri="+this.uri+"\nreturn="+s);
		if (s == null)
			return null;
		else
			return JSON.parseArray(s, clazz);
	}

}