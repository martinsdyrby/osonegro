package com.molamil.osonegro.utils;
import android.util.Log;
public class Logger {

	private static final String TAG = "[OsoNegro]";
	public static void debug(String msg) {
		Log.d(TAG, msg);
	}

	public static void info(String msg) {
		Log.i(TAG, msg);
	}

	public static void error(String msg) {
		Log.e(TAG, msg);
	}

	public static void error(String msg, Exception e) {
		Log.e(TAG, msg, e);
	}

	public static void verbose(String msg) {
		Log.v(TAG, msg);
	}
}