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
				System.out.println("파일이 없음.");
				continue;
			}
			
			int rowindex=0;
			int columnindex=0;
			String productName="";
			
			XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
			//시트 수 (첫번째에만 존재하므로 0을 준다) 
			//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다 
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			//행의 수 
			int rows=sheet.getPhysicalNumberOfRows();
			
			
			//플랫폼별 분기 로직 -----------------------------------------------------------
			/* i값으로 플랫폼 판단
			 * i = 0  : 북큐브
			 * i = 1  : 조아라
			 * i = 2  : 카카오
			 * i = 3  : 교보문고
			 * i = 4  : 미스터블루
			 * i = 5  : 문피아
			 * i = 6  : 네이버
			 * i = 7  : 리디북스
			 * i = 8  : 로망띠끄
			 * i = 9  : 톡소다
			 * i = 10 : 원스토어
			 * i = 11 : 예스24
			 * i = 12 : 알라딘
			 * i = 13 : 이퍼브
			 */
			switch (i) {
			case 0:
				for(rowindex=3;rowindex<rows;rowindex++){
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								value="";
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
								switch (cell.getCellType()){
									case XSSFCell.CELL_TYPE_FORMULA:
										value=cell.getCellFormula();
										System.out.println("1번" + value);
										break; 
									case XSSFCell.CELL_TYPE_NUMERIC:
										if ( HSSFDateUtil.isCellDateFormatted(cell) ){
											SimpleDateFormat fommatter = new SimpleDateFormat("yyyy-MM-dd");
											value = fommatter.format(cell.getDateCellValue())+"";
											System.out.println("2번" + value);
										}else{
											// 소수점 제거됨. 해결요망
											int numeric = (int) cell.getNumericCellValue();
											value = numeric+"";
											System.out.println("3번" + value);
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=40;
						String temp = "T(";
			
						System.out.println("총 행 셀갯수 " + cells);
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
							System.out.println("현재 행번호 " + columnindex);
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
			
						for(columnindex=0;columnindex<=cells;columnindex++){
			
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
							String temp="T(";
			
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
					//행을읽는다 
					XSSFRow row=sheet.getRow(rowindex); if(row !=null){
						//셀의 수 
						int cells=row.getPhysicalNumberOfCells();
						for(columnindex=0;columnindex<=cells;columnindex++){
							
							//셀값을 읽는다 
							XSSFCell cell=row.getCell(columnindex);
							String value=""; 
							//셀이 빈값일경우를 위한 널체크
							if(cell==null){
								continue;
							}else{ 
								//타입별로 내용 읽기
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
								System.out.println("값은? " + value);
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
		
		System.out.println("이하 북네임 리스트 출력");
		for(int j=0; j<bookName.size(); j++) {
			System.out.println("북네임?" + bookName.get(j));
		}
		
		// 이하 엑셀 생성 코드-----------------------------------------------------------------------
		XSSFWorkbook objWorkBook = new XSSFWorkbook();
		String fileName = "정산별DB";

		//워크시트 생성
		XSSFSheet objSheet = objWorkBook.createSheet();

		//시트 이름
		objWorkBook.setSheetName(0 , "sheet");

		//행생성
		XSSFRow objRow = objSheet.createRow((short)0);

		//셀 생성
		XSSFCell objCell = null;


		//스타일 설정

		//스타일 객체 생성 
		XSSFCellStyle styleHd = objWorkBook.createCellStyle();    //제목 스타일
		XSSFCellStyle styleSub = objWorkBook.createCellStyle();   //상단 스타일
		XSSFCellStyle styleCon = objWorkBook.createCellStyle();   //내용 스타일
		XSSFCellStyle styleCon2 = objWorkBook.createCellStyle();   //내용 스타일2
		XSSFCellStyle styleCon3 = objWorkBook.createCellStyle();   //내용 스타일3
		XSSFCellStyle styleCon4 = objWorkBook.createCellStyle();   //내용 스타일4
		XSSFCellStyle styleBody = objWorkBook.createCellStyle();   //왼쪽 스타일
		XSSFCellStyle stylesum = objWorkBook.createCellStyle();   //소계 스타일
		XSSFCellStyle styleBottom = objWorkBook.createCellStyle();   //하단 스타일


		//제목 폰트
		XSSFFont font = objWorkBook.createFont();
		font.setFontHeightInPoints((short)15);

		//제목 스타일에 폰트 적용, 정렬
		styleHd.setFont(font);


		//상단 폰트
		XSSFFont font2 = objWorkBook.createFont();

		//상단 스타일 설정
		styleSub.setBottomBorderColor(HSSFColor.BLACK.index);
		styleSub.setLeftBorderColor(HSSFColor.BLACK.index);
		styleSub.setRightBorderColor(HSSFColor.BLACK.index);
		styleSub.setTopBorderColor(HSSFColor.BLACK.index);

		//셀에 색 넣기
		styleSub.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);

		//글자 속성
		styleSub.setFont(font2);
		styleSub.setWrapText(true); 


		//왼쪽 스타일 설정
		styleBody.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
		styleBody.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleBody.setLeftBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleBody.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleBody.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);

		//내용 스타일
		styleCon.setBottomBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleCon.setLeftBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleCon.setRightBorderColor(HSSFColor.GREY_50_PERCENT.index);
		styleCon.setTopBorderColor(HSSFColor.GREY_50_PERCENT.index);

		//엑셀 상단 열 제목
		String[] title = {"작품명", "작가", "브랜드", "정가", "담당자"};
		int length = title.length;
		
		//칼럼이름
		objRow = objSheet.createRow(0);

		for(int i=0; i<length; i++){
			objCell = objRow.createCell(i);
			objCell.setCellValue(title[i]);
			objCell.setCellStyle(styleSub);
		}
		
		//본문내용
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
		//시트 수 (첫번째에만 존재하므로 0을 준다) 
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다 
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		//행의 수 
		int rows=sheet.getPhysicalNumberOfRows();
		
		//이하 DB속성세팅 값 삽입---------------------------------------------------------
		DbSetting dbSetting = new DbSetting();
		
		for(rowindex=1;rowindex<rows;rowindex++){
			//행을읽는다 
			XSSFRow row=sheet.getRow(rowindex); if(row !=null){
				//셀의 수 
				int cells=row.getPhysicalNumberOfCells();
	
				for(columnindex=0;columnindex<=cells;columnindex++){
	
					//셀값을 읽는다 
					XSSFCell cell=row.getCell(columnindex);
					String value=""; 
	
					//셀이 빈값일경우를 위한 널체크
					if(cell==null){
						continue;
					}else{ 
						//타입별로 내용 읽기
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
				System.out.println("값은? " + dbSetting);
				dbSettingService.registerDbSetting(dbSetting);
			}
		}
		
		return "redirect:dbSettings";
	}
}
