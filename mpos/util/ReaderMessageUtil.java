package com.mpos.util;

import android.content.Context;

public class ReaderMessageUtil {

	Context context;
	LangPrefs langPrefs;

	static int lang_status = 0;

	public ReaderMessageUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		lang_status = LangPrefs.getLanguage(this.context);
	}

	public static String readerConnected() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Reader connected.";

		} else if (lang_status == LangPrefs.LAN_SIN) {

			return "Ívrh iïnkaê;hs'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "thrpg;ghd; ,izf;fg;gl;Ls;sJ";

		} else {

			return "Reader connected.";
		}
	}

	public static String readerConnecting() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Trying to connect with cardreader ...";

		} else if (lang_status == LangPrefs.LAN_SIN) {

			return "ldâ Ívrh iu. iïnkaO fjñka mj;S'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "thrpg;ghid  ,izf;f Kaw;rpf;fg;gLfpd;wJ";

		} else {

			return "Trying to connect with cardreader ...";
		}
	}

	public static String readerNotConnected() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Please turn on the cardreader.";

		} else if (lang_status == LangPrefs.LAN_SIN) {

			return "ldâ Ívrh l%shd;aul lrkak'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "thrpg;ghid  Md; nra;f";

		} else {

			return "Please turn on the cardreader.";
		}
	}

	public static String readerVerifying() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Reader connected.Verifying ...";

		} else if (lang_status == LangPrefs.LAN_SIN) {

			return "Ívrh iïnkaê;hs' mÍlaId flfrñka mj;S'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "thrpg;ghd; ,izf;fg;gl;Ls;sJ. rupghu;f;fg;gLfpd;wJ.";

		} else {

			return "Reader connected.Verifying ...";
		}
	}

	public static String readerDisconnected() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Reader disconnected. Please check the reader.";

		} else if (lang_status == LangPrefs.LAN_SIN) {

			return "Ívrh úikaê úh' Ívrh mÍlaId lrkak'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "thrpg;ghd; Jz;bf;fg;gl;Ls;sJ. rupghu;f;fTk;";

		} else {

			return "Reader disconnected. Please check the reader.";
		}
	}

	// ================DS Sales Audio Messages==================//

	public static String waitReader() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Please wait !!";

		}

		else if (lang_status == LangPrefs.LAN_SIN) {
			// Sinhala Translation needed
			return "Ívrh úikaê úh' Ívrh mÍlaId lrkak'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "jaT nra;J fhj;jpUf;fTk;";

		} else {

			return "Please wait !!";
		}
	}

	public static String processTransaction() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Processing transaction.";

		}

		else if (lang_status == LangPrefs.LAN_SIN) {
			// Sinhala Translation needed
			return "Ívrh úikaê úh' Ívrh mÍlaId lrkak'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "gupkhw;wk; nrayhf;fg;gLfpd;wJ ";

		} else {

			return "Processing transaction.";
		}
	}

	public static String updateCard() {
		if (lang_status == LangPrefs.LAN_EN) {

			return "Updating the card ...";

		}

		else if (lang_status == LangPrefs.LAN_SIN) {
			// Sinhala Translation needed
			return "Ívrh úikaê úh' Ívrh mÍlaId lrkak'";

		} else if (lang_status == LangPrefs.LAN_TA) {

			return "ml;il Nkk;gLj;jg;gLfpd;wJ";

		} else {

			return "Updating the card ...";
		}
	}

}
