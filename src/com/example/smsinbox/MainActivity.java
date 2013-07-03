package com.example.smsinbox;

import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
//import android.view.Menu;
//import com.example.smsinbox.data.SMSData;


@SuppressLint("NewApi") public class MainActivity extends Activity {

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodydisplay);
		
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor c= getContentResolver().query(uri, null, null ,null,null);
		String[] columns= c.getColumnNames();
		c.moveToFirst();
		String date =  c.getString(c.getColumnIndex("date"));
		Long timestamp = Long.parseLong(date);    
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		calendar.setTimeZone(TimeZone.getTimeZone("IST"));
		Date finaldate = calendar.getTime();
		String smsDate = finaldate.toString();
		Log.d(MainActivity.class.getName(), smsDate);
		
		
		
		for (int loop_index= 0; loop_index<columns.length;loop_index++){
			Log.d("Nikhila","Column name "+loop_index+": "+columns[loop_index] );

		}
		c= getContentResolver().query(uri, null, null ,null,null);
	while(c.moveToNext()){
		
		Log.d("Nikhila", "Sms thread_id : "+c.getString(1) );
		Log.d("Nikhila", "Sms address : "+c.getString(2) );
		Log.d("Nikhila", "Sms person : "+c.getString(3) );
		Log.d("Nikhila", "Sms date : "+c.getString(4) );
		Log.d("Nikhila", "Sms date_sent : "+c.getString(5) );
		Log.d("Nikhila", "Sms protocol : "+c.getString(6) );
		Log.d("Nikhila", "Sms read : "+c.getString(7) );
		Log.d("Nikhila", "Sms status : "+c.getString(8) );
		Log.d("Nikhila", "Sms type : "+c.getString(9) );
		Log.d("Nikhila", "Sms  reply_path_present : "+c.getString(10) );
		Log.d("Nikhila", "Sms subject : "+c.getString(11) );
		Log.d("Nikhila", "Sms body : "+c.getString(12) );
		Log.d("Nikhila", "Sms service_center : "+c.getString(13) );
		Log.d("Nikhila", "Sms  locked : "+c.getString(14) );
		Log.d("Nikhila", "Sms error_code : "+c.getString(15) );
		Log.d("Nikhila", "Sms seen : "+c.getString(16) );
		
		
		
			
		}
	
	
	
	/*// get the supported ids for GMT-08:00 (Pacific Standard Time)
	 String[] ids = TimeZone.getAvailableIDs(-8 * 60 * 60 * 1000);
	 // if no ids were returned, something is wrong. get out.
	 if (ids.length == 0)
	     System.exit(0);

	  // begin output
	 System.out.println("Current Time");

	 // create a Pacific Standard Time time zone
	 SimpleTimeZone pdt = new SimpleTimeZone(-8 * 60 * 60 * 1000, ids[0]);

	 // set up rules for daylight savings time
	 pdt.setStartRule(Calendar.APRIL, 1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);
	 pdt.setEndRule(Calendar.OCTOBER, -1, Calendar.SUNDAY, 2 * 60 * 60 * 1000);

	 // create a GregorianCalendar with the Pacific Daylight time zone
	 // and the current date and time
	 
	 Calendar calendar = new GregorianCalendar();
	 Date trialTime = new Date();
	 calendar.setTime(trialTime);

	 // print out a bunch of interesting things
	 System.out.println("ERA: " + calendar.get(Calendar.ERA));
	 System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
	 System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
	 System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
	 System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
	 System.out.println("DATE: " + calendar.get(Calendar.DATE));
	 System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
	 System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
	 System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
	 System.out.println("DAY_OF_WEEK_IN_MONTH: "
	                    + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
	 System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
	 System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
	 System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
	 System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
	 System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
	 System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
	 System.out.println("ZONE_OFFSET: "
	                    + (calendar.get(Calendar.ZONE_OFFSET)/(60*60*1000)));
	 System.out.println("DST_OFFSET: "
	                    + (calendar.get(Calendar.DST_OFFSET)/(60*60*1000)));

	 System.out.println("Current Time, with hour reset to 3");
	 calendar.clear(Calendar.HOUR_OF_DAY); // so doesn't override
	 calendar.set(Calendar.HOUR, 3);
	 System.out.println("ERA: " + calendar.get(Calendar.ERA));
	 System.out.println("YEAR: " + calendar.get(Calendar.YEAR));
	 System.out.println("MONTH: " + calendar.get(Calendar.MONTH));
	 System.out.println("WEEK_OF_YEAR: " + calendar.get(Calendar.WEEK_OF_YEAR));
	 System.out.println("WEEK_OF_MONTH: " + calendar.get(Calendar.WEEK_OF_MONTH));
	 System.out.println("DATE: " + calendar.get(Calendar.DATE));
	 System.out.println("DAY_OF_MONTH: " + calendar.get(Calendar.DAY_OF_MONTH));
	 System.out.println("DAY_OF_YEAR: " + calendar.get(Calendar.DAY_OF_YEAR));
	 System.out.println("DAY_OF_WEEK: " + calendar.get(Calendar.DAY_OF_WEEK));
	 System.out.println("DAY_OF_WEEK_IN_MONTH: "
	                    + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
	 System.out.println("AM_PM: " + calendar.get(Calendar.AM_PM));
	 System.out.println("HOUR: " + calendar.get(Calendar.HOUR));
	 System.out.println("HOUR_OF_DAY: " + calendar.get(Calendar.HOUR_OF_DAY));
	 System.out.println("MINUTE: " + calendar.get(Calendar.MINUTE));
	 System.out.println("SECOND: " + calendar.get(Calendar.SECOND));
	 System.out.println("MILLISECOND: " + calendar.get(Calendar.MILLISECOND));
	 System.out.println("ZONE_OFFSET: "
	        + (calendar.get(Calendar.ZONE_OFFSET)/(60*60*1000))); // in hours
	 System.out.println("DST_OFFSET: "
	        + (calendar.get(Calendar.DST_OFFSET)/(60*60*1000))); // in hours       
*/
	
	 
	}
    
    

	
		
    


    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
     //   return true;
   // }
    
}
