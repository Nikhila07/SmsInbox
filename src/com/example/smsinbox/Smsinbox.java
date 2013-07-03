package com.example.smsinbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.AdapterView.OnItemSelectedListener;
/*import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;*/

public class Smsinbox extends Activity {
	SimpleCursorAdapter adapter;
	Button settings;
	Button upload;
	HttpClient client;
	JSONObject json;
	//NEW VARIABLES
	JSONObject validator = null;
	String user = null;
	
	String serverurl = "http://50.116.12.205/";
//	/postData("http://10.0.2.2/upload.php", json);
	
	//END OF NEW VARIABLES
	protected Object mActionMode;
	  public int selectedItem = -1;
	  private View viewContainer;
	

	public void onCreate(Bundle SavedInstanceState) {
		super.onCreate(SavedInstanceState);
		setContentView(R.layout.list_display);
		/*Intent menu = new Intent(getApplicationContext(), Menu.class);
		startActivity(menu);*/

		settings = (Button) findViewById(R.id.bsettings);
		
		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent feed = new Intent(getApplicationContext(), Feed.class);
				startActivity(feed);
				Toast.makeText(Smsinbox.this, "Here you can save the username or number and filter your messages", Toast.LENGTH_LONG).show();

			}

		});
		
		 upload = (Button) findViewById(R.id.bupload);
		    
		    upload.setOnClickListener(new OnClickListener() {
	            @Override
				public void onClick(View v) {
	            	getUserNumber();
	            	if (user ==null){
	            		Toast.makeText(getApplicationContext(),"Abort Sync :User Number unavailable", Toast.LENGTH_LONG);
	            	}
	            	else{
	            		getValidator();
	            	}
				} 
		 });


	}
	public void getUserNumber(){
		SharedPreferences prefs = getSharedPreferences("BANK_IDS",
				Context.MODE_PRIVATE);
		String usernumber = prefs.getString("USER_NUMBER", "");
		if(usernumber.length()<=0){
			final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.user_number);
            dialog.setCancelable(false);
            dialog.setTitle("Enter your contact number");
            final EditText userNo = (EditText) dialog.findViewById(R.id.userno);
            Button button = (Button) dialog.findViewById(R.id.bsaveno);
            button.setOnClickListener(new OnClickListener() {
            @Override
                public void onClick(View v) {
            		String userNumber = userNo.getText().toString();
            		if(userNumber.length()<=7 || userNumber.length()>15){
            			Toast.makeText(Smsinbox.this, "Invalid User Number. Should be atleast 8 numbers and not greater than 15 numbers", Toast.LENGTH_LONG).show();
            		}else{
            			SharedPreferences prefs = getSharedPreferences("BANK_IDS",
            					Context.MODE_PRIVATE);
            			Editor editor = prefs.edit();
            			editor.putString("USER_NUMBER", userNumber);
            			user = userNumber;
    					editor.commit();
    					dialog.dismiss();
            		}
                }
            });
            //now that the dialog is set up, it's time to show it    
            dialog.show();
		}
		else{
			user = usernumber;
		}
	}
	
	public void getValidator(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				//GET MESSAGE INFO FOR THE USER BEFORE UPLOADING
		
				HttpClient httpclient = new DefaultHttpClient();
		    	String infourl = serverurl + "user_info.php?user=" + user ; //+ objold.getString("user");
		    	HttpGet get = new HttpGet(infourl.toString());
		    	HttpResponse getresponse = null;
				try {
					getresponse = httpclient.execute(get);
				} catch (ClientProtocolException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				} catch (IOException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
		    	
		    	BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(getresponse.getEntity().getContent(), "UTF-8"));
				} catch (UnsupportedEncodingException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (IllegalStateException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
		        String json_user_info = "";
		        String x;
		        try {
					while((x = reader.readLine())!= null){
						json_user_info += x;
					}
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
		        json_user_info= json_user_info.trim();
		        JSONObject jsonobj = null;
				try {
					jsonobj = new JSONObject(json_user_info);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		        try {
					Log.d("the user info json", jsonobj.getString("sync_info"));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		    	try {
					validator = jsonobj.getJSONObject("sync_info");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//END OF RECEIVING USER INFO FROM SERVER
		    	//GET new filtered messages
		    	JSONObject newmsg = getNewMessages();
		    	
		    	//CALL postData function to start posting messages
		    	postData(serverurl + "upload.php",  newmsg);
		    	
			}
			
		}).start();
	}
	
	public JSONObject getNewMessages(){
		Uri uri = Uri.parse("content://sms/inbox");
	    String[] projection = new String[]{"address","_id", "body","date"};
	    StringBuilder inQuery = new StringBuilder();
	    inQuery.append("1=1");
	    SharedPreferences prefs = getSharedPreferences("BANK_IDS",Context.MODE_PRIVATE);
		Set<String> ids = prefs.getStringSet(Feed.BANK_ID, null);
		if (ids != null && !ids.isEmpty()) {
			inQuery.append(") AND address IN (");
			Iterator<String> iterator = ids.iterator();
			while (iterator.hasNext()) {
				inQuery.append("'"+iterator.next()+"'");
				if(iterator.hasNext())
					inQuery.append(",");
			}
		}
		
		Cursor mCursor= getContentResolver().query(uri, projection,inQuery.toString() ,null,null);
    	
    	JSONObject json =  new JSONObject();
    	try {
        	if(mCursor.getCount()!=0){
        		mCursor.moveToFirst();
        		JSONArray messages = new JSONArray();
        		do{    String sender = mCursor.getString(mCursor.getColumnIndex("address"));
        			   String date = String.valueOf(mCursor.getLong(mCursor.getColumnIndex("date")));
        			   String sms = mCursor.getString(mCursor.getColumnIndex("body"));
        			   if(isNew(sender, date)){
        			   JSONObject message = new JSONObject();
        			   		message.put("sms", sms);
        			   		message.put("sender", sender);
        			   		message.put("date", date);
        			   		messages.put(message);
        			   }
        		}while(mCursor.moveToNext());
        		json.put("smses", messages);
        		json.put("user",this.user);
        	}
    	} catch (JSONException e) {
    		showToast("Data is not good");
    	}
    	
    	return json;
		
	}
		
	public boolean isNew(String sender, String date){
		if(validator.has(sender)){
			try {
				if(Long.valueOf(date.trim()) > Long.valueOf(validator.getString(sender).trim())){
					return true;
				}
				else{
					return false;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			return true;
		}
		return true;
	}

	public void postData(final String url,final JSONObject objold) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// Create a new HttpClient and Post Header
			    HttpClient httpclient = new DefaultHttpClient();
			    

			    try {
			    	JSONObject obj =new JSONObject();
			    	obj.put("user", "USER");
			    	obj.put("smses", "xyz");
			    	  HttpPost post = new HttpPost(url.toString());
			          //post.setHeader("json", obj.toString());
	                  ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
	                  pairs.add(new BasicNameValuePair("myjson", objold.toString()));
	                  
	                  Log.d("JSON OBJECT", objold.toString());
	                  post.setEntity(new UrlEncodedFormEntity(pairs));
	               
			        // Execute HTTP Post Request
			        HttpResponse response = httpclient.execute(post);
			        Log.d("RECIEVED RESPONSE", EntityUtils.toString(response.getEntity()));
			     
			        
			        showToast(response.getStatusLine().toString());
			        
			       
			    } catch (ClientProtocolException e) {
			    	showToast(e.getMessage());
			    } catch (IOException e) {
			    	
			    	showToast(e.getMessage());
			    } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			}
		}).start();
	}
		
	
		
	@Override
	public void onResume(){
		super.onResume();
		Uri uri = Uri.parse("content://sms/inbox");
	    String[] projection = new String[]{"address","_id"};
	    StringBuilder inQuery = new StringBuilder();
	    SharedPreferences prefs = getSharedPreferences("BANK_IDS",Context.MODE_PRIVATE);
		Set<String> ids = prefs.getStringSet(Feed.BANK_ID, null);
		if (ids != null && !ids.isEmpty()) {
			inQuery.append("AND address IN (");
			Iterator<String> iterator = ids.iterator();
			while (iterator.hasNext()) {
				inQuery.append("'"+iterator.next()+"'");
				if(iterator.hasNext())
					inQuery.append(",");
			}
			inQuery.append(") ");
		}
		Cursor c= getContentResolver().query(uri, projection,"1=1) "+inQuery+"GROUP BY (address" ,null,null);
	
	    String[] from = new String[]{"address"};
	 
	    int[] to = new int[]{R.id.numbText};
	    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),R.layout.display,c ,from,to,0);
	    
	    final ListView listView =(ListView)findViewById(R.id.listView1);
	   
	    listView.setAdapter(adapter);
	   
	   
      
       
	    
	    listView.setOnItemClickListener(new OnItemClickListener() {
	   

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String selectedSender = ((TextView)arg1.findViewById(R.id.numbText)).getText().toString();
				Log.v("Nikhila", "Item selected = "+selectedSender);	
				Intent smsbody = new Intent(getApplicationContext(), Smsbody.class);
				smsbody.putExtra("sender", selectedSender);
				startActivity(smsbody);
				
			}
	    	
		});
	}
	private void showToast(final String message){
		 runOnUiThread(new  Runnable() {
				
				@Override
				public void run() {
			        Toast.makeText(Smsinbox.this, message, Toast.LENGTH_LONG).show();
					
				}
			});
	}
	
}
	    

		 



