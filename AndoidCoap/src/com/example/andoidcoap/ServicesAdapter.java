package com.example.andoidcoap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.andoidcoap.DiscoveryResponseParser.EndpointDescription;

public class ServicesAdapter extends ArrayAdapter<EndpointDescription> {
	
	private final Context context;
	private final EndpointDescription[] values;

	public ServicesAdapter(Context context, EndpointDescription[] values) {
		
		super(context, R.layout.services_item_layout, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.services_item_layout, parent,
				false);

		TextView textView = (TextView) rowView.findViewById(R.id.tvEndpoint);
		textView.setText(values[position].endpoint);
		
		textView = (TextView) rowView.findViewById(R.id.tvTitle);
		textView.setText(values[position].title);

		return rowView;
	}
}
