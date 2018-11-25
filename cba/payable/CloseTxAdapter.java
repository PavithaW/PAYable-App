package com.cba.payable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpos.connection.PaginationHandler;
import com.mpos.storage.RecSale;
import com.mpos.util.LangPrefs;
import com.mpos.util.Sinhala_LangSelect;
import com.mpos.util.Tamil_LangSelect;
import com.mpos.util.TypeFaceUtils;
import com.mpos.util.UtilityFunction;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CloseTxAdapter extends MposPageAdapter<RecSale> {

    private static final String LOG_TAG = "payable " + CloseTxAdapter.class.getSimpleName();

    private DecimalFormat df;
    private SimpleDateFormat sdf;

    Tamil_LangSelect tamlang_select;
    Sinhala_LangSelect sinlang_select;

    private int mLastPosition = -1;

    //boolean track_id_status=false;

    public CloseTxAdapter(Context c, PaginationHandler ph, int pageSize, int cId) {
        super(c, ph, pageSize, cId);

        df = new DecimalFormat("####0.00");
        // sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm a",Locale.US);
        //sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm", Locale.US);
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.US);

        Activity activity = (Activity) context;

        tamlang_select = new Tamil_LangSelect(c, activity);
        sinlang_select = new Sinhala_LangSelect(c, activity);
    }

    @Override
    protected View renderListRow(RecSale data, int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // v = vi.inflate(R.layout.lstopentx2, null);
            v = vi.inflate(R.layout.lstdata_history, null);
            v.setTag(new Integer(ROW_DATA));
        } else {
            int tag = ((Integer) convertView.getTag()).intValue();

            if (tag != ROW_DATA) {
                LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // v = vi.inflate(R.layout.lstopentx2, null);
                v = vi.inflate(R.layout.lstdata_history, null);
                v.setTag(new Integer(ROW_DATA));
            }
        }

        TextView txtName = (TextView) v.findViewById(R.id.txtCardName);
        txtName.setTypeface(TypeFaceUtils.setRobotoRegular(context));

        TextView txtAmount = (TextView) v.findViewById(R.id.txtAmount);
        txtAmount.setTypeface(TypeFaceUtils.setRobotoMedium(context));

        TextView txtLastCC4 = (TextView) v.findViewById(R.id.txtSerialNo);
        txtLastCC4.setTypeface(TypeFaceUtils.setLatoBold(context));

        TextView txtTs = (TextView) v.findViewById(R.id.txtDate);
        txtTs.setTypeface(TypeFaceUtils.setRobotoMedium(context));

        TextView txtStatus = (TextView) v.findViewById(R.id.txtType);
        txtStatus.setTypeface(TypeFaceUtils.setRobotoMedium(context));

        TextView txtTrackingid = (TextView) v.findViewById(R.id.txtTrackingid);
        TextView txtInst = (TextView) v.findViewById(R.id.instTv);
        txtTrackingid.setTypeface(TypeFaceUtils.setRobotoMedium(context));
        txtInst.setTypeface(TypeFaceUtils.setRobotoMedium(context));

        ImageView imgCard = (ImageView) v.findViewById(R.id.imgCardIcon);
        ImageView imgStatus = (ImageView) v.findViewById(R.id.imghistory);

        RecSale rec = arrItems.get(position);
        int langstatus = LangPrefs.getLanguage(context);
        int txType = rec.getTxType();

        if (txType == RecSale.MANUAL_TRANSACTION) {
            txtName.setText("Manual Transaction");
        } else {
            String nm = rec.getCardHolder().trim();
            if (nm.length() > 19) {
                nm = nm.substring(0, 19);
            }
            txtName.setText(nm);
        }

        String ct = UtilityFunction.getCurrencyTypeString(rec.getCurrencyType());

        int inst = rec.getInstallment();
        if (inst != 0) {
            if (langstatus == LangPrefs.LAN_EN) {
                txtInst.setTypeface(TypeFaceUtils.setRobotoMedium(context));
                txtInst.setText("Inst : " + String.valueOf(inst));
            } else if (langstatus == LangPrefs.LAN_TA) {
                tamlang_select.setInstallment(txtInst, String.valueOf(inst));
            } else if (langstatus == LangPrefs.LAN_SIN) {
                sinlang_select.setInstallment(txtInst, String.valueOf(inst));
            }
        } else {
            txtInst.setText("");
        }


        System.out.println("MyTIMe "+ rec.getTime());

        txtAmount.setText(df.format(rec.getAmount()) + " " + ct);
        txtLastCC4.setText(rec.getCcLast4());
        txtTs.setText(sdf.format(rec.getTime()));


        //CHANGE CARD ICON
        if (rec.getCardType() == RecSale.CARD_VISA) {
            imgCard.setImageResource(R.drawable.visacard_icon);
        } else if (rec.getCardType() == RecSale.CARD_MASTER) {
            imgCard.setImageResource(R.drawable.mastercard_icon);
        } else if (rec.getCardType() == RecSale.CARD_AMEX) {
            imgCard.setImageResource(R.drawable.amex);
        } else if (rec.getCardType() == RecSale.CARD_DINERS) {
            imgCard.setImageResource(R.drawable.diners_club);
        }

        if ((rec.getStatus() == RecSale.STATUS_OPEN) || (rec.getStatus() == RecSale.STATUS_CLOSE)) {

            if (langstatus == LangPrefs.LAN_EN) {
                txtStatus.setText("Sale");
            } else if (langstatus == LangPrefs.LAN_TA) {
                tamlang_select.Apply_VoidList(txtStatus, "Sale");
            } else if (langstatus == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_VoidList(txtStatus, "Sale");
            } else {

            }
        }

        if (rec.getStatus() == RecSale.STATUS_CLOSE_VOID || rec.getStatus() == RecSale.STATUS_OPEN_VOID) {

            if (langstatus == LangPrefs.LAN_EN) {
                txtStatus.setText("Void");
            } else if (langstatus == LangPrefs.LAN_TA) {
                tamlang_select.Apply_VoidList(txtStatus, "Void");
            } else if (langstatus == LangPrefs.LAN_SIN) {
                sinlang_select.Apply_VoidList(txtStatus, "Void");
            } else {

            }

            // lang_status = LangPrefs.getLanguage(context);
            //
            // if(lang_status==LangPrefs.LAN_EN)
            // {
            // txtStatus.setText("Void") ;
            // }
            // }else{
            // lang_status = LangPrefs.getLanguage(context);
            //
            // if(lang_status==LangPrefs.LAN_EN)
            // {
            // txtStatus.setText("Sale") ;
            // }
        }


        if (txType == RecSale.MANUAL_TRANSACTION) {
            imgStatus.setVisibility(View.INVISIBLE);
        } else {
            imgStatus.setVisibility(View.VISIBLE);

            if (rec.getSigFlag() == RecSale.SIGNATURE_AVAILABLE) {
                imgStatus.setImageResource(R.drawable.history_check_icon);
            } else if (rec.getSigFlag() == RecSale.SIGNATURE_NOT_AVAILABLE) {
                imgStatus.setImageResource(R.drawable.history_void_icon);
            }
        }


        /*if (rec.getSigFlag() == RecSale.SIGNATURE_AVAILABLE) {
            imgStatus.setImageResource(R.drawable.history_check_icon);
        }

        if (rec.getSigFlag() == RecSale.SIGNATURE_NOT_AVAILABLE) {
            imgStatus.setImageResource(R.drawable.history_void_icon);
        }

        if (txType == RecSale.MANUAL_TRANSACTION) {
            imgStatus.setVisibility(View.INVISIBLE);
        }*/


        if (rec.getMerchantInvoiceId() != null && rec.getMerchantInvoiceId().trim().length() > 0) {
            txtTrackingid.setVisibility(View.VISIBLE);
            txtTrackingid.setText(rec.getMerchantInvoiceId());
        } else {
            txtTrackingid.setVisibility(View.INVISIBLE);
        }
        // Animation animation = AnimationUtils.loadAnimation(context,
        // (position > mLastPosition) ? R.anim.up_from_bottom :
        // R.anim.down_from_top);
        // convertView.startAnimation(animation);
        // mLastPosition = position;
        return v;
    }
}
