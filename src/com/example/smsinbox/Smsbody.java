package com.example.smsinbox;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class Smsbody extends Activity {
	
	SimpleCursorAdapter adapter;
	SimpleDateFormat sdf ;
	public void onCreate(Bundle SavedInstanceState){
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.smsbodydisplay);
		Calendar calendar= Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone("GMT+0530");
		calendar.setTimeZone(tz);
	    sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
	    Uri uri = Uri.parse("content://sms/inbox");
	    String selcetedsender = getIntent().getStringExtra("sender");
	    
	    
	    
	    ((TextView)findViewById(R.id.bnumbdisplay)).setText(selcetedsender);
	    Cursor c = getContentResolver().query(uri, null, "address LIKE '%"+selcetedsender+"%'" ,null,null);
	    String[] from = new String[]{"body","date"};
	    int[] to = new int[]{R.id.bodymessage,R.id.bodydate};
	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.bodydisplay,c ,from,to,0);
	    adapter.setViewBinder(new ViewBinder() {
			
			@Override
			public boolean setViewValue(View arg0, Cursor arg1, int arg2) {
				if(arg0.getId() == R.id.bodydate){
					String date =  arg1.getString(arg2);
					Long timestamp = Long.parseLong(date);
					String dateString = sdf.format(new Date(timestamp));
					((TextView)arg0).setText(dateString);
					return true;
				}else
					return false;
			}
		});
	    ListView listView =(ListView)findViewById(R.id.bsms);
	    listView.setAdapter(adapter);


}
}