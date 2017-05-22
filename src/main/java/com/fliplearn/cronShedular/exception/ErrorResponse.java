package com.fliplearn.cronShedular.exception;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author smruti
 *
 */
@Entity
public class ErrorResponse {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int errorId;
	
	private String errorCode;
	private String InternalMessage;
	private String userMessage;
	
	@JsonIgnore
	private String apiName;
	
	@JsonIgnore
	private boolean apiStatus;
	
	@JsonIgnore
	private Date createdDate;
	
	public int getErrorId() {
		return errorId;
	}
	public void setErrorId(int errorId) {
		this.errorId = errorId;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getInternalMessage() {
		return InternalMessage;
	}
	public void setInternalMessage(String internalMessage) {
		InternalMessage = internalMessage;
	}
	public String getUserMessage() {
		return userMessage;
	}
	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}
	
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public boolean isApiStatus() {
		return apiStatus;
	}
	public void setApiStatus(boolean apiStatus) {
		this.apiStatus = apiStatus;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@PrePersist
	  protected void onCreate() {
		 createdDate = new Date();
	  }


}
