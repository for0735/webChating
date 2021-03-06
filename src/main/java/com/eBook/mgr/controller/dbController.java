package com.eBook.mgr.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import com.eBook.mgr.domain.DbSetting;
import com.eBook.mgr.domain.platform.Bookcube;
import com.eBook.mgr.service.DbSettingService;

@Controller
@RequestMapping("/ebook")
public class dbController implements ServletContextAware{
	
	private static final Logger log = LoggerFactory.getLogger(dbController.class);

	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	DbSettingService dbSettingService;
	
	private ServletContext servletContext;
	
	@RequestMapping(value = "/dbSettings", method = RequestMethod.GET)
	public void dbSettings() {
		
	}
	
	@RequestMapping(value = "/missing", method = RequestMethod.POST)
	public String missing(@RequestParam("file") MultipartFile file[], HttpServletResponse response, HttpServletRequest request) throws Exception{
		List<String> bookName = new ArrayList<String>();
		
		for(int i=0; i<file.length; i++) {
			System.out.println("file : " + file[i].getOriginalFilename());
			String fileName = uploadExcelFile(file[i]);
			String excelPath;
			if(fileName != null) {
				System.out.println("File Name : " + fileName);
				excelPath = uploadPath + '/' + fileName ;
				System.out.println("Excel Path : " + excelPath);
			} else {
				System.out.println("????????? ??????.");
				continue;
			}
			
			int rowindex=0;
			int columnindex=0;
			String productName="";
			
			XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
			//?????? ??? (??????????????? ??????????????? 0??? ??????) 
			//?????? ??? ????????? ?????????????????? FOR?????? ????????? ???????????? 
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			//?????? ??? 
			int rows=sheet.getPhysicalNumberOfRows();
			
			
			//???????????? ?????? ?????? -----------------------------------------------------------
			/* i????????? ????????? ??????
			 * i = 0  : ?????????
			 * i = 1  : ?????????
			 * i = 2  : ?????????
			 * i = 3  : ????????????
			 * i = 4  : ???????????????
			 * i = 5  : ?????????
			 * i = 6  : ?????????
			 * i = 7  : ????????????
			 * i = 8  : ????????????
			 * i = 9  : ?????????
			 * i = 10 : ????????????
			 * i = 11 : ??????24
			 * i = 12 : ?????????
			 * i = 13 : ?????????
			 */
			switch (i) {
			case 0:
				for(rowindex=3;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								value="";
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											double numeric = (double) cell.getNumericCellValue();
											value = String.valueOf(Math.round(numeric));
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==0) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									//bookName.add(value);
								}
							}
							
						}
					}
				}
				break;
			case 1:
				for(rowindex=1;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==0) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									//bookName.add(value);
								}
							}							
						}
					}
				}
				break;
			case 2:
				for(rowindex=5;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==10) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									//bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 3:
				for(rowindex=1;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==8) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									//bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 4:
				for(rowindex=6;rowindex<rows-1;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==1) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									//bookName.add(value);
								}
							} 
						}
					}
				}
				break;
			case 5:
				for(rowindex=1;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										System.out.println("1???" + value);
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
											System.out.println("2???" + value);
										}else{
											// ????????? ?????????. ????????????
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
											System.out.println("3???" + value);
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										System.out.println(value);
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										System.out.println(value);
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										System.out.println(value);
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==2) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 6:
				for(rowindex=4;rowindex<rows-2;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==0) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 7:
				for(rowindex=1;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=40;
						String temp = "T(";
			
						System.out.println("??? ??? ????????? " + cells);
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
							System.out.println("?????? ????????? " + columnindex);
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
							
							if(value.indexOf(temp) == 0) {
								value = value.substring(3);
								value = value.substring(0, value.length()-2);
								
								System.out.println(value);
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==3) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 8:
				for(rowindex=3;rowindex<rows-1;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==2) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 9:
				for(rowindex=5;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==2) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 10:
				for(rowindex=2;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==5) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 11:
				for(rowindex=1;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==1) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 12:
				for(rowindex=1;rowindex<rows;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
							String temp="T(";
			
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
										}else{
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
										}
										break;
									case XSSFCell.CELL_TYPE_STRING:
										value=cell.getStringCellValue()+"";
										break;
									case XSSFCell.CELL_TYPE_BLANK:
										value="";
										break;
									case XSSFCell.CELL_TYPE_ERROR:
										value="";
										break;
								} 
							}
							if(value.indexOf(temp) == 0) {
								value = value.substring(3);
								value = value.substring(0, value.length()-2);
								
								System.out.println(value);
							}
			
							System.out.println("value : " + value);
							
							if (columnindex==2) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			case 13:
				for(rowindex=3;rowindex<rows+2;rowindex++){
					//??????????????? 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//?????? ??? 
						int cells=row.getPhysicalNumberOfCells();
						for(columnindex=0;columnindex<=cells;columnindex++){
							
							//????????? ????????? 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
							//?????? ?????????????????? ?????? ?????????
							if(cell==null){
								continue;
							}else{ 
								//???????????? ?????? ??????
								switch (cell.getCellType()){
								case XSSFCell.CELL_TYPE_FORMULA:
									value=cell.getCellFormula();
									break; 
								case XSSFCell.CELL_TYPE_NUMERIC:
									if ( HSSFDateUtil.isCellDateFormatted(cell) ){
										SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
										value = fommatter.format(cell.getDateCellValue())+"";
									}else{
										int numeric = (int) cell.getNumericCellValue();
										value = numeric+"";
									}
									break;
								case XSSFCell.CELL_TYPE_STRING:
									value=cell.getStringCellValue()+"";
									break;
								case XSSFCell.CELL_TYPE_BLANK:
									value="";
									break;
								case XSSFCell.CELL_TYPE_ERROR:
									value="";
									break;
								} 
							}
							
							System.out.println("value : " + value);
							
							if (columnindex==1) {
								System.out.println("??????? " + value);
								boolean checkName = dbSettingService.findDbSetting(value);
								if (checkName == false) {
									bookName.add(value);
								}
							}
						}
					}
				}
				break;
			default:
				break;
			}
		}
		
		System.out.println("?????? ????????? ????????? ??????");
		for(int j=0; j<bookName.size(); j++) {
			System.out.println("??????????" + bookName.get(j));
		}
		
		// ?????? ?????? ?????? ??????-----------------------------------------------------------------------
		XSSFWorkbook objWorkBook = new XSSFWorkbook();
		String fileName = "?????????DB";

		//???????????? ??????
		XSSFSheet objSheet = objWorkBook.createSheet();

		//?????? ??????
		objWorkBook.setSheetName(0 , "sheet");

		//?????????
		XSSFRow objRow = objSheet.createRow((short)0);

		//??? ??????
		XSSFCell objCell = null;


		//????????? ??????

		//????????? ?????? ?????? 
		XSSFCellStyle styleHd = objWorkBook.createCellStyle();    //?????? ?????????
		XSSFCellStyle styleSub = objWorkBook.createCellStyle();   //?????? ?????????
		XSSFCellStyle styleCon = objWorkBook.createCellStyle();   //?????? ?????????
		XSSFCellStyle styleCon2 = objWorkBook.createCellStyle();   //?????? ?????????2
		XSSFCellStyle styleCon3 = objWorkBook.createCellStyle();   //?????? ?????????3
		XSSFCellStyle styleCon4 = objWorkBook.createCellStyle();   //?????? ?????????4
		XSSFCellStyle styleBody = objWorkBook.createCellStyle();   //?????? ?????????
		XSSFCellStyle stylesum = objWorkBook.createCellStyle();   //?????? ?????????
		XSSFCellStyle styleBottom = objWorkBook.createCellStyle();   //?????? ?????????


		//?????? ??????
		XSSFFont font = objWorkBook.createFont();
		font.setFontHeightInPoints((short)15);

		//?????? ???????????? ?????? ??????, ??????
		styleHd.setFont(font);


		//?????? ??????
		XSSFFont font2 = objWorkBook.createFont();

		//?????? ????????? ??????
		styleSub.setBottomBorderColor(HSSFColor.BLACK.index);
		styleSub.setLeftBorderColor(HSSFColor.BLACK.index);
		styleSub.setRightBorderColor(HSSFColor.BLACK.index);
		styleSub.setTopBorderColor(HSSFColor.BLACK.index);

		//?????? ??? ??????
		styleSub.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);

		//?????? ??????
		styleSub.setFont(font2);
		styleSub.setWrapText(true); 


		//?????? ????????? ??????
		styleBody.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		styleBody.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleBody.setLeftBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleBody.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleBody.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);

		//?????? ?????????
		styleCon.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleCon.setLeftBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleCon.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleCon.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);

		//?????? ?????? ??? ??????
		String[] title = {"?????????", "??????", "?????????", "??????", "?????????"};
		int length = title.length;
		
		//????????????
		objRow = objSheet.createRow(0);

		for(int i=0; i<length; i++){
			objCell = objRow.createCell(i);
			objCell.setCellValue(title[i]);
			objCell.setCellStyle(styleSub);
		}
		
		//????????????
		for(int i=0; i<bookName.size(); i++) {
			objRow = objSheet.createRow(1+i);
			title[0] = bookName.get(i);
			title[1] = "";
			title[2] = "";
			title[3] = "";
			title[4] = "";
			
			for(int j=0; j<length; j++) {
				objCell = objRow.createCell(j);
				objCell.setCellValue(title[j]);
				objCell.setCellStyle(styleSub);
			}
		}
		
		try {
			response.setContentType("application/Msexcel");
			response.setHeader("Content-Disposition", "ATTachment; Filename="+URLEncoder.encode(fileName,"UTF-8")+".xlsx");
	
			OutputStream fileOut  = response.getOutputStream(); 
			objWorkBook.write(fileOut);
			fileOut.close();
	
			response.getOutputStream().flush();
			response.getOutputStream().close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:dbSettings";
	}
	
	private String uploadExcelFile(MultipartFile multipartFile)  {
		try {
			byte [] bytes = multipartFile.getBytes();
			Path path = Paths.get(uploadPath + '/' + multipartFile.getOriginalFilename());
			System.out.println("path : " + path.toString());
			Files.write(path, bytes);
			return multipartFile.getOriginalFilename();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	@RequestMapping(value = "/dbUpload", method = RequestMethod.POST)
	public String dbUpload(@RequestParam("file") MultipartFile file[], HttpServletRequest request) throws Exception {
		dbSettingService.removeDbSetting();
		
		System.out.println("------------------------------");
		System.out.println("file : " + file[14].getOriginalFilename());
		String fileName = uploadExcelFile(file[14]);
		System.out.println("File Name : " + fileName);
		String excelPath = uploadPath + '/' + fileName ;
		System.out.println("Excel Path : " + excelPath);
		
		int rowindex=0;
		int columnindex=0;
		
		XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
		//?????? ??? (??????????????? ??????????????? 0??? ??????) 
		//?????? ??? ????????? ?????????????????? FOR?????? ????????? ???????????? 
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		//?????? ??? 
		int rows=sheet.getPhysicalNumberOfRows();
		
		//?????? DB???????????? ??? ??????---------------------------------------------------------
		DbSetting dbSetting = new DbSetting();
		
		for(rowindex=1;rowindex<rows;rowindex++){
			//??????????????? 
			XSSFRow row=sheet.getRow(rowindex); if(row !=null){
				//?????? ??? 
				int cells=row.getPhysicalNumberOfCells();
	
				for(columnindex=0;columnindex<=cells;columnindex++){
	
					//????????? ????????? 
					XSSFCell cell=row.getCell(columnindex);
					String value=""; 
	
					//?????? ?????????????????? ?????? ?????????
					if(cell==null){
						continue;
					}else{ 
						//???????????? ?????? ??????
						switch (cell.getCellType()){
							case XSSFCell.CELL_TYPE_FORMULA:
								value=cell.getCellFormula();
								break; 
							case XSSFCell.CELL_TYPE_NUMERIC:
								if ( HSSFDateUtil.isCellDateFormatted(cell) ){
									SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
									value = fommatter.format(cell.getDateCellValue())+"";
								}else{
									int numeric = (int) cell.getNumericCellValue();
									value = numeric+"";
								}
								break;
							case XSSFCell.CELL_TYPE_STRING:
								value=cell.getStringCellValue()+"";
								break;
							case XSSFCell.CELL_TYPE_BLANK:
								value="";
								break;
							case XSSFCell.CELL_TYPE_ERROR:
								value="";
								break;
						} 
					}
	
					System.out.println("value : " + value);
					
					if (columnindex==0) {
						dbSetting.setProductName(value);
					} else if (columnindex==1) {
						dbSetting.setAuthor(value);
					} else if (columnindex==2) {
						dbSetting.setBrand(value);
					} else if (columnindex==3) {
						dbSetting.setNetPrice(value);
					} else if (columnindex==4) {
						dbSetting.setManager(value);
					}
					
				}
				System.out.println("??????? " + dbSetting);
				dbSettingService.registerDbSetting(dbSetting);
			}
		}
		
		return "redirect:dbSettings";
	}
}
