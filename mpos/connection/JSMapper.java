package com.mpos.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mpos.pojo.APIData;
import com.mpos.pojo.LstBatchlistRes;
import com.mpos.pojo.LstRecSale;
import com.mpos.pojo.LstSettleSummary;
import com.mpos.pojo.Merchant;
import com.mpos.pojo.SimpleAck;
import com.mpos.pojo.TXDsNfcReq;
import com.mpos.pojo.TxSaleRes;
import com.mpos.pojo.TxSettlementResponse;
import com.mpos.pojo.TxVoidRes;


public class JSMapper {

    public static APIData getData(String strRes, CallParams param) {
        APIData resData = null;
        Gson gson = new GsonBuilder().create();

        switch (param.getEndpoint()) {
            case SAMPLE_CALL:
                resData = gson.fromJson(strRes, APIData.class);
                break;

            case SignIn:
                resData = gson.fromJson(strRes, Merchant.class);
                break;

		/*	
		case EReceiptSales :
			resData  = gson.fromJson(strRes, Merchant.class);
			break ;*/

            case Sales:
                resData = gson.fromJson(strRes, TxSaleRes.class);
                break;

            case EmvSales:
                resData = gson.fromJson(strRes, TxSaleRes.class);
                break;

            case DsSale:
                resData = gson.fromJson(strRes, TxSaleRes.class);
                break;

            case DSEmvSale:
                resData = gson.fromJson(strRes, TxSaleRes.class);
                break;

            case Signature:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case Settlement:
                resData = gson.fromJson(strRes, TxSettlementResponse.class);
                break;

            case voidTx:
                resData = gson.fromJson(strRes, TxVoidRes.class);
                break;

            case CloseTxHistory:
                LstRecSale lst = new LstRecSale();
                lst.setList(strRes);
                resData = lst;
                break;

            case OpenTxHistory:
                LstRecSale lstOpenTx = new LstRecSale();
                lstOpenTx.setList(strRes);
                resData = lstOpenTx;
                break;

            case SignUp:
                resData = gson.fromJson(strRes, Merchant.class);
                break;

            case SettlementSummary:
                LstSettleSummary lstSummary = new LstSettleSummary();
                lstSummary.setList(strRes);
                resData = lstSummary;
                break;

            case SettlementSummaryV2:
                LstSettleSummary lstSummaryV2 = new LstSettleSummary();
                lstSummaryV2.setList(strRes);
                resData = lstSummaryV2;
                break;

            case ReceiptEmail:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case ReceiptSMS:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case ModifyPassword:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case DeviceInfo:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case Batchlist:
                LstBatchlistRes lstBatchList = new LstBatchlistRes();
                lstBatchList.setList(strRes);
                resData = lstBatchList;
                break;

            case BatchTxHistory:
                LstRecSale lstBatchTx = new LstRecSale();
                lstBatchTx.setList(strRes);
                resData = lstBatchTx;
                break;

            case Echo:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case KeyEntryTx:
                resData = gson.fromJson(strRes, TxSaleRes.class);
                break;

            case ResendReceiptEmail:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case ResendReceiptSMS:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case DSICCOflineErr:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

             case BatchEmvLogs:
                resData = gson.fromJson(strRes, SimpleAck.class);
                break;

            case DSNfcSale:
                resData = gson.fromJson(strRes, TxSaleRes.class);
                break;

            default:
                resData = null;

        }

        return resData;
    }

}
