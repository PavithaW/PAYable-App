package com.cba.payable;

import java.util.HashMap;

import com.mpos.pojo.APIData;
import com.mpos.pojo.Merchant;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.setting.env.Config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChangeLanguageActivity extends GenericActivity {

	TextView txtTitle, txtEnglish, txtSinhala, txtTamil;
	CheckBox chkEnglish, chkSinhala, chkTamil;

	RelativeLayout rlSubmit, rlEnglish, rlSinhala, rlTamil;

	LangPrefs langPrefs;
	Sinhala_LangSelect sinlang_select;
	Tamil_LangSelect tamlang_select;

	int lang_status = 0;
	int lang_status2 = 0;

	public static boolean isLanguageChanged;

	@Override
	protected void onInitActivity(APIData... data) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_change_language);

		InitViews();
	}
	
	public void onNavBack(View v) {

		onNavBackPressed();
	}

	private void InitViews() {

		lang_status = LangPrefs.getLanguage(getApplicationContext());

		lang_status2 = lang_status;

		sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

		tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

		txtEnglish = (TextView) findViewById(R.id.txtEnglish);
		txtEnglish.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

		txtSinhala = (TextView) findViewById(R.id.txtSinhala);
		txtSinhala.setText("isxy,");
		txtSinhala.setTypeface(TypeFaceUtils.setSinhalaFont(getApplicationContext()));

		txtTamil = (TextView) findViewById(R.id.txtTamil);
		txtTamil.setText("jkpo;");
		txtTamil.setTypeface(TypeFaceUtils.setBaminiFont(getApplicationContext()));

		chkEnglish = (CheckBox) findViewById(R.id.chkEnglish);

		chkTamil = (CheckBox) findViewById(R.id.chkTamil);

		chkSinhala = (CheckBox) findViewById(R.id.chkSinhala);

		rlTamil = (RelativeLayout) findViewById(R.id.rlTamil);
		rlTamil.setOnClickListener(onClickListener);

		rlEnglish = (RelativeLayout) findViewById(R.id.rlEnglish);
		rlEnglish.setOnClickListener(onClickListener);

		rlSinhala = (RelativeLayout) findViewById(R.id.rlSinhala);
		rlSinhala.setOnClickListener(onClickListener);

		rlSubmit = (RelativeLayout) findViewById(R.id.rlSubmit);

		if (lang_status == LangPrefs.LAN_EN) {
			chkEnglish.setChecked(true);
			chkSinhala.setChecked(false);
			chkTamil.setChecked(false);
			lang_status = 0;
		}

		else if (lang_status == LangPrefs.LAN_TA) {
			chkTamil.setChecked(true);
			chkSinhala.setChecked(false);
			chkEnglish.setChecked(false);
			tamlang_select.Apply_ChangeLanguage(txtTitle, txtEnglish, txtSinhala, txtTamil);
		}

		else if (lang_status == LangPrefs.LAN_SIN) {
			chkSinhala.setChecked(true);
			chkEnglish.setChecked(false);
			chkTamil.setChecked(false);
			sinlang_select.Apply_ChangeLanguage(txtTitle, txtEnglish, txtSinhala, txtTamil);

		} else {
			// do nothing
			chkEnglish.setChecked(true);
			chkSinhala.setChecked(false);
			chkTamil.setChecked(false);
			lang_status = 1;
		}

		rlSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (lang_status2 == lang_status) {
					showToastMessage_local("Please Select a different language", "changelang_val", lang_status);
				} else {
					LangPrefs.setLanguage(ChangeLanguageActivity.this, lang_status);
					// Intent in = new Intent(getApplicationContext(),
					// SignInActivity.class);
					// startActivity(in);
					// finish();
					showToastMessage_local("You langauge is been changed to English", "changelang", lang_status);
					_fireLogOut();
				}
			}
		});

	}

	private View.OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {

			case R.id.rlEnglish: {
				lang_status = 0;
				chkEnglish.setChecked(true);
				chkSinhala.setChecked(false);
				chkTamil.setChecked(false);
				break;
			}

			case R.id.rlTamil: {
				lang_status = 1;
				chkTamil.setChecked(true);
				chkSinhala.setChecked(false);
				chkEnglish.setChecked(false);
				break;
			}

			case R.id.rlSinhala: {
				lang_status = 2;
				chkSinhala.setChecked(true);
				chkEnglish.setChecked(false);
				chkTamil.setChecked(false);
				break;
			}
			}
		}
	};

	private void _fireLogOut() {
		/*Merchant m = Config.getUser(getApplicationContext());
		m.setAuth1(0);
		m.setAuth2(0);

		Config.setState(getApplicationContext(), Config.STATUS_LOGOUT);
		Config.setUser(getApplicationContext(), m);

		logOut();*/

		/*Setting.isSettingsClose = true;
		pushActivity(Launcher.class);
		finish();*/

		isLanguageChanged = true;
		Setting.isSettingsClose = true;
		SettingsSecondary.isSettingsSecondaryClose= true;
		finish();
	}

}
