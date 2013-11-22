package net.shoppier.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JSONParser extends AsyncTask<String, Void, JSONObject> {

	static InputStream is = null;
	static JSONObject jObj = null;
	static JSONArray jObjArry = null;
	static String json = "";
	private List<NameValuePair> params;


	// constructor
	public JSONParser(List<NameValuePair> paramsInput) {
		
			this.params = paramsInput;
	}
	
	public JSONParser(){
		
	}

	@Override
	protected JSONObject doInBackground(String... url) {

		JSONObject toReturn = getJSONFromUrl(url[0]);
		return toReturn;

	}

	public JSONObject getJSONFromUrl(String url) {

		// Making HTTP request
		try {
			// defaultHttpClient
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = null;

			// Url Encoding the POST parameters
			if(params != null){
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));
				 httpResponse = httpClient.execute(httpPost);
			}else{
				HttpGet httpget = new HttpGet(url);
				httpResponse = httpClient.execute(httpget);
			}
			
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				//sb.append(line + "\n");
				sb.append(line);
			}
			is.close();
			json = sb.toString();
			Log.e("JSON Output", json);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		// try to parse the string to a JSON object
		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			
		}

		// return JSON String
		return jObj;

	}
	

}
