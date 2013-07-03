package com.example.smsinbox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.TextView;

import com.example.smsinbox.R;

public class Feed extends Activity {

	Button saveCmd;
	Button ClearCmd;
    TextView ClickCmd;
	EditText input;

	ListView lv;
	ArrayAdapter<String> arrayAdapter;
	public static final String BANK_ID = "bankIds";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed);

		feed();

		saveCmd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String check = input.getText().toString();
				SharedPreferences prefs = getSharedPreferences("BANK_IDS",
						Context.MODE_PRIVATE);

				Editor editor = prefs.edit();

				// Retrieve the values
				Set<String> set = prefs.getStringSet(BANK_ID, null);

				if(set == null)
					set = new HashSet<String>();
								
				
				if (set != null && !set.contains(check)) {
					your_array_list.add(check);
					Iterator<String> iterator = set.iterator();
					// New set
					Set<String> newSet = new HashSet<String>();
					while (iterator.hasNext()) {
						newSet.add(iterator.next());
					}
					newSet.add(check);

					editor.putStringSet(BANK_ID, newSet);
					editor.commit();
				}
				

				arrayAdapter.notifyDataSetInvalidated();

			}
		});

		ClearCmd.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("null")
			@Override
			public void onClick(View arg0) {
				String check = input.getText().toString();
				SharedPreferences prefs = getSharedPreferences("BANK_IDS",
						Context.MODE_PRIVATE);

				Editor editor = prefs.edit();

				// Retrieve the values
				Set<String> set = prefs.getStringSet(BANK_ID, null);

				// Set the values
				if (set != null && set.contains(check)) {
					your_array_list.remove(check);
					Iterator<String> iterator = set.iterator();
					// New set
					Set<String> newSet = new HashSet<String>();
					while (iterator.hasNext()) {
						newSet.add(iterator.next());
					}
					newSet.remove(check);

					editor.putStringSet(BANK_ID, newSet);
					editor.commit();
				}
				else {
					
					
					Toast.makeText(Feed.this, "Touch on the number you want to delete and then click clear button", Toast.LENGTH_LONG).show();					
					
				}

				arrayAdapter.notifyDataSetInvalidated();
			}

		});
		
		
		


	}

	ArrayList<String> your_array_list = new ArrayList<String>();

	private void feed() {
		saveCmd = (Button) findViewById(R.id.bResutls);
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.feedrow,R.id.tvrow, your_array_list);
		input = (EditText) findViewById(R.id.etNames);
		ClickCmd = (TextView) findViewById(R.id.tvrow);
		lv = (ListView) findViewById(R.id.tvcontact);

		lv.setAdapter(arrayAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String item = your_array_list.get(position);
				Log.d("ITEM", item);
				input.setText(item);
			}
		});

		SharedPreferences prefs = getSharedPreferences("BANK_IDS",
				Context.MODE_PRIVATE);
		Set<String> ids = prefs.getStringSet(BANK_ID, null);
		if (ids != null) {
			Iterator<String> iterator = ids.iterator();
			while (iterator.hasNext()) {
				your_array_list.add(iterator.next());
			}
		}

		ClearCmd = (Button) findViewById(R.id.bClear);
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.feedrow,
				R.id.tvrow, your_array_list);
		input = (EditText) findViewById(R.id.etNames);
		ClickCmd = (TextView) findViewById(R.id.tvrow);
		lv = (ListView) findViewById(R.id.tvcontact);
		lv.setAdapter(arrayAdapter);

	}

}
