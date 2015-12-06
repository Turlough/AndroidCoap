package com.example.andoidcoap;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Prefs {
	private SharedPreferences prefs;
	private Editor editor;

	private String BASE_ENDPOINT;

	public Prefs(Context context) {
		prefs = context.getSharedPreferences("coap", Context.MODE_PRIVATE);
		editor = prefs.edit();
	}

	public void setBaseEndpoint(String value) {
		editor.putString(BASE_ENDPOINT, value).commit();
	}
	
	public String getBaseEndpoint(){
		return prefs.getString(BASE_ENDPOINT, "coap://192.168.1.3:5683");
	}
}
