package com.mpos.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cba.payable.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthYearCardTextWatcher implements TextWatcher {

    private static final String INITIAL_MONTH_ADD_ON = "0";
    private static final int MAX_MONTH = 12;
    private static final String DEFAULT_MONTH = "01";
    private static final String SPACE = "/";
    private EditText mEditText;
    private Button mbtnCharge;
    private RelativeLayout mrlCard;
    private Context mContext;
    private int mLength;
    private boolean isValidDate = false;
    private String mLastInput = "";
    boolean isSlash = false; //class level initialization
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
    private Integer MAX_LIMIT_YEAR = 20;
    private boolean isGoodyear = false;

    public MonthYearCardTextWatcher(Context context, EditText editText, Button btnCharge, RelativeLayout rlCard) {
        mContext = context;
        mEditText = editText;
        mbtnCharge = btnCharge;
        mrlCard = rlCard;

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        mLength = mEditText.getText().length();

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mEditText.setError(null);

    }

    private void formatCardExpiringDate(Editable s) {
        String input = s.toString();
        String mLastInput = "";

        SimpleDateFormat formatter = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        Calendar expiryDateDate = Calendar.getInstance();

        try {
            expiryDateDate.setTime(formatter.parse(input));
            isValidDate = true;
        } catch (java.text.ParseException e) {

            isValidDate = false;

            if (s.length() == 2 && !mLastInput.endsWith(SPACE) && isSlash) {
                isSlash = false;
                int month = Integer.parseInt(input);
                if (month <= MAX_MONTH) {
                    mEditText.setText(mEditText.getText().toString().substring(0, 1));
                    mEditText.setSelection(mEditText.getText().toString().length());
                } else {
                    s.clear();
                    mEditText.setText("");
                    mEditText.setSelection(mEditText.getText().toString().length());
                }
            } else if (s.length() == 2 && !mLastInput.endsWith(SPACE) && !isSlash) {
                isSlash = true;
                int month = Integer.parseInt(input);
                if (month <= MAX_MONTH) {
                    mEditText.setText(mEditText.getText().toString() + SPACE);
                    mEditText.setSelection(mEditText.getText().toString().length());
                } else if (month > MAX_MONTH) {
                    mEditText.setSelection(mEditText.getText().toString().length());
                    s.clear();
                }
            } else if (s.length() == 1) {
                int month = Integer.parseInt(input);

                if (month > 1 && month < MAX_MONTH) {
                    isSlash = true;
                    mEditText.setText(INITIAL_MONTH_ADD_ON + mEditText.getText().toString() + SPACE);
                    mEditText.setSelection(mEditText.getText().toString().length());
                }
            }

            mLastInput = mEditText.getText().toString();
            return;
        }

        //button status
        if (isValidDate && mEditText.getText().length() == 5) {
            if (mEditText.getText().length() == 5) {
                String expDate = mEditText.getText().toString();

                try {
                    String MMYY[] = expDate.split(SPACE);
                    int EXP_YY = Integer.valueOf(MMYY[1]);

                    String NOW_MMYY[] = sdf.format(new Date()).split(SPACE);
                    int NOW_YY = Integer.valueOf(NOW_MMYY[1]) + MAX_LIMIT_YEAR;

                    if (NOW_YY >= EXP_YY) {
                        isGoodyear = true;
                    } else {
                        isGoodyear = false;
                    }

                } catch (Exception e) {
                    isGoodyear = false;
                }

                if (isGoodyear) {
                    mbtnCharge.setBackgroundResource(R.drawable.sign_in_btn_selector);
                    mbtnCharge.setEnabled(true);
                } else {
                    mEditText.setText("");
                    mbtnCharge.setBackgroundColor(ContextCompat.getColor(mContext, R.color.txtfields_color));
                    mbtnCharge.setEnabled(false);
                }

            } else {
                mbtnCharge.setBackgroundColor(ContextCompat.getColor(mContext, R.color.txtfields_color));
                mbtnCharge.setEnabled(false);
            }
        } else {
            mbtnCharge.setBackgroundColor(ContextCompat.getColor(mContext, R.color.txtfields_color));
            mbtnCharge.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        mEditText.setTextColor(Color.BLACK);

        //added by amal
        try {
            formatCardExpiringDate(s);
        } catch (NumberFormatException e) {
            s.clear();
        }

        /*int currentLength = mEditText.getText().length();
        boolean ignoreBecauseIsDeleting = false;
        if (mLength > currentLength) {
            ignoreBecauseIsDeleting = true;
        }

        if (ignoreBecauseIsDeleting && s.toString().startsWith(SPACE)) {
            return;
        }


        if (s.length() == 1 && !isNumberChar(String.valueOf(s.charAt(0)))) {
            System.out.println("Key 1");
            mEditText.setText(DEFAULT_MONTH + SPACE);
        } else if (s.length() == 1 && !isCharValidMonth(s.charAt(0))) {
            System.out.println("Key 2");
            mEditText.setText(INITIAL_MONTH_ADD_ON + String.valueOf(s.charAt(0)) + SPACE);
        } else if (s.length() == 2 && String.valueOf(s.charAt(s.length() - 1)).equals(SPACE)) {
            System.out.println("Key 3");
            mEditText.setText(INITIAL_MONTH_ADD_ON + String.valueOf(s));
        } else if (!ignoreBecauseIsDeleting &&
                (s.length() == 2 && !String.valueOf(s.charAt(s.length() - 1)).equals(SPACE))) {

            int enteredMonth = Integer.valueOf(mEditText.getText().toString());
            if (enteredMonth > MAX_MONTH) {
                mEditText.setText("12/");
            } else {
                System.out.println("Key 4");
                mEditText.setText(mEditText.getText().toString() + SPACE);
            }

        } else if (s.length() == 3 && !String.valueOf(s.charAt(s.length() - 1)).equals(SPACE) && !ignoreBecauseIsDeleting) {
            System.out.println("Key 4");
            s.insert(2, SPACE);
            mEditText.setText(String.valueOf(s));
        } else if (s.length() > 3 && !isCharValidMonth(s.charAt(0))) {
            System.out.println("Key 5");
            mEditText.setText(INITIAL_MONTH_ADD_ON + s);
        } else if (s.length() == 5) {
            System.out.println("Key 5");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
            Date strDate = null;
            try {
                strDate = sdf.parse(s.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String MMYY[] = s.toString().split("/");
            int MM = Integer.valueOf(MMYY[0]);
            int YY = Integer.valueOf(MMYY[1]);

            //get current month and year
            String NOW_MMYY[] = sdf.format(new Date()).split("/");

            int NOW_MM = Integer.valueOf(NOW_MMYY[0]);
            int NOW_YY = Integer.valueOf(NOW_MMYY[1]);

            if (NOW_MM == MM && NOW_YY == YY) {
                isValidDate = true;
            } else if (MM > 12 || new Date().after(strDate)) {
                isValidDate = false;
            } else {
                isValidDate = true;
            }

            if (isValidDate) {
                mbtnCharge.setBackgroundResource(R.drawable.sign_in_btn_selector);
                mbtnCharge.setEnabled(true);
                //mbtnCharge.setBackgroundColor(ContextCompat.getColor(mContext, Color.parseColor("#1E82C5")));
            } else {
                mEditText.setTextColor(Color.RED);
            }


        } else if (s.length() < 5) {
            System.out.println("Key 6");
            mbtnCharge.setBackgroundColor(mContext.getResources().getColor(R.color.void_dialogbtn));
        }

        if (!ignoreBecauseIsDeleting) {
            mEditText.setSelection(mEditText.getText().toString().length());
        }*/

    }

    private boolean isCharValidMonth(char charFromString) {
        int month = 0;
        if (Character.isDigit(charFromString)) {
            month = Integer.parseInt(String.valueOf(charFromString));
        }
        return month <= 1;
    }

    // \d : represents digit character
    private boolean isNumberChar(String string) {
        return string.matches(".*\\d.*");
    }

}
