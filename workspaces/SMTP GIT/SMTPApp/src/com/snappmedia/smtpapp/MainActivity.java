package com.snappmedia.smtpapp;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	String user;
	String pass;
	String	 recipient;
	String title;
	
	EditText name;
	EditText useremail;
	EditText phone;
	EditText country;
	
	Spinner liketoreceive;
	Spinner aboutus;
	
	

	Button send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = "ADD EMAIL ADDRESS HERE";
		pass = "PASSWORD TO EMAIL ADDRESS";
		recipient = "ADD EMAIL ADDRESS HERE";
		title = "New User: ";
		
		name = (EditText) findViewById(R.id.name);
		useremail = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phonenumber);
		country = (EditText) findViewById(R.id.country);
		
		liketoreceive = (Spinner) findViewById(R.id.SpinnerContactType);
		aboutus = (Spinner) findViewById(R.id.SpinnerHearAbout);

		send = (Button) findViewById(R.id.send);
		send.setOnClickListener(this);

	}

	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.send) {
			
			if (name.getText().toString().length() == 0 || useremail.getText().toString().length() == 0 || country.getText().toString().length() == 0 || country.getText().toString().length() == 0) {
			
				Toast.makeText(this, "Please Complete all fields", Toast.LENGTH_SHORT).show();
			}
			else {
				String[] recp = { recipient }; // add more email addresses here //
				
				SendEmailAsyncTask email = new SendEmailAsyncTask();
				
				email.m = new Mail(user, pass);
				email.m.set_to(recp);
				
				email.m.set_from(useremail.getText().toString());  //doesnt seem to do anything // 
				email.m.set_subject(title + useremail.getText().toString());
				
				email.m.setBody("Name: " + name.getText().toString() + System.getProperty("line.separator")
						+ "  EMAIL: " + useremail.getText().toString() + System.getProperty("line.separator")
						+ "  COUNTRY: " + country.getText().toString() + System.getProperty("line.separator")
						+ "  PHONE NUMBER: " + phone.getText().toString() + System.getProperty("line.separator")
						+ "  LIKE TO RECEIVE: " + liketoreceive.getSelectedItem().toString() + System.getProperty("line.separator")
						+ "  HEARD ABOUT US: " + aboutus.getSelectedItem().toString() + System.getProperty("line.separator")
						);
				
				Toast.makeText(this, "Message Sent.", Toast.LENGTH_SHORT).show();
				email.execute();
				
			}
		}
	}

}

class SendEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {
	Mail m;

	public SendEmailAsyncTask() {
		if (BuildConfig.DEBUG)
			Log.v(SendEmailAsyncTask.class.getName(), "SendEmailAsyncTask()");

	}

	@Override
	protected Boolean doInBackground(Void... params) {
		if (BuildConfig.DEBUG)
			Log.v(SendEmailAsyncTask.class.getName(), "doInBackground()");
		try {
			m.send();
			return true;
		} catch (AuthenticationFailedException e) {
			Log.e(SendEmailAsyncTask.class.getName(), "Bad account details");
			e.printStackTrace();
			return false;
		} catch (MessagingException e) {
			Log.e(SendEmailAsyncTask.class.getName(), m.get_to() + "failed");
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

