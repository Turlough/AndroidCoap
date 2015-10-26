package com.example.andoidcoap;

import java.net.URI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import ch.ethz.inf.vs.californium.coap.GETRequest;
import ch.ethz.inf.vs.californium.coap.Request;
import ch.ethz.inf.vs.californium.coap.Response;

import com.example.andoidcoap.DiscoveryResponseParser.EndpointDescription;

public class MainActivity extends Activity {

	Button btnGet;
	TextView tvResponse;
	private EditText etEndpoint;
	ListView lvServices;
	BroadcastReceiver receiver;
	Intent clientIntent;
	private String endpoint = "coap://192.168.1.3:5683";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		clientIntent = new Intent(this, CoapClientService.class);

		tvResponse = (TextView) findViewById(R.id.tvResponse);
		etEndpoint = (EditText) findViewById(R.id.etEndpoint);
		etEndpoint.setText(endpoint);
		etEndpoint
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							
							endpoint = etEndpoint.getText().toString();
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		                    imm.hideSoftInputFromWindow(etEndpoint.getWindowToken(), 0);
		    				clientIntent.putExtra(CoapClientService.ENDPOINT, endpoint);
		    				startService(clientIntent);
		    				
							return true;
							
						}
						return false;
					}
				});

		lvServices = (ListView) findViewById(R.id.lvServices);

		btnGet = (Button) findViewById(R.id.btnGet);
		btnGet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clientIntent.putExtra(CoapClientService.ENDPOINT, endpoint);
				startService(clientIntent);
			}
		});

	}

	@Override
	public void onStart() {

		IntentFilter filter = new IntentFilter();
		filter.addAction(CoapClientService.ACTION.ON_DISCOVERY.toString());

		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				String response = intent.getExtras().getString(
						CoapClientService.server_response);

				tvResponse.setText(response);

				DiscoveryResponseParser parser = new DiscoveryResponseParser(
						response);

				final ServicesAdapter adapter = new ServicesAdapter(
						MainActivity.this, parser.getDescriptions());
				lvServices.setAdapter(adapter);

				lvServices
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									final View view, int position, long id) {

								final EndpointDescription item = (EndpointDescription) parent
										.getItemAtPosition(position);

								String endpoint = CoapClientService.ENDPOINT
										+ item.endpoint;
								new CoapAsyncTask().execute(endpoint);

							}

						});

			}
		};

		registerReceiver(receiver, filter);

		clientIntent.putExtra(CoapClientService.ENDPOINT, endpoint);
		startService(clientIntent);
		super.onStart();
	}

	@Override
	public void onStop() {

		if (receiver != null)
			unregisterReceiver(receiver);

		stopService(clientIntent);
		super.onStop();

	}

	class CoapAsyncTask extends AsyncTask<String, Object, String> {

		URI uri;

		@Override
		protected String doInBackground(String... params) {
			try {

				uri = new URI(params[0]);

				Request request = new GETRequest();
				request.setURI(uri);
				request.enableResponseQueue(true);
				request.execute();

				Response response = request.receiveResponse();
				String s = response.getPayloadString();

				if (!TextUtils.isEmpty(s)) {
					return s;
				} else {
					return "No response received.";
				}

			} catch (Exception e) {
				return e.getMessage();
			}

		}

		@Override
		protected void onPostExecute(String result) {

			tvResponse.setText(result);

		}

		void showResult(Exception e) {
			tvResponse.setText(e.getMessage());
		}

	}

}
