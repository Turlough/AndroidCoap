package ch.ethz.inf.vs.californium.examples.android;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button btnStartStop;
	private boolean running = false;
	private Intent coapServerIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnStartStop = (Button) findViewById(R.id.button1);
		
		coapServerIntent = new Intent(this, CoapService.class);
		
		btnStartStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!running){
					running = true;
					btnStartStop.setText("Stop Server");
					startService(coapServerIntent);
				} else {
					stopService(coapServerIntent);
					btnStartStop.setText("Start Server");
					running = false;
				}
			}
		});
		
		new IpAddressAsyncTask().execute((Void) null);
	}

	
	@Override
	protected void onStop() {
//		if(running){
//			stopService(coapServerIntent);
//		}
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		if (running) {
			stopService(coapServerIntent);
		}
		super.onDestroy();
	}
	
	class IpAddressAsyncTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {

			WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
			String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
			return String.format("%s: %d",ip, CoapService.DEFAULT_PORT);
		}

		@Override
		protected void onPostExecute(String result) {
			
			TextView tvAddress= (TextView)findViewById(R.id.tvAddress);
			tvAddress.setText(result);
			super.onPostExecute(result);
		}
	}

}
