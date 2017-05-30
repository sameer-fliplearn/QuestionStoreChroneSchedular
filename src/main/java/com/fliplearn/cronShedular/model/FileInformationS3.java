package com.fliplearn.cronShedular.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class FileInformationS3 {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	private String fileName;
	private String fileUrl;
	private Boolean cronStatus;
	private Integer assignedCron;
	private Date createdOn;
	private Date odtCreationDate;
	private Date xhtmlCreationDate;
	private String fileUploadStatus;
	private String responseUrl;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public Boolean getCronStatus() {
		return cronStatus;
	}
	public void setCronStatus(Boolean cronStatus) {
		this.cronStatus = cronStatus;
	}
	public Integer getAssignedCron() {
		return assignedCron;
	}
	public void setAssignedCron(Integer assignedCron) {
		this.assignedCron = assignedCron;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	public Date getOdtCreationDate() {
		return odtCreationDate;
	}
	public void setOdtCreationDate(Date odtCreationDate) {
		this.odtCreationDate = odtCreationDate;
	}
	public Date getXhtmlCreationDate() {
		return xhtmlCreationDate;
	}
	public void setXhtmlCreationDate(Date xhtmlCreationDate) {
		this.xhtmlCreationDate = xhtmlCreationDate;
	}
	public String getFileUploadStatus() {
		return fileUploadStatus;
	}
	public void setFileUploadStatus(String fileUploadStatus) {
		this.fileUploadStatus = fileUploadStatus;
	}
	public String getResponseUrl() {
		return responseUrl;
	}
	public void setResponseUrl(String responseUrl) {
		this.responseUrl = responseUrl;
	}
	public FileInformationS3(String fileName, String fileUrl, Boolean cronStatus, Integer assignedCron, Date createdOn,
			Date odtCreationDate, Date xhtmlCreationDate, String fileUploadStatus, String responseUrl) {
		super();
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.cronStatus = cronStatus;
		this.assignedCron = assignedCron;
		this.createdOn = createdOn;
		this.odtCreationDate = odtCreationDate;
		this.xhtmlCreationDate = xhtmlCreationDate;
		this.fileUploadStatus = fileUploadStatus;
		this.responseUrl = responseUrl;
	}
	public FileInformationS3() {
	}
}
