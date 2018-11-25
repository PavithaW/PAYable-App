package com.cba.payable;

import com.mpos.util.TypeFaceUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class LogDescriptionActivity extends Activity {

	TextView txtTitle;
	TextView lblLogClass, txtLogClass, lblLogDate, txtLogDate, lblLogDes, txtLogDes, lblLogLevel, txtLogLevel,
			lblLogToken, txtLogToken;
	private int loglevel, logtoken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_description);

		InitViews();

		Bundle extras = getIntent().getExtras();

		if (extras != null) {
			txtLogClass.setText(extras.getString("logclass"));
			txtLogDate.setText(extras.getString("logdate"));
			txtLogDes.setText(extras.getString("logdesc"));

			txtLogLevel.setText(String.valueOf(extras.getInt("loglevel")));
			txtLogToken.setText(String.valueOf(extras.getInt("logtoken")));
		}
	}

	private void InitViews() {
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

		lblLogClass = (TextView) findViewById(R.id.lblLogClass);
		lblLogClass.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtLogClass = (TextView) findViewById(R.id.txtLogClass);
		txtLogClass.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

		lblLogDate = (TextView) findViewById(R.id.lblLogDate);
		lblLogDate.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtLogDate = (TextView) findViewById(R.id.txtLogDate);
		txtLogDate.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

		lblLogDes = (TextView) findViewById(R.id.lblLogDes);
		lblLogDes.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtLogDes = (TextView) findViewById(R.id.txtLogDes);
		txtLogDes.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

		lblLogLevel = (TextView) findViewById(R.id.lblLogLevel);
		lblLogLevel.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtLogLevel = (TextView) findViewById(R.id.txtLogLevel);
		txtLogLevel.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

		lblLogToken = (TextView) findViewById(R.id.lblLogToken);
		lblLogToken.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtLogToken = (TextView) findViewById(R.id.txtLogToken);
		txtLogToken.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

	}
}
