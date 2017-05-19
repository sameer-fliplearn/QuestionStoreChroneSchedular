package com.fliplearn.cronShedular.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CronServiceRead {
/*
	public static void main(String args[]) throws IOException{
		readExcel();
	}*/
	
	public static void readExcel() throws IOException {
		int indexactuall = -1;
		int indexchaptername = -1;
		String fileDownloadLocation="";
		//double status=0;
		String excelFilePath = "/home/sameer/WatcherFile/fileinformation.xlsx";
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		int countBook = 0;
		Workbook workbook = new XSSFWorkbook(inputStream);
	/*	while (countBook <= 14) {*/
			Sheet firstSheet = workbook.getSheetAt(countBook);
			Iterator<Row> iterator = firstSheet.iterator();
			while(iterator.hasNext()){
		Row fisrtRow = iterator.next();
			if(fisrtRow.getRowNum()==0)
				continue;
			Iterator<Cell> cellIterator1 = fisrtRow.cellIterator();
			while (cellIterator1.hasNext()) {
			boolean flag=false;
				String fileName = "";
				String fileLocation = "";
				Integer status=null;
				Cell cel5 = cellIterator1.next();
				cel5.getColumnIndex();
				switch (cel5.getCellType()) {
				case Cell.CELL_TYPE_STRING:
					System.out.print(cel5.getStringCellValue());
					if (cel5.getColumnIndex() == 0) {
						fileName = cel5.getStringCellValue();
					}
					if (cel5.getColumnIndex() == 1) {
						fileLocation = cel5.getStringCellValue();
					}
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					System.out.print(cel5.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (cel5.getColumnIndex() == 2) {
				   status = Integer.valueOf(cel5.getNumericCellValue()+"");
				   flag=true;	
					}
					break;
				}

			
			if(flag==true && status==0){
				continue;
			}
			else if(status!=null){
				fileDownloadLocation=fileLocation.trim()+"/"+fileName.trim();	
				
			}
			
			}
			
			
			
			
			}
		

		workbook.close();
		inputStream.close();

		countBook++;
	}


	public static String callQuestionService(String fileDownloadLocation){
		if(fileDownloadLocation.trim().isEmpty())
			return null;
		else{
			
		}
		
		return null;
	}



}
