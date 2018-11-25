package com.cba.payable;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;

public class SettingsSecondary extends GenericActivity {

    TextView txtTitle, txtCaption, txtVisaMaster, txtAmexDiners;
    RelativeLayout rlVisaMaster, rlAmexDiners;
    public static boolean isSettingsSecondaryClose;

    @Override
    protected void onInitActivity(APIData... data) {
        setContentView(R.layout.activity_settings_secondary);
        InitViews();
    }

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.rlVisaMaster: {
                    Home.isAmexCardSelected = false;
                    Intent intent = new Intent(SettingsSecondary.this, Setting.class);
                    intent.putExtra("isAmex", false);
                    startActivityForResult(intent, 0);
                    break;
                }

                case R.id.rlAmexDiners: {
                    Home.isAmexCardSelected = true;
                    Intent intent = new Intent(SettingsSecondary.this, Setting.class);
                    intent.putExtra("isAmex", true);
                    startActivityForResult(intent, 0);
                    break;
                }

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if (isSettingsSecondaryClose) {
            isSettingsSecondaryClose = false;
            finish();
        }

    }

    private void InitViews() {
        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);
        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtCaption = (TextView) findViewById(R.id.txtCaption);
        txtCaption.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        rlVisaMaster = (RelativeLayout) findViewById(R.id.rlVisaMaster);
        rlVisaMaster.setOnClickListener(onClickListener);

        txtVisaMaster = (TextView) findViewById(R.id.txtVisaMaster);
        txtVisaMaster.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        rlAmexDiners = (RelativeLayout) findViewById(R.id.rlAmexDiners);
        rlAmexDiners.setOnClickListener(onClickListener);

        txtAmexDiners = (TextView) findViewById(R.id.txtAmexDiners);
        txtAmexDiners.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_Secondary_Settings(txtTitle, txtCaption, txtVisaMaster, txtAmexDiners);
        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_Secondary_Settings(txtTitle, txtCaption, txtVisaMaster, txtAmexDiners);
        } else {
            // do nothing
        }
    }
}
