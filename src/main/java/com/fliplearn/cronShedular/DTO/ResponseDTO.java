package com.fliplearn.cronShedular.DTO;

public class ResponseDTO {
	
	Object response;
	
	Object error;

	int responseCode;
	
	String responseStatus;
	
	String responseType;
    

	@Override
	public String toString() {
		return "ResponseDTO [response=" + response + ", error=" + error + ", responseCode=" + responseCode
				+ ", responseStatus=" + responseStatus + ", responseType=" + responseType + "]";
	}
	public ResponseDTO(){
		
	}
	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}


	public Object getError() {
		return error;
	}
	public void setError(Object error) {
		this.error = error;
	}
	
	
	
	
}
