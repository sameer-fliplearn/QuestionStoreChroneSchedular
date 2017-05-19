package com.fliplearn.cronShedular.model;

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
	private Boolean fileStatus;
	
	public FileInformationS3() {
		// TODO Auto-generated constructor stub
	}
	
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
	public Boolean getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(Boolean fileStatus) {
		this.fileStatus = fileStatus;
	}
	
}
