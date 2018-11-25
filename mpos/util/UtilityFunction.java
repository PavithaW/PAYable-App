package com.mpos.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.setting.env.Consts;

import java.io.ByteArrayOutputStream;

public class UtilityFunction {

	public static String encodeTobase64(Bitmap image) {

		if (image == null) {
			return null;
		}

		Bitmap immagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

		return imageEncoded;
	}

	public static Bitmap decodeBase64(String input) {

		if (input == null) {
			return null;
		}

		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory
				.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public static String getCurrencyTypeString(int currencyType) {

		switch (currencyType) {
			case Consts.LKR:
				return "LKR";
			case Consts.USD:
				return "USD";
			case Consts.GBP:
				return "GBP";
			case Consts.EUR:
				return "EUR";
		}
		return "LKR";
	}

	public static String getCurrencyTypeStringSI(int currencyType) {

		switch (currencyType) {
			case Consts.LKR:
				return "re";
			case Consts.USD:
				return "we'fvd'";
			case Consts.GBP:
				return "mjqï";
			case Consts.EUR:
				return "hqfrda";
		}
		return "re";
	}

	public static String getCurrencyTypeStringTA(int currencyType) {

		switch (currencyType) {
			case Consts.LKR:
				return "&gh";
			case Consts.USD:
				return "m.nlhyu;";
			case Consts.GBP:
				return "gTz;l;";
			case Consts.EUR:
				return "ANuh";
		}
		return "&gh";
	}
}
