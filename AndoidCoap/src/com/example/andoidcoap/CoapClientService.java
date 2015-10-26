package com.example.andoidcoap;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;
import ch.ethz.inf.vs.californium.coap.ResponseHandler;

public class CoapClientService extends Service {

	// exit codes for runtime errors
	public static final int ERR_INIT_FAILED = 1;
	public static final int DEFAULT_PORT = 5683;
	public static final String EXTRA_PORT = "ch.ethz.inf.vs.californium.extras.PORT";
	public final static String ENDPOINT = "endpoint";
	public final static String server_response = "server_response";
	
	private String endpoint;

	public static enum ACTION {
		ON_DISCOVERY
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		endpoint = intent.getExtras().getString(ENDPOINT);

		discover();

		return super.onStartCommand(intent, flags, startId);
	}

	private void discover() {

		final Request request = new GETRequest();
		request.setURI(endpoint + "/.well-known/core");

		request.registerResponseHandler(new ResponseHandler() {
			public void handleResponse(Response response) {
				sendDiscoveryBroadcast(response.getPayloadString());
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					request.execute();
				} catch (IOException e) {
					sendDiscoveryBroadcast(e.getMessage());
				}

			}

		}).start();

	}

	private void sendDiscoveryBroadcast(String response) {

		Intent intent = new Intent();
		intent.setAction(ACTION.ON_DISCOVERY.toString());
		intent.putExtra(server_response, response);
		sendBroadcast(intent);

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

}
