package com.cba.payable;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mpos.pojo.APIData;
import com.mpos.pojo.TXHistoryReq;
import com.mpos.util.CHARFilter;
import com.mpos.util.DateHelper;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Filter extends GenericActivity {

    // private CheckBox chkVisa, chkMaster;
    private EditText txtSt, txtEn;

    private TextView txtTitle, txtFilterOptions, txtStarts, txtSelectDate, txtEnds, txtSelectEndDate, txtCardType, search;
    private TextView txtVisa;
    private TextView txtMaster;
    private TextView txtAmex;
    private TextView txtDiners;
    private CheckBox chkVisa, chkMaster, chkAmex, chkDiners;
    RelativeLayout rlFilter, rlStartDate, rlEndDate, rlVisa, rlMaster, rlAmex, rlDiners;

    private LangPrefs langPrefs;
    private Sinhala_LangSelect sinlang_select;
    private Tamil_LangSelect tamlang_select;

    int lang_status = 0;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    public static boolean isFiltered;

    protected void onInitActivity(APIData... data) {
        // setContentView(R.layout.layout_filter);

        setContentView(R.layout.activity_filter_history);

        InitViews();
        setDateTimeField();

        if (m_client.isLoggedIn() && m_client_amex.isLoggedIn()) {
            /*rlVisa.setVisibility(View.VISIBLE);
            rlMaster.setVisibility(View.VISIBLE);
            rlAmex.setVisibility(View.VISIBLE);
            rlDiners.setVisibility(View.VISIBLE);*/

            if (CloseTransations.isVMActivated) {
                // VISA SELECTED
                rlVisa.setVisibility(View.VISIBLE);
                rlMaster.setVisibility(View.VISIBLE);
                rlAmex.setVisibility(View.GONE);
                rlDiners.setVisibility(View.GONE);
            } else {
                //AMEX SELECTED
                rlVisa.setVisibility(View.GONE);
                rlMaster.setVisibility(View.GONE);
                rlAmex.setVisibility(View.VISIBLE);
                rlDiners.setVisibility(View.VISIBLE);
            }

        } else {
            if (m_client.isLoggedIn()) {
                rlVisa.setVisibility(View.VISIBLE);
                rlMaster.setVisibility(View.VISIBLE);
                rlAmex.setVisibility(View.GONE);
                rlDiners.setVisibility(View.GONE);
            } else if (m_client_amex.isLoggedIn()) {
                rlVisa.setVisibility(View.GONE);
                rlMaster.setVisibility(View.GONE);
                rlAmex.setVisibility(View.VISIBLE);
                rlDiners.setVisibility(View.VISIBLE);
            }
        }
    }

    public void onFilter(View v) {

        if (!chkVisa.isChecked() && !chkMaster.isChecked() && !chkAmex.isChecked() && !chkDiners.isChecked()) {
            showDialog("Error", "Please Select at least one card.");
            return;
        }

        TXHistoryReq req = new TXHistoryReq();

        String strDateFormat = "dd/MM/yyyy";

        if (chkVisa.isChecked()) {
            req.setVisa(1);
        } else {
            req.setVisa(0);
        }

        if (chkMaster.isChecked()) {
            req.setMaster(1);
        } else {
            req.setMaster(0);
        }

        if (chkAmex.isChecked()) {
            req.setAmex(1);
        } else {
            req.setAmex(0);
        }

        if (chkDiners.isChecked()) {
            req.setDc(1);
        } else {
            req.setDc(0);
        }

        String strSt = txtSelectDate.getText().toString();
        String strEn = txtSelectEndDate.getText().toString();

        //CHECK START DATE
        if (strSt != null) {
            strSt = strSt.trim();

            //Log.i(TAG, "Str St:" + strSt) ;

            int res = CHARFilter.isBloackedCharExist(strSt);

            if (res > 0) {
                showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
                return;
            }

            if (strSt.length() > 0) {
                try {
                    Date d = DateHelper.stringToDate(strSt, strDateFormat);
                    req.setStDate(d);
                } catch (ParseException e) {
                    showDialog("Error", "Please enter the date in a correct format.");
                    //	txtSt.setText("");
                    return;
                }
            }
        }

        //CHECK END DATE
        if (strEn != null) {
            strEn = strEn.trim();

            int res = CHARFilter.isBloackedCharExist(strEn);

            if (res > 0) {
                showDialog("Error", "Character " + CHARFilter.blockedCharacter[res - 1] + " is not allowed to enter.");
                return;
            }

            if (strEn.length() > 0) {
                try {
                    Date d = DateHelper.stringToDate(strEn, strDateFormat);
                    d.setHours(23);
                    d.setMinutes(59);
                    d.setSeconds(59);

                    System.out.println("D date " + d);
                    if (req.getStDate() != null) {

                        if (d.after(req.getStDate()) || d.equals(req.getStDate())) {
                            req.setEnDate(d);
                        } else {
                            showDialog("Error", "Please enter correct end date.");
                            //	txtEn.setText("");
                            return;
                        }

                    } else {
                        req.setEnDate(d);
                    }

                } catch (ParseException e) {
                    showDialog("Error", "Please enter the date in a correct format.");
                    //	txtEn.setText("");
                    return;
                }
            }
        }

        isFiltered = true;
        finishWithResult(req);
    }

    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtSelectDate.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
                txtSelectDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //block select future date
        fromDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                txtSelectEndDate.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
                txtSelectEndDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        //block select future date
        toDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.txtSelectDate: {
                    fromDatePickerDialog.show();
                    break;
                }

                case R.id.rlStartDate: {
                    fromDatePickerDialog.show();
                    break;
                }

                case R.id.txtSelectEndDate: {
                    toDatePickerDialog.show();
                    break;
                }

                case R.id.rlEndDate: {
                    toDatePickerDialog.show();
                    break;
                }
            }
        }
    };

    public void onNavBack(View v) {
        onNavBackPressed();
    }

    private void InitViews() {

        sinlang_select = new Sinhala_LangSelect(getApplicationContext(), this);

        tamlang_select = new Tamil_LangSelect(getApplicationContext(), this);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));

        txtFilterOptions = (TextView) findViewById(R.id.txtFilterOptions);
        txtFilterOptions.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtStarts = (TextView) findViewById(R.id.txtStarts);
        txtStarts.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtSelectDate = (TextView) findViewById(R.id.txtSelectDate);
        txtSelectDate.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        txtSelectDate.setOnClickListener(onClickListener);

        txtEnds = (TextView) findViewById(R.id.txtEnds);
        txtEnds.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtSelectEndDate = (TextView) findViewById(R.id.txtSelectEndDate);
        txtSelectEndDate.setTypeface(TypeFaceUtils.setRobotoRegular(getApplicationContext()));
        txtSelectEndDate.setOnClickListener(onClickListener);

        txtCardType = (TextView) findViewById(R.id.txtCardType);
        txtCardType.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));


        txtVisa = (TextView) findViewById(R.id.txtVisa);
        txtVisa.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtMaster = (TextView) findViewById(R.id.txtMaster);
        txtMaster.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtAmex = (TextView) findViewById(R.id.txtAmex);
        txtAmex.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        txtDiners = (TextView) findViewById(R.id.txtDiners);
        txtDiners.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));


        chkVisa = (CheckBox) findViewById(R.id.chkVisa);
        chkVisa.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        chkMaster = (CheckBox) findViewById(R.id.chkMaster);
        chkMaster.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        chkAmex = (CheckBox) findViewById(R.id.chkAmex);
        chkAmex.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        chkDiners = (CheckBox) findViewById(R.id.chkDiners);
        chkDiners.setTypeface(TypeFaceUtils.setLatoRegular(getApplicationContext()));

        rlFilter = (RelativeLayout) findViewById(R.id.rlFilter);

        rlStartDate = (RelativeLayout) findViewById(R.id.rlStartDate);
        rlStartDate.setOnClickListener(onClickListener);

        rlEndDate = (RelativeLayout) findViewById(R.id.rlEndDate);
        rlEndDate.setOnClickListener(onClickListener);

        rlVisa = (RelativeLayout) findViewById(R.id.rlVisa);
        rlMaster = (RelativeLayout) findViewById(R.id.rlMaster);
        rlAmex = (RelativeLayout) findViewById(R.id.rlAmex);
        rlDiners = (RelativeLayout) findViewById(R.id.rlDiners);

		/*search = (TextView) findViewById(R.id.search);
        search.setTypeface(TypeFaceUtils.setRobotoMedium(getApplicationContext()));*/

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        lang_status = LangPrefs.getLanguage(getApplicationContext());

        if (lang_status == LangPrefs.LAN_EN) {

        } else if (lang_status == LangPrefs.LAN_SIN) {
            sinlang_select.Apply_FilterHistory(txtTitle, txtFilterOptions, txtStarts, txtSelectDate, txtEnds,
                    txtSelectEndDate, txtCardType, txtVisa, txtMaster, txtAmex, txtDiners);

        } else if (lang_status == LangPrefs.LAN_TA) {
            tamlang_select.Apply_FilterHistory(txtTitle, txtFilterOptions, txtStarts, txtSelectDate, txtEnds,
                    txtSelectEndDate, txtCardType, txtVisa, txtMaster, txtAmex, txtDiners);
        } else {
            // do nothing
        }

    }

}
