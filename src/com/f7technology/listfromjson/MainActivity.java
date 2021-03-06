package com.f7technology.listfromjson;

import java.util.ArrayList;
import java.util.HashMap;

import library.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ListView list;
	TextView ver;
	TextView name;
	TextView api;
	Button Btngetdata;
	ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
	private static String url = "http://api.learn2crack.com/android/jsonos/";
	private static final String TAG_OS = "android";
	private static final String TAG_VER = "ver";
	private static final String TAG_NAME = "name";
	private static final String TAG_API = "api";
	JSONArray android = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		oslist = new ArrayList<HashMap<String, String>>();
		Btngetdata = (Button) findViewById(R.id.getdata);
		Btngetdata.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new JSONParse().execute();
			}
		});
	}
	
	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ver = (TextView) findViewById(R.id.vers);
			name = (TextView) findViewById(R.id.name);
			api = (TextView) findViewById(R.id.api);
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Getting data...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
		
		@Override
		protected JSONObject doInBackground (String...args) {
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url);
			return json;
		}
		
		@Override
		protected void onPostExecute(JSONObject json) {
			pDialog.dismiss();
			try {
				android = json.getJSONArray(TAG_OS);
				for (int i = 0; i < android.length(); i++) {
					JSONObject c = android.getJSONObject(i);
					
					String ver = c.getString(TAG_VER);
					String name = c.getString(TAG_NAME);
					String api = c.getString(TAG_API);
					
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(TAG_VER, ver);
					map.put(TAG_NAME, name);
					map.put(TAG_API, api);
					oslist.add(map);
					list=(ListView) findViewById(R.id.list);
					ListAdapter adapter = new SimpleAdapter(MainActivity.this, oslist, R.layout.list_row, 
															new String[] {TAG_VER, TAG_NAME, TAG_API}, 
															new int[] {R.id.vers, R.id.name, R.id.api});
					list.setAdapter(adapter);
					list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Toast.makeText(MainActivity.this, oslist.get(+position).get("name"), Toast.LENGTH_SHORT).show();
						}
					});
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}