package com.mpos.connection;

import com.mpos.pojo.APIData;


public class ApiResponse {

	private ApiException exception;
	private APIData data;
	private CallParams params;

	public ApiResponse(APIData res, CallParams p) {
		data = res;
		params = p;
		exception = null;
	}

	public ApiResponse(ApiException ex, CallParams p) {
		exception = ex;
		params = p;
	}

	public APIData getData() throws ApiException {
		if (exception != null) {
			throw exception;
		}

		return data;
	}

	public CallParams getParams() {
		return params;
	}

}
