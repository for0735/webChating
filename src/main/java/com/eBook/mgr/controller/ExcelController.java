package com.eBook.mgr.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
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

import com.eBook.mgr.domain.platform.Aladin;
import com.eBook.mgr.domain.platform.Bookcube;
import com.eBook.mgr.domain.platform.Epub;
import com.eBook.mgr.domain.platform.Joara;
import com.eBook.mgr.domain.platform.Kakao;
import com.eBook.mgr.domain.platform.Kyobo;
import com.eBook.mgr.domain.platform.Mrblue;
import com.eBook.mgr.domain.platform.Munpia;
import com.eBook.mgr.domain.platform.Naver;
import com.eBook.mgr.domain.platform.Product;
import com.eBook.mgr.domain.platform.Ridibooks;
import com.eBook.mgr.domain.platform.Romance;
import com.eBook.mgr.domain.platform.Tocsoda;
import com.eBook.mgr.domain.platform.Winstore;
import com.eBook.mgr.domain.platform.Yes24;
import com.eBook.mgr.service.AuthService;
import com.eBook.mgr.service.EBookService;

@Controller
@RequestMapping("/ebook")
public class ExcelController implements ServletContextAware {
	
	/*
	 * 엑셀파일 업로드  84행
	 * 엑셀파일 다운로드  1748행
	 * */
	
	private static final Logger log = LoggerFactory.getLogger(ExcelController.class);
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	EBookService eBookService;
	
	@Autowired
	AuthService authService;
	
	private ServletContext servletContext;
	
	@RequestMapping(method = RequestMethod.GET)
	public String index() {
		return "ebook/index";
	}
	
	
	//엑셀 업로드 로직 - yes24 B2B 부분
	@RequestMapping(value = "/process_yes24B2B", method = RequestMethod.POST)
	public String process_yes24B2B(@RequestParam("file") MultipartFile file, String p_type, String yearText, String monthText) throws Exception {
		System.out.println("file : " + file.getOriginalFilename());
		String fileName = uploadExcelFile(file);
		System.out.println("File Name : " + fileName);
		String excelPath = uploadPath + '/' + fileName ;
		System.out.println("Excel Path : " + excelPath);
		
		int rowindex=0;
		int columnindex=0;
		
		
		//SetDate 세팅-----------------------------------------
		System.out.println("연도 :" + yearText);
		System.out.println("월 :" + monthText);
		
		if(monthText.length()==1) {
			monthText = "0" + monthText;
		}
		
		String date=yearText + "-" + monthText + "-10";
		
		
		//-----------------------------------------------------
		
		XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
		//시트 수 (첫번째에만 존재하므로 0을 준다) 
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다 
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		//행의 수 
		int rows=sheet.getPhysicalNumberOfRows();
		
		// 이상 공통적인 엑셀 파싱 코드 ---------------------------------------------------------
			
		Yes24 yes24 = new Yes24();
		yes24.setSetDate(date);
		
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
						yes24.setNo(value + " (B2B)");
					} else if (columnindex==1) {
						yes24.setSaleDate(value + " (발주일)");
					} else if (columnindex==2) {
						yes24.setSales(value);
					} else if (columnindex==3) {
						yes24.setProductName(value);
					} else if (columnindex==4) {
						yes24.setAuthor(value);
					} else if (columnindex==5) {
						yes24.setBrand(value);
					} else if (columnindex==6) {
					} else if (columnindex==7) {
					} else if (columnindex==8) {
						yes24.setEpublId(value);
					} else if (columnindex==9) {
						yes24.setPaperbookIsbn(value);
					} else if (columnindex==10) {
						yes24.setEbookIsbn(value);
					} else if (columnindex==11) {
						yes24.setCostRate(value);
					} else if (columnindex==12) {
					} else if (columnindex==13) {
						yes24.setBrandSalePrice(value);
					} else if (columnindex==14) {
						yes24.setPayment(value);
					} 
					
					
				}
				String author = eBookService.readAuthor(yes24.getProductName());
				log.info("작가명??? : " + author);
				yes24.setAuthor(author);
				String writerId	= eBookService.readWriterId(yes24.getAuthor());
				log.info("작가??? : " + writerId);
				yes24.setWriterId(writerId);
				System.out.println("값은? " + yes24);
				eBookService.registerYes24(yes24);
			}
		}

		
		return "redirect:/";
	}
	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public String process(@RequestParam("file") MultipartFile file, String p_type, String yearText, String monthText) throws Exception {
		System.out.println("file : " + file.getOriginalFilename());
		String fileName = uploadExcelFile(file);
		System.out.println("File Name : " + fileName);
		String excelPath = uploadPath + '/' + fileName ;
		System.out.println("Excel Path : " + excelPath);
		
		int rowindex=0;
		int columnindex=0;
		
		
		//SetDate 세팅-----------------------------------------
		System.out.println("연도 :" + yearText);
		System.out.println("월 :" + monthText);
		
		if(monthText.length()==1) {
			monthText = "0" + monthText;
		}
		
		String date=yearText + "-" + monthText + "-10";
		
		
		//-----------------------------------------------------
		
		XSSFWorkbook workbook = new XSSFWorkbook(excelPath);
		//시트 수 (첫번째에만 존재하므로 0을 준다) 
		//만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다 
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		//행의 수 
		int rows=sheet.getPhysicalNumberOfRows();
		
		// 이상 공통적인 엑셀 파싱 코드 ---------------------------------------------------------
		
		
		
		
		
		
		// 이하 플랫폼별 분기처리 로직 ---------------------------------------------------------
		
		switch (p_type) {
		case "p_bookcube":		
			Bookcube bookcube = new Bookcube();
			bookcube.setSetDate(date);
			
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
							bookcube.setProductName(value);
						} else if (columnindex==1) {
							bookcube.setAuthor(value);
						} else if (columnindex==2) {
							bookcube.setBrand(value);
						} else if (columnindex==3) {
							bookcube.setSalesCategory(value);
						} else if (columnindex==4) {
							bookcube.setSalesType(value);
						} else if (columnindex==5) {
							bookcube.setEpisode(value);
						} else if (columnindex==6) {
							bookcube.setNetPrice(value);
						} else if (columnindex==7) {
							bookcube.setSalePrice(value);
						} else if (columnindex==8) {
							bookcube.setDiscount(value);
						} else if (columnindex==9) {
							bookcube.setFee(value);
						} else if (columnindex==10) {
							bookcube.setNetTargetPrice(value);
						} else if (columnindex==11) {
							bookcube.setPayment(value);
						} else if (columnindex==12) {
							bookcube.setBookId(value);
						} else if (columnindex==13) {
							bookcube.setIsbn(value);
						} else if (columnindex==14) {
							bookcube.setEIsbn(value);
						} else if (columnindex==15) {
							bookcube.setEIsbnEpub(value);
						} 
						
						
					}
					String brand = eBookService.readBrand(bookcube.getProductName());
					String author = eBookService.readAuthor(bookcube.getProductName());
					log.info("작가명??? : " + author);
					bookcube.setAuthor(author);
					log.info("브랜드??? : " + brand);
					String writerId	= eBookService.readWriterId(bookcube.getAuthor());
					log.info("작가??? : " + writerId);
					bookcube.setBrand(brand);
					bookcube.setWriterId(writerId);
					System.out.println("값은? " + bookcube);
					eBookService.registerBookcube(bookcube);
				}
			}
			break;
		case "p_epub":		
			Epub epub = new Epub();
			epub.setSetDate(date);
			
			for(rowindex=3;rowindex<rows+2;rowindex++){
				//행을읽는다 
				XSSFRow row=sheet.getRow(rowindex); if(row !=null){
					//셀의 수 
					int cells=row.getPhysicalNumberOfCells();
		
					for(columnindex=0;columnindex<=cells+2;columnindex++){
		
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
							epub.setIndexNum(value);
						} else if (columnindex==1) {
							epub.setProductName(value);
						} else if (columnindex==2) {
							epub.setPublisher(value);
						} else if (columnindex==3) {
							epub.setPublisherAmount(value);
						} else if (columnindex==4) {
							epub.setSales(value);
						} else if (columnindex==5) {
							epub.setSalesAmount(value);
						} else if (columnindex==6) {
							epub.setCencelsAmount(value);
						} else if (columnindex==7) {
							epub.setCostRate(value);
						} else if (columnindex==8) {
							epub.setPayment(value);
						} else if (columnindex==9) {
							epub.setEventType(value);
						} else if (columnindex==10) {
							epub.setSalesType(value);
						} else if (columnindex==11) {
							epub.setEventAmount(value);
						} else if (columnindex==12) {
							epub.setAuthor(value);
						} else if (columnindex==13) {
							epub.setEpubid(value);
						} else if (columnindex==14) {
							epub.setSetCode(value);
						} else if (columnindex==15) {
							epub.setPaperbookIsbn(value);
						} else if (columnindex==16) {
							epub.setEbookIsbn(value);
						} else if (columnindex==17) {
							epub.setSalseDate(value);
						} else if (columnindex==18) {
							epub.setCencelsDate(value);
						}
						
						
					}
					String brand = eBookService.readBrand(epub.getProductName());
					String author = eBookService.readAuthor(epub.getProductName());
					log.info("작가명??? : " + author);
					epub.setAuthor(author);
					log.info("브랜드??? : " + brand);
					String writerId	= eBookService.readWriterId(epub.getAuthor());
					log.info("작가??? : " + writerId);
					epub.setBrand(brand);
					epub.setWriterId(writerId);
					System.out.println("값은? " + epub);
					eBookService.registerEpub(epub);
				}
			}
			break;
		case "p_joara":
			Joara joara = new Joara();
			joara.setSetDate(date);
			
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
							joara.setProductName(value);
						} else if (columnindex==1) {
							joara.setProductCode(value);
						} else if (columnindex==2) {
							value = value.replaceAll("\\[", "");
							value = value.replaceAll("\\]", "");
							joara.setAuthor(value);
						} else if (columnindex==3) {
							joara.setUnit(value);
						} else if (columnindex==4) {
							joara.setSaleAmount(value);
						} else if (columnindex==5) {
							joara.setRatio(value);
						} else if (columnindex==6) {
							joara.setPayment(value);
						} else if (columnindex==7) {
							joara.setPaymentDate(value);
						} 
						
						
					}
					log.info("======================================");
					System.out.println(joara);
					String brand = eBookService.readBrand(joara.getProductName());
					String author = eBookService.readAuthor(joara.getProductName());
					log.info("작가명??? : " + author);
					joara.setAuthor(author);
					log.info("브랜드??? : " + brand);
					String writerId	= eBookService.readWriterId(joara.getAuthor());
					log.info("작가??? : " + writerId);
					joara.setBrand(brand);
					joara.setWriterId(writerId);
					System.out.println("값은? " + joara);
					eBookService.registerJoara(joara);
				}
			}
			break;
		case "p_kakao":
			Kakao kakao = new Kakao();
			kakao.setSetDate(date);
			
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
							kakao.setSpecialGeneral2(value);
						} else if (columnindex==1) {
							kakao.setEntrepreneur(value);
						} else if (columnindex==2) {
							kakao.setEntrepreneurId(value);
						} else if (columnindex==3) {
							kakao.setPublisher(value);
						} else if (columnindex==4) {
							kakao.setContractUid(value);
						} else if (columnindex==5) {
							kakao.setContractCategory(value);
						} else if (columnindex==6) {
							kakao.setContract(value);
						} else if (columnindex==7) {
							kakao.setNewContract(value);
						} else if (columnindex==8) {
							kakao.setCategory(value);
						} else if (columnindex==9) {
							kakao.setSeriesId(value);
						} else if (columnindex==10) {
							kakao.setProductName(value);
						} else if (columnindex==11) {
							kakao.setProductCode(value);
						} else if (columnindex==12) {
							kakao.setLable(value);
						} else if (columnindex==13) {
							kakao.setAuthor(value);
						} else if (columnindex==14) {
							kakao.setIsIsbn(value);
						} else if (columnindex==15) {
							kakao.setCpSettlementRateAndroid(value);
						} else if (columnindex==16) {
							kakao.setCpSettlementRateIos(value);
						} else if (columnindex==17) {
							kakao.setUseTicketType(value);
						} else if (columnindex==18) {
							kakao.setUseTicketAmount(value);
						} else if (columnindex==19) {
							kakao.setCacheSum(value);
						} else if (columnindex==20) {
							kakao.setCpp1(value);
						} else if (columnindex==21) {
							kakao.setCpl1(value);
						} else if (columnindex==22) {
							kakao.setCp1_1(value);
						} else if (columnindex==23) {
							kakao.setCp2_1(value);
						} else if (columnindex==24) {
							kakao.setCp3_1(value);
						} else if (columnindex==25) {
							kakao.setCp7_1(value);
						} else if (columnindex==26) {
							kakao.setCp8_1(value);
						} else if (columnindex==27) {
							kakao.setCp9_1(value);
						} else if (columnindex==28) {
							kakao.setEventCache1(value);
						} else if (columnindex==29) {
							kakao.setOriginalSum(value);
						} else if (columnindex==30) {
							kakao.setCpp2(value);
						} else if (columnindex==31) {
							kakao.setCpl2(value);
						} else if (columnindex==32) {
							kakao.setCp1_2(value);
						} else if (columnindex==33) {
							kakao.setCp2_2(value);
						} else if (columnindex==34) {
							kakao.setCp3_2(value);
						} else if (columnindex==35) {
							kakao.setCp7_2(value);
						} else if (columnindex==36) {
							kakao.setCp8_2(value);
						} else if (columnindex==37) {
							kakao.setCp9_2(value);
						} else if (columnindex==38) {
							kakao.setEventCache2(value);
						} else if (columnindex==39) {
							kakao.setValueSupply(value);
						} else if (columnindex==40) {
							kakao.setTaxAmount(value);
						} else if (columnindex==41) {
							kakao.setPayment(value);
						} 
						
						
					}
					String brand = eBookService.readBrand(kakao.getProductName());
					String author = eBookService.readAuthor(kakao.getProductName());
					log.info("작가명??? : " + author);
					kakao.setAuthor(author);
					log.info("브랜드??? : " + brand);
					String writerId	= eBookService.readWriterId(kakao.getAuthor());
					log.info("작가??? : " + writerId);
					kakao.setBrand(brand);
					kakao.setWriterId(writerId);
					System.out.println("값은? " + kakao);
					eBookService.registerKakao(kakao);
				}
			}
			break;
		case "p_kyobo":
			Kyobo kyobo = new Kyobo();
			kyobo.setSetDate(date);
			
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
							kyobo.setSalesCategory(value);
						} else if (columnindex==1) {
							kyobo.setSalesType(value);
						} else if (columnindex==2) {
							kyobo.setProduct(value);
						} else if (columnindex==3) {
							kyobo.setMainSection(value);
						} else if (columnindex==4) {
							kyobo.setBookBarcode(value);
						} else if (columnindex==5) {
							kyobo.setIsbn(value);
						} else if (columnindex==6) {
							kyobo.setEpubIsbn(value);
						} else if (columnindex==7) {
							kyobo.setPdfIsbn(value);
						} else if (columnindex==8) {
							kyobo.setProductName(value);
						} else if (columnindex==9) {
							kyobo.setEpisode(value);
						} else if (columnindex==10) {
							kyobo.setAuthor(value);
						} else if (columnindex==11) {
							kyobo.setNetPrice(value);
						} else if (columnindex==12) {
							kyobo.setSalePrice(value);
						} else if (columnindex==13) {
							kyobo.setSaleRate(value);
						} else if (columnindex==14) {
							kyobo.setDownload(value);
						} else if (columnindex==15) {
							kyobo.setNetTargetPrice(value);
						} else if (columnindex==16) {
							kyobo.setNetTargetPriceAmount(value);
						} else if (columnindex==17) {
							kyobo.setPayment(value);
						} else if (columnindex==18) {
							kyobo.setAccountingRates(value);
						} else if (columnindex==18) {
							kyobo.setBookNumber(value);
						} 
						
						
					}
					String brand = eBookService.readBrand(kyobo.getProductName());
					String author = eBookService.readAuthor(kyobo.getProductName());
					log.info("작가명??? : " + author);
					kyobo.setAuthor(author);
					log.info("브랜드??? : " + brand);
					String writerId	= eBookService.readWriterId(kyobo.getAuthor());
					log.info("작가??? : " + writerId);
					kyobo.setBrand(brand);
					kyobo.setWriterId(writerId);
					System.out.println("값은? " + kyobo);
					eBookService.registerKyobo(kyobo);
				}
			}
			break;
		case "p_mrblue":
			Mrblue mrblue = new Mrblue();
			mrblue.setSetDate(date);
			
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
							mrblue.setDate(value);
						} else if (columnindex==1) {
							mrblue.setProductName(value);
						} else if (columnindex==2) {
							mrblue.setAuthor(value);
						} else if (columnindex==3) {
							mrblue.setCode(value);
						} else if (columnindex==4) {
							mrblue.setFixedNum(value);
						} else if (columnindex==5) {
							mrblue.setFixedSales(value);
						} else if (columnindex==6) {
							mrblue.setFixedPayment(value);
						} else if (columnindex==7) {
							mrblue.setAAppOnerentalNum(value);
						} else if (columnindex==8) {
							mrblue.setAAppOnerentalUse(value);
						} else if (columnindex==9) {
							mrblue.setAAppOnerentalPayment(value);
						} else if (columnindex==10) {
							mrblue.setAAppOnemngrNum(value);
						} else if (columnindex==11) {
							mrblue.setAAppOnemngrUse(value);
						} else if (columnindex==12) {
							mrblue.setAAppOnemngrPayment(value);
						} else if (columnindex==13) {
							mrblue.setAAppAllrentalNum(value);
						} else if (columnindex==14) {
							mrblue.setAAppAllrentalUse(value);
						} else if (columnindex==15) {
							mrblue.setAAppAllrentalPayment(value);
						} else if (columnindex==16) {
							mrblue.setAAppAllmngrNum(value);
						} else if (columnindex==17) {
							mrblue.setAAppAllmngrUse(value);
						} else if (columnindex==18) {
							mrblue.setAAppAllmngrPayment(value);
						} else if (columnindex==19) {
							mrblue.setIAppOnerentalNum(value);
						} else if (columnindex==20) {
							mrblue.setIAppOnerentalUse(value);
						} else if (columnindex==21) {
							mrblue.setIAppOnerentalPayment(value);
						} else if (columnindex==22) {
							mrblue.setIAppOnemngrNum(value);
						} else if (columnindex==23) {
							mrblue.setIAppOnemngrUse(value);
						} else if (columnindex==24) {
							mrblue.setIAppOnemngrPayment(value);
						} else if (columnindex==25) {
							mrblue.setIAppAllrentalNum(value);
						} else if (columnindex==26) {
							mrblue.setIAppAllrentalUse(value);
						} else if (columnindex==27) {
							mrblue.setIAppAllrentalPayment(value);
						} else if (columnindex==28) {
							mrblue.setIAppAllmngrNum(value);
						} else if (columnindex==29) {
							mrblue.setIAppAllmngrUse(value);
						} else if (columnindex==30) {
							mrblue.setIAppAllmngrPayment(value);
						} else if (columnindex==31) {
							mrblue.setAmountNum(value);
						} else if (columnindex==32) {
							mrblue.setAmountUse(value);
						} else if (columnindex==33) {
							mrblue.setAmountPayment(value);
						} else if (columnindex==34) {
							mrblue.setAmountSettlement(value);
						} else if (columnindex==35) {
							mrblue.setSales(value);
						} else if (columnindex==36) {
							mrblue.setPayment(value);
						} 
						
						
					}
					String brand = eBookService.readBrand(mrblue.getProductName());
					String author = eBookService.readAuthor(mrblue.getProductName());
					log.info("작가명??? : " + author);
					mrblue.setAuthor(author);
					log.info("브랜드??? : " + brand);
					String writerId	= eBookService.readWriterId(mrblue.getAuthor());
					log.info("작가??? : " + writerId);
					mrblue.setBrand(brand);
					mrblue.setWriterId(writerId);
					System.out.println("값은? " + mrblue);
					eBookService.registerMrblue(mrblue);
				}
			}

			break;
		case "p_munpia":
			Munpia munpia = new Munpia();
			munpia.setSetDate(date);
			
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
							value="";
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
										double numeric = (double) cell.getNumericCellValue();
										value = String.valueOf(Math.round(numeric));
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
						
						if (columnindex==0) {
							munpia.setDate(value);
						} else if (columnindex==1) {
							munpia.setClassification(value);
						} else if (columnindex==2) {
							munpia.setProductName(value);
						} else if (columnindex==3) {
							munpia.setPurchase(value);
						} else if (columnindex==4) {
							munpia.setPurchaseCancel(value);
						} else if (columnindex==5) {
							munpia.setRental(value);
						} else if (columnindex==6) {
							munpia.setRentalCencel(value);
						} else if (columnindex==7) {
							munpia.setTotalSales(value);
						} else if (columnindex==8) {
							munpia.setTotalSalesCencel(value);
						} else if (columnindex==9) {
							munpia.setRealSale(value);
						} else if (columnindex==10) {
							munpia.setSubPayment(value);
						} else if (columnindex==11) {
							munpia.setIos(value);
						} 
					}
					String brand = eBookService.readBrand(munpia.getProductName());
					String author = eBookService.readAuthor(munpia.getProductName());
					log.info("작가??? : " + author);
					munpia.setAuthor(author);
					String writerId	= eBookService.readWriterId(munpia.getAuthor());
					log.info("브랜드??? : " + brand);
					log.info("작가??? : " + writerId);
					munpia.setBrand(brand);
					munpia.setWriterId(writerId);
					munpia.setPayment(Integer.toString(Integer.parseInt(munpia.getSubPayment()) + Integer.parseInt(munpia.getIos())));
					System.out.println("값은? " + munpia);
					eBookService.registerMunpia(munpia);
				}
			}
			break;
		case "p_naver":
			Naver naver = new Naver();
			naver.setSetDate(date);
			
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
							naver.setProductName(value);
						} else if (columnindex==1) {
							naver.setProductNo(value);
						} else if (columnindex==2) {
							naver.setProviderCode(value);
						} else if (columnindex==3) {
							naver.setCpName(value);
						} else if (columnindex==4) {
							naver.setAuthor(value);
						} else if (columnindex==5) {
							naver.setBrand(value);
						} else if (columnindex==6) {
							naver.setAgeLimit(value);
						} else if (columnindex==7) {
							naver.setServiceDay(value);
						} else if (columnindex==8) {
							naver.setRentalNumber(value);
						} else if (columnindex==9) {
							naver.setRentalNonpy(value);
						} else if (columnindex==10) {
							naver.setRentalFree(value);
						} else if (columnindex==11) {
							naver.setRentalPaidCookie(value);
						} else if (columnindex==12) {
							naver.setRentalFreeCookie(value);
						} else if (columnindex==13) {
							naver.setRentalPaidICookie(value);
						} else if (columnindex==14) {
							naver.setRentalFreeICookie(value);
						} else if (columnindex==15) {
							naver.setOwnNumber(value);
						} else if (columnindex==16) {
							naver.setOwnNonpy(value);
						} else if (columnindex==17) {
							naver.setOwnFree(value);
						} else if (columnindex==18) {
							naver.setOwnPaidCookie(value);
						} else if (columnindex==19) {
							naver.setOwnFreeCookie(value);
						} else if (columnindex==20) {
							naver.setOwnPaidICookie(value);
						} else if (columnindex==21) {
							naver.setOwnFreeICookie(value);
						} else if (columnindex==22) {
							naver.setCookieRentalNumber(value);
						} else if (columnindex==23) {
							naver.setCookieRentalPaid(value);
						} else if (columnindex==24) {
							naver.setCookieRentalFree(value);
						} else if (columnindex==25) {
							naver.setCookieOwnNumber(value);
						} else if (columnindex==26) {
							naver.setCookieOwnPaid(value);
						} else if (columnindex==27) {
							naver.setCookieOwnFree(value);
						} else if (columnindex==28) {
							naver.setICookieRentalNumber(value);
						} else if (columnindex==29) {
							naver.setICookieRentalPaid(value);
						} else if (columnindex==30) {
							naver.setICookieRentalFree(value);
						} else if (columnindex==31) {
							naver.setICookieOwnNumber(value);
						} else if (columnindex==32) {
							naver.setICookieOwnPaid(value);
						} else if (columnindex==33) {
							naver.setICookieOwnFree(value);
						} else if (columnindex==34) {
							naver.setICookieFee(value);
						} else if (columnindex==35) {
							naver.setSum(value);
						} else if (columnindex==36) {
						} 
					}
					
					int payment = Integer.parseInt(naver.getSum()) - Integer.parseInt(naver.getICookieFee());
					payment = (int) ((int)payment * 0.7);
					
					System.out.println(payment);
					
					naver.setPayment(Integer.toString(payment));
					
					String author = eBookService.readAuthor(naver.getProductName());
					log.info("작가명??? : " + author);
					naver.setAuthor(author);
					String writerId	= eBookService.readWriterId(naver.getAuthor());
					log.info("작가??? : " + writerId);
					naver.setWriterId(writerId);
					
					System.out.println("값은? " + naver);
					eBookService.registerNaver(naver);
				}
			}
			break;
		case "p_ridibooks":
			Ridibooks ridibooks = new Ridibooks();
			ridibooks.setSetDate(date);
			
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
										log.info("값?" + cell.getNumericCellValue());
										double numeric = (double) cell.getNumericCellValue();
										//value = String.valueOf(Math.round(numeric));
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
						
						if(value.indexOf(temp) == 0) {
							value = value.substring(3);
							value = value.substring(0, value.length()-2);
							
							System.out.println(value);
						}
		
						System.out.println("value : " + value);
						
						if (columnindex==0) {
							ridibooks.setSales(value);
						} else if (columnindex==1) {
							ridibooks.setSeriesId(value);
						} else if (columnindex==2) {
							ridibooks.setBookId(value);
						} else if (columnindex==3) {
							ridibooks.setProductName(value);
						} else if (columnindex==4) {
							ridibooks.setEpisode(value);
						} else if (columnindex==5) {
							ridibooks.setAuthor(value);
						} else if (columnindex==6) {
							ridibooks.setTranslators(value);
						} else if (columnindex==7) {
							ridibooks.setPicture(value);
						} else if (columnindex==8) {
							ridibooks.setBrand(value);
						} else if (columnindex==9) {
							ridibooks.setSeriesName(value);
						} else if (columnindex==10) {
							ridibooks.setEbooksPrice(value);
						} else if (columnindex==11) {
							ridibooks.setSingleSaleSales(value);
						} else if (columnindex==12) {
							ridibooks.setSingleSaleTicket(value);
						} else if (columnindex==13) {
							ridibooks.setSingleFreeTicket(value);
						} else if (columnindex==14) {
							ridibooks.setRentalAmount(value);
						} else if (columnindex==15) {
							ridibooks.setRentalTicket(value);
						} else if (columnindex==16) {
							ridibooks.setRentalFreeTicket(value);
						} else if (columnindex==17) {
							ridibooks.setFreeDownAmount(value);
						} else if (columnindex==18) {
							ridibooks.setFreeDownTicket(value);
						} else if (columnindex==19) {
							ridibooks.setSetSaleAmount(value);
						} else if (columnindex==20) {
							ridibooks.setSetSaleTicket(value);
						} else if (columnindex==21) {
							ridibooks.setSetRentalAmount(value);
						} else if (columnindex==22) {
							ridibooks.setSetRentalTicket(value);
						} else if (columnindex==23) {
							ridibooks.setCancelAmount(value);
						} else if (columnindex==24) {
							ridibooks.setCancelTicket(value);
						} else if (columnindex==25) {
							ridibooks.setCancelSingleSaleSales(value);
						} else if (columnindex==26) {
							ridibooks.setCancelSingleSaleTicket(value);
						} else if (columnindex==27) {
							ridibooks.setCancelRentalAmount(value);
						} else if (columnindex==28) {
							ridibooks.setCancelRentalTicket(value);
						} else if (columnindex==29) {
							ridibooks.setCancelFreeDownAmount(value);
						} else if (columnindex==30) {
							ridibooks.setCancelFreeDownTicket(value);
						} else if (columnindex==31) {
							ridibooks.setCancelSetSaleAmount(value);
						} else if (columnindex==32) {
							ridibooks.setCancelSetSaleTicket(value);
						} else if (columnindex==33) {
							ridibooks.setCancelSetRentalAmount(value);
						} else if (columnindex==34) {
							ridibooks.setCancelSetRentalTicket(value);
						} else if (columnindex==35) {
							ridibooks.setPayment(value);
						} else if (columnindex==36) {
							ridibooks.setPaperbookIsbn10(value);
						} else if (columnindex==37) {
							ridibooks.setPaperbookIsbn13(value);
						} else if (columnindex==38) {
							ridibooks.setEbookIsbn10(value);
						} else if (columnindex==39) {
							ridibooks.setEbookIsbn13(value);
						} else if (columnindex==40) {
							ridibooks.setCpManageId(value);
						} else if (columnindex==41) {
							ridibooks.setCategory1(value);
						} else if (columnindex==42) {
							ridibooks.setCategory2(value);
							System.out.println("밸류값?" + value);
						} 
						
						
					}
					String author = eBookService.readAuthor(ridibooks.getProductName());
					log.info("작가명??? : " + author);
					ridibooks.setAuthor(author);
					String writerId	= eBookService.readWriterId(ridibooks.getAuthor());
					log.info("작가??? : " + writerId);
					ridibooks.setWriterId(writerId);
					System.out.println("값은? " + ridibooks);
					eBookService.registerRidibooks(ridibooks);
				}
			}
			break;
		case "p_romance":
			Romance romance = new Romance();
			romance.setSetDate(date);
			
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
							romance.setBookCode(value);
						} else if (columnindex==1) {
							romance.setType(value);
						} else if (columnindex==2) {
							romance.setProductName(value);
						} else if (columnindex==3) {
							romance.setBrand(value);
						} else if (columnindex==4) {
							romance.setAuthor(value);
						} else if (columnindex==5) {
							romance.setSaleRate(value);
						} else if (columnindex==6) {
							romance.setInternetSales(value);
						} else if (columnindex==7) {
							romance.setPayment(value);
						} else if (columnindex==8) {
							romance.setRegDate(value);
						} else if (columnindex==9) {
							romance.setDealer(value);
						} else if (columnindex==10) {
							romance.setIsbn(value);
						} 
						
						
					}
					String author = eBookService.readAuthor(romance.getProductName());
					log.info("작가명??? : " + author);
					romance.setAuthor(author);
					String writerId	= eBookService.readWriterId(romance.getAuthor());
					log.info("작가??? : " + writerId);
					romance.setWriterId(writerId);
					System.out.println("값은? " + romance);
					eBookService.registerRomance(romance);
				}
			}
			break;
		case "p_tocsoda":
			Tocsoda tocsoda = new Tocsoda();
			tocsoda.setSetDate(date);
			
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
							tocsoda.setProductBacord(value);
						} else if (columnindex==1) {
							tocsoda.setLargeCategory(value);
						} else if (columnindex==2) {
							tocsoda.setProductName(value);
						} else if (columnindex==3) {
							tocsoda.setAuthor(value);
						} else if (columnindex==4) {
							tocsoda.setPc(value);
						} else if (columnindex==5) {
							tocsoda.setAndroid(value);
						} else if (columnindex==6) {
							tocsoda.setIos(value);
						} else if (columnindex==7) {
							tocsoda.setIosIap(value);
						} else if (columnindex==8) {
							tocsoda.setSum(value);
						} else if (columnindex==9) {
							tocsoda.setPayment(value);
						} 
						
						
					}
					String brand = eBookService.readBrand(tocsoda.getProductName());
					String author = eBookService.readAuthor(tocsoda.getProductName());
					log.info("작가명??? : " + author);
					tocsoda.setAuthor(author);
					log.info("브랜드??? : " + brand);
					String writerId	= eBookService.readWriterId(tocsoda.getAuthor());
					log.info("작가??? : " + writerId);
					tocsoda.setBrand(brand);
					tocsoda.setWriterId(writerId);
					System.out.println("값은? " + tocsoda);
					eBookService.registerTocsoda(tocsoda);
				}
			}
			break;
		case "p_winstore":
			Winstore winstore = new Winstore();
			winstore.setSetDate(date);
			
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
							winstore.setPartnerName(value);
						} else if (columnindex==1) {
							winstore.setPartnerId(value);
						} else if (columnindex==2) {
							winstore.setEntrepreneur(value);
						} else if (columnindex==3) {
							winstore.setChannelProductId(value);
						} else if (columnindex==4) {
							winstore.setChannelProductName(value);
						} else if (columnindex==5) {
							winstore.setProductName(value);
						} else if (columnindex==6) {
							winstore.setEpisode(value);
						} else if (columnindex==7) {
							winstore.setProductId(value);
						} else if (columnindex==8) {
							winstore.setSecondSettlementStatus(value);
						} else if (columnindex==9) {
							winstore.setCategory(value);
						} else if (columnindex==10) {
							winstore.setBrand(value);
						} else if (columnindex==11) {
							winstore.setAuthor(value);
						} else if (columnindex==12) {
							winstore.setPictureAuthor(value);
						} else if (columnindex==13) {
							winstore.setType(value);
						} else if (columnindex==14) {
							winstore.setSaleRate(value);
						} else if (columnindex==15) {
							winstore.setCencelRate(value);
						} else if (columnindex==16) {
							winstore.setSettlementTargetAmount(value);
						} else if (columnindex==17) {
							winstore.setSaleRate(value);
						} else if (columnindex==18) {
							winstore.setSalesAmount(value);
						} else if (columnindex==19) {
							winstore.setCencelRate(value);
						} else if (columnindex==20) {
							winstore.setCencelsAmount(value);
						} else if (columnindex==21) {
							winstore.setSum(value);
						} else if (columnindex==22) {
							winstore.setCustomerPayment(value);
						} else if (columnindex==23) {
							winstore.setCustomerPaymentReceipt(value);
						} else if (columnindex==24) {
							winstore.setCustomerPaymentEtc(value);
						} else if (columnindex==25) {
							winstore.setCustomerPaymentService(value);
						} else if (columnindex==26) {
							winstore.setCustomerPaymentServiceEtc(value);
						} else if (columnindex==27) {
							winstore.setServiceDeduction(value);
						} else if (columnindex==28) {
							winstore.setPayment(value);
						} else if (columnindex==29) {
							winstore.setPaymentType(value);
						} 
						
						
					}
					String author = eBookService.readAuthor(winstore.getProductName());
					log.info("작가명??? : " + author);
					winstore.setAuthor(author);
					String writerId	= eBookService.readWriterId(winstore.getAuthor());
					log.info("작가??? : " + writerId);
					winstore.setWriterId(writerId);
					System.out.println("값은? " + winstore);
					eBookService.registerWinstore(winstore);
				}
			}
			break;
		case "p_yes24":
			Yes24 yes24 = new Yes24();
			yes24.setSetDate(date);
			
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
							yes24.setNo(value);
						} else if (columnindex==1) {
							yes24.setProductName(value);
						} else if (columnindex==2) {
							yes24.setBrand(value);
						} else if (columnindex==3) {
							yes24.setBrandSalePrice(value);
						} else if (columnindex==4) {
							yes24.setSales(value);
						} else if (columnindex==5) {
							yes24.setSalesSalePrice(value);
						} else if (columnindex==6) {
							yes24.setSalesRefundPrice(value);
						} else if (columnindex==7) {
							yes24.setCostRate(value);
						} else if (columnindex==8) {
							yes24.setPayment(value);
						} else if (columnindex==9) {
							yes24.setEventType(value);
						} else if (columnindex==10) {
							yes24.setSaleType(value);
						} else if (columnindex==11) {
							yes24.setEventSalePrice(value);
						} else if (columnindex==12) {
							yes24.setAuthor(value);
						} else if (columnindex==13) {
							yes24.setBookId(value);
						} else if (columnindex==14) {
							yes24.setEpublId(value);
						} else if (columnindex==15) {
							yes24.setSetCode(value);
						} else if (columnindex==16) {
							yes24.setPaperbookIsbn(value);
						} else if (columnindex==17) {
							yes24.setEbookIsbn(value);
						} else if (columnindex==18) {
							yes24.setSaleDate(value);
						} else if (columnindex==19) {
							yes24.setRefundDate(value);
						} 
						
						
					}
					String author = eBookService.readAuthor(yes24.getProductName());
					log.info("작가명??? : " + author);
					yes24.setAuthor(author);
					String writerId	= eBookService.readWriterId(yes24.getAuthor());
					log.info("작가??? : " + writerId);
					yes24.setWriterId(writerId);
					System.out.println("값은? " + yes24);
					eBookService.registerYes24(yes24);
				}
			}

			break;
			
		case "p_aladin":
			Aladin aladin = new Aladin();
			aladin.setSetDate(date);
			
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
										//value = numeric+"";
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
						if(value.indexOf(temp) == 0) {
							value = value.substring(3);
							value = value.substring(0, value.length()-2);
							
							System.out.println(value);
						}
		
						System.out.println("value : " + value);
						
						if (columnindex==0) {
							aladin.setSalesCancelDate(value);
						} else if (columnindex==1) {
							aladin.setItemId(value);
						} else if (columnindex==2) {
							aladin.setProductName(value);
						} else if (columnindex==3) {
							aladin.setSalesType(value);
						} else if (columnindex==4) {
							aladin.setNetPrice(value);
						} else if (columnindex==5) {
							aladin.setSalePrice(value);
						} else if (columnindex==6) {
							aladin.setPayment(value);
						} else if (columnindex==7) {
							aladin.setCancelDate(value);
						} else if (columnindex==8) {
							aladin.setAuthor(value);
						} else if (columnindex==9) {
							aladin.setBrand(value);
						} else if (columnindex==10) {
							aladin.setPublisher(value);
						} else if (columnindex==11) {
							aladin.setIsbn(value);
						} else if (columnindex==12) {
							aladin.setCid(value);
						} 
						
						
					}
					String author = eBookService.readAuthor(aladin.getProductName());
					log.info("작가명??? : " + author);
					aladin.setAuthor(author);
					String writerId	= eBookService.readWriterId(aladin.getAuthor());
					log.info("작가??? : " + writerId);
					aladin.setWriterId(writerId);
					System.out.println("값은? " + aladin);
					eBookService.registerAladin(aladin);
				}
			}

			break;

		default:
			break;
		}

		
		// 플랫폼별 분기처리 끝 -----------------------------------------------------------------------
		
		return "redirect:/";
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
	
	//엑셀 다운로드 로직
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public String download(String p_type, HttpServletResponse response, HttpServletRequest request, String[] nowDate, Principal principal) throws Exception {
		
		String auth = authService.authRoll(principal.getName());
		
		XSSFWorkbook objWorkBook = new XSSFWorkbook();
		String fileName = "";

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

		
		//날짜세팅 -----------------------------------------------------------------------
		nowDate = request.getParameterValues("nowDate");
		String setDate = nowDate[0];
		System.out.println("현재날짜 세팅값 : " + setDate);
		
		// 이하 플랫폼별 분기처리 로직 ---------------------------------------------------------
		
		switch (p_type) {
		case "p_bookcube":
			fileName = "북큐브";
			String[] bookcube_title = {"제목","저자","출판권자","판매처","판매유형","부수","정가","판매액","할인","수수료","정산대상금액","정산액","북넘","ISBN","e-ISBN(PDF)","e-ISBN(ePub)"};
			int length = bookcube_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(2);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(bookcube_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Bookcube> bookList = null;
			if(auth.equals("ROLE_ADMIN")) {
				bookList = eBookService.listBookcube(setDate);
			} else {
				bookList = authService.listBookcube(setDate, principal.getName());
			}
			for(int i=0; i<bookList.size(); i++) {
				objRow = objSheet.createRow(3+i);
				bookcube_title[0] = bookList.get(i).getProductName();
				bookcube_title[1] = bookList.get(i).getAuthor();
				bookcube_title[2] = bookList.get(i).getBrand();
				bookcube_title[3] = bookList.get(i).getSalesCategory();
				bookcube_title[4] = bookList.get(i).getSalesType();
				bookcube_title[5] = bookList.get(i).getEpisode();
				bookcube_title[6] = bookList.get(i).getNetPrice();
				bookcube_title[7] = bookList.get(i).getSalePrice();
				bookcube_title[8] = bookList.get(i).getDiscount();
				bookcube_title[9] = bookList.get(i).getFee();
				bookcube_title[10] = bookList.get(i).getNetTargetPrice();
				bookcube_title[11] = bookList.get(i).getPayment();
				bookcube_title[12] = bookList.get(i).getBookId();
				bookcube_title[13] = bookList.get(i).getIsbn();
				bookcube_title[14] = bookList.get(i).getEIsbn();
				bookcube_title[15] = bookList.get(i).getEIsbnEpub();
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(bookcube_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			
			break;
			
		case "p_epub":
			fileName = "이퍼브";
			String[] epub_title = {"번호","도서명","출판사","출판사판매가","판매서점","서점판매가","서점환불가","원가율","출판사정산액","이벤트구분","퍈매구분","이벤트판매가","작가명","ePubID","세트코드","종이책ISBN","전자책ISBN","판매일","환불일"};
			length = epub_title.length;		
			
			//칼럼이름
			objRow = objSheet.createRow(2);
			
			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(epub_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Epub> epubList = null;
			if(auth.equals("ROLE_ADMIN")) {
				epubList = eBookService.listEpub(setDate);
			} else {
				epubList = authService.listEpub(setDate, principal.getName());
			}
			for(int i=0; i<epubList.size(); i++) {
				objRow = objSheet.createRow(3+i);
				epub_title[0] = epubList.get(i).getIndexNum();
				epub_title[1] = epubList.get(i).getProductName();
				epub_title[2] = epubList.get(i).getPublisher();
				epub_title[3] = epubList.get(i).getPublisherAmount();
				epub_title[4] = epubList.get(i).getSales();
				epub_title[5] = epubList.get(i).getSalesAmount();
				epub_title[6] = epubList.get(i).getCencelsAmount();
				epub_title[7] = epubList.get(i).getCostRate();
				epub_title[8] = epubList.get(i).getPayment();
				epub_title[9] = epubList.get(i).getEventType();
				epub_title[10] = epubList.get(i).getSalesType();
				epub_title[11] = epubList.get(i).getEventAmount();
				epub_title[12] = epubList.get(i).getAuthor();
				epub_title[13] = epubList.get(i).getEpubid();
				epub_title[14] = epubList.get(i).getSetCode();
				epub_title[15] = epubList.get(i).getPaperbookIsbn();
				epub_title[16] = epubList.get(i).getEbookIsbn();
				epub_title[17] = epubList.get(i).getSalseDate();
				epub_title[18] = epubList.get(i).getCencelsDate();
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(epub_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			
			break;
		case "p_joara":
			fileName = "조아라";
			String[] joara_title = {"작품명","작품코드","작가명","단가","판매건수","비율","정산금액","정산일"};
			length = joara_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(0);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(joara_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Joara> joaraList = null;
			if(auth.equals("ROLE_ADMIN")) {
				joaraList = eBookService.listJoara(setDate);
			} else {
				joaraList = authService.listJoara(setDate, principal.getName());
			}
			for(int i=0; i<joaraList.size(); i++) {
				objRow = objSheet.createRow(1+i);
				joara_title[0] = joaraList.get(i).getProductName();
				joara_title[1] = joaraList.get(i).getProductCode();
				joara_title[2] = joaraList.get(i).getAuthor();
				joara_title[3] = joaraList.get(i).getUnit();
				joara_title[4] = joaraList.get(i).getSaleAmount();
				joara_title[5] = joaraList.get(i).getRatio();
				joara_title[6] = joaraList.get(i).getPayment();
				joara_title[7] = joaraList.get(i).getPaymentDate();
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(joara_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			
			break;
		case "p_kakao":
			fileName = "카카오";
			String[] kakao_title = {"특별/일반","사업자명","사업자번호","발행자명","계약UID","계약유형","계약(과/면세)","당월신규계약여부","카테고리","시리즈ID","시리즈명","제품코드(발행자가 부여하는 코드)","레이블","작가명","ISBN(유/무)","CP정산율_안드로이드/이벤트캐시","CP정산율_iOS","이용권 종류","이용권 금액","캐시합계","CPP","CPI","CP1","CP2","CP3","CP7","CP8","CP9","이벤트캐시","원화합계","CPP","CPI","CP1","CP2","CP3","CP7","CP8","CP9","이벤트캐시","공급가액","세액","합계금액"};
			length = kakao_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(4);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(kakao_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Kakao> kakaoList = null;
			if(auth.equals("ROLE_ADMIN")) {
				kakaoList = eBookService.listKakao(setDate);
			} else {
				kakaoList = authService.listKakao(setDate, principal.getName());
			}
			for(int i=0; i<kakaoList.size(); i++) {
				objRow = objSheet.createRow(5+i);
				kakao_title[0] = kakaoList.get(i).getSpecialGeneral2();
				kakao_title[1] = kakaoList.get(i).getEntrepreneur();
				kakao_title[2] = kakaoList.get(i).getEntrepreneurId();
				kakao_title[3] = kakaoList.get(i).getPublisher();
				kakao_title[4] = kakaoList.get(i).getContractUid();
				kakao_title[5] = kakaoList.get(i).getContractCategory();
				kakao_title[6] = kakaoList.get(i).getContract();
				kakao_title[7] = kakaoList.get(i).getNewContract();
				kakao_title[8] = kakaoList.get(i).getCategory();
				kakao_title[9] = kakaoList.get(i).getSeriesId();
				kakao_title[10] = kakaoList.get(i).getProductName();
				kakao_title[11] = kakaoList.get(i).getProductCode();
				kakao_title[12] = kakaoList.get(i).getLable();
				kakao_title[13] = kakaoList.get(i).getAuthor();
				kakao_title[14] = kakaoList.get(i).getIsIsbn();
				kakao_title[15] = kakaoList.get(i).getCpSettlementRateAndroid();
				kakao_title[16] = kakaoList.get(i).getCpSettlementRateIos();
				kakao_title[17] = kakaoList.get(i).getUseTicketType();
				kakao_title[18] = kakaoList.get(i).getUseTicketAmount();
				kakao_title[19] = kakaoList.get(i).getCacheSum();
				kakao_title[20] = kakaoList.get(i).getCpp1();
				kakao_title[21] = kakaoList.get(i).getCpl1();
				kakao_title[22] = kakaoList.get(i).getCp1_1();
				kakao_title[23] = kakaoList.get(i).getCp2_1();
				kakao_title[24] = kakaoList.get(i).getCp3_1();
				kakao_title[25] = kakaoList.get(i).getCp7_1();
				kakao_title[26] = kakaoList.get(i).getCp8_1();
				kakao_title[27] = kakaoList.get(i).getCp9_1();
				kakao_title[28] = kakaoList.get(i).getEventCache1();
				kakao_title[29] = kakaoList.get(i).getOriginalSum();
				kakao_title[30] = kakaoList.get(i).getCpp2();
				kakao_title[31] = kakaoList.get(i).getCpl2();
				kakao_title[32] = kakaoList.get(i).getCp1_2();
				kakao_title[33] = kakaoList.get(i).getCp2_2();
				kakao_title[34] = kakaoList.get(i).getCp3_2();
				kakao_title[35] = kakaoList.get(i).getCp7_2();
				kakao_title[36] = kakaoList.get(i).getCp8_2();
				kakao_title[37] = kakaoList.get(i).getCp9_2();
				kakao_title[38] = kakaoList.get(i).getEventCache2();
				kakao_title[39] = kakaoList.get(i).getValueSupply();
				kakao_title[40] = kakaoList.get(i).getTaxAmount();
				kakao_title[41] = kakaoList.get(i).getPayment();
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(kakao_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			
			break;
		case "p_kyobo":
			fileName = "교보문고";
			String[] kyobo_title = {"판매구분","판매형태","상품","대분류","도서바코드","ISBN","EPUB ISBN","PDF ISBN","상품명","회차명","저자명","정가","판매가","판매량","다운로드수","정산대상판매가","정산대상판매총액","지불액","정산율(%)","북넘버"};
			length = kyobo_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(0);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(kyobo_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Kyobo> kyoboList = null;
			if(auth.equals("ROLE_ADMIN")) {
				kyoboList = eBookService.listkyobo(setDate);
			} else {
				kyoboList = authService.listkyobo(setDate, principal.getName());
			}
			for(int i=0; i<kyoboList.size(); i++) {
				objRow = objSheet.createRow(1+i);
				kyobo_title[0] = kyoboList.get(i).getSalesCategory();
				kyobo_title[1] = kyoboList.get(i).getSalesType();
				kyobo_title[2] = kyoboList.get(i).getProduct();
				kyobo_title[3] = kyoboList.get(i).getMainSection();
				kyobo_title[4] = kyoboList.get(i).getBookBarcode();
				kyobo_title[5] = kyoboList.get(i).getIsbn();
				kyobo_title[6] = kyoboList.get(i).getEpubIsbn();
				kyobo_title[7] = kyoboList.get(i).getPdfIsbn();
				kyobo_title[8] = kyoboList.get(i).getProductName();
				kyobo_title[9] = kyoboList.get(i).getEpisode();
				kyobo_title[10] = kyoboList.get(i).getAuthor();
				kyobo_title[11] = kyoboList.get(i).getNetPrice();
				kyobo_title[12] = kyoboList.get(i).getSalePrice();
				kyobo_title[13] = kyoboList.get(i).getSaleRate();
				kyobo_title[14] = kyoboList.get(i).getDownload();
				kyobo_title[15] = kyoboList.get(i).getNetTargetPrice();
				kyobo_title[16] = kyoboList.get(i).getNetTargetPriceAmount();
				kyobo_title[17] = kyoboList.get(i).getPayment();
				kyobo_title[18] = kyoboList.get(i).getAccountingRates();
				kyobo_title[19] = kyoboList.get(i).getBookNumber();
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(kyobo_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_mrblue":
			fileName = "미스터블루";
			String[] mrblue_title = {"일자","작품명","작가명","코드","건수","매출액","정산액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","건수","이용권(화)수","매출액","정산액","매출액","정산액"};
			length = mrblue_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(5);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(mrblue_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Mrblue> mrblueList = null;
			if(auth.equals("ROLE_ADMIN")) {
				mrblueList = eBookService.listMrblue(setDate);
			} else {
				mrblueList = authService.listMrblue(setDate, principal.getName());
			}
			for(int i=0; i<mrblueList.size(); i++) {
				objRow = objSheet.createRow(6+i);
				mrblue_title[0] = mrblueList.get(i).getDate();
				mrblue_title[1] = mrblueList.get(i).getProductName();
				mrblue_title[2] = mrblueList.get(i).getAuthor();
				mrblue_title[3] = mrblueList.get(i).getCode();
				mrblue_title[4] = mrblueList.get(i).getFixedNum();
				mrblue_title[5] = mrblueList.get(i).getFixedSales();
				mrblue_title[6] = mrblueList.get(i).getFixedPayment();
				mrblue_title[7] = mrblueList.get(i).getAAppOnerentalNum();
				mrblue_title[8] = mrblueList.get(i).getAAppOnerentalUse();
				mrblue_title[9] = mrblueList.get(i).getAAppOnerentalPayment();
				mrblue_title[10] = mrblueList.get(i).getAAppOnemngrNum();		
				mrblue_title[11] = mrblueList.get(i).getAAppOnemngrUse();		
				mrblue_title[12] = mrblueList.get(i).getAAppOnemngrPayment();	
				mrblue_title[13] = mrblueList.get(i).getAAppAllrentalNum();	
				mrblue_title[14] = mrblueList.get(i).getAAppAllrentalUse();	
				mrblue_title[15] = mrblueList.get(i).getAAppAllrentalPayment();
				mrblue_title[16] = mrblueList.get(i).getAAppAllmngrNum();		
				mrblue_title[17] = mrblueList.get(i).getAAppAllmngrUse();		
				mrblue_title[18] = mrblueList.get(i).getAAppAllmngrPayment();	
				mrblue_title[19] = mrblueList.get(i).getIAppOnerentalNum();	
				mrblue_title[20] = mrblueList.get(i).getIAppOnerentalUse();	
				mrblue_title[21] = mrblueList.get(i).getIAppOnerentalPayment();
				mrblue_title[22] = mrblueList.get(i).getIAppOnemngrNum();		
				mrblue_title[23] = mrblueList.get(i).getIAppOnemngrUse();		
				mrblue_title[24] = mrblueList.get(i).getIAppOnemngrPayment();	
				mrblue_title[25] = mrblueList.get(i).getIAppAllrentalNum();	
				mrblue_title[26] = mrblueList.get(i).getIAppAllrentalUse();	
				mrblue_title[27] = mrblueList.get(i).getIAppAllrentalPayment();
				mrblue_title[28] = mrblueList.get(i).getIAppAllmngrNum();		
				mrblue_title[29] = mrblueList.get(i).getIAppAllmngrUse();		
				mrblue_title[30] = mrblueList.get(i).getIAppAllmngrPayment();	
				mrblue_title[31] = mrblueList.get(i).getAmountNum();			
				mrblue_title[32] = mrblueList.get(i).getAmountUse();			
				mrblue_title[33] = mrblueList.get(i).getAmountPayment();		
				mrblue_title[34] = mrblueList.get(i).getAmountSettlement();	
				mrblue_title[35] = mrblueList.get(i).getSales();				
				mrblue_title[36] = mrblueList.get(i).getPayment();				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(mrblue_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			
			break;
		case "p_munpia":
			fileName = "문피아";
			String[] munpia_title = {"날짜","분류","작품","구매","구매취소","대여","대여취소","총판매","총판매취소","실판매","정산","IOS"};
			length = munpia_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(0);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(munpia_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Munpia> munpiaList = null;
			if(auth.equals("ROLE_ADMIN")) {
				munpiaList = eBookService.listMunpia(setDate);
			} else {
				munpiaList = authService.listMunpia(setDate, principal.getName());
			}
			for(int i=0; i<munpiaList.size(); i++) {
				objRow = objSheet.createRow(1+i);
				munpia_title[0] = munpiaList.get(i).getDate();
				munpia_title[1] = munpiaList.get(i).getClassification();
				munpia_title[2] = munpiaList.get(i).getProductName();
				munpia_title[3] = munpiaList.get(i).getPurchase();
				munpia_title[4] = munpiaList.get(i).getPurchaseCancel();
				munpia_title[5] = munpiaList.get(i).getRental();
				munpia_title[6] = munpiaList.get(i).getRentalCencel();
				munpia_title[7] = munpiaList.get(i).getTotalSales();
				munpia_title[8] = munpiaList.get(i).getTotalSalesCencel();
				munpia_title[9] = munpiaList.get(i).getRealSale();
				munpia_title[10] = munpiaList.get(i).getSubPayment();
				munpia_title[11] = munpiaList.get(i).getIos();
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(munpia_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_naver":
			fileName = "네이버";
			String[] naver_title = {"컨텐츠","컨텐츠No","공급자코드","CP명","작가명","출판사","나이제한","서비스일","건수","비정산","무료이용권","유상쿠키","무상쿠키","유상i쿠키","무상i쿠키","건수","비정산","무료이용권","유상쿠키","무상쿠키","유상i쿠키","무상i쿠키","건수","유상","무상","건수","유상","무상","건수","유상","무상","건수","유상","무상","i쿠피 수수료","합계"};
			length = naver_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(0);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(naver_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Naver> naverList = null;
			if(auth.equals("ROLE_ADMIN")) {
				naverList = eBookService.listNaver(setDate);
			} else {
				naverList = authService.listNaver(setDate, principal.getName());
			}
			for(int i=0; i<naverList.size(); i++) {
				objRow = objSheet.createRow(1+i);
				naver_title[0]  = naverList.get(i).getProductName();			
				naver_title[1]  = naverList.get(i).getProductNo();			
				naver_title[2]  = naverList.get(i).getProviderCode();		
				naver_title[3]  = naverList.get(i).getCpName();				
				naver_title[4]  = naverList.get(i).getAuthor();				
				naver_title[5]  = naverList.get(i).getBrand();				
				naver_title[6]  = naverList.get(i).getAgeLimit();			
				naver_title[7]  = naverList.get(i).getServiceDay();			
				naver_title[8]  = naverList.get(i).getRentalNumber();		
				naver_title[9]  = naverList.get(i).getRentalNonpy();			
				naver_title[10] = naverList.get(i).getRentalFree();			
				naver_title[11] = naverList.get(i).getRentalPaidCookie();	
				naver_title[12] = naverList.get(i).getRentalFreeCookie();	
				naver_title[13] = naverList.get(i).getRentalPaidICookie();	
				naver_title[14] = naverList.get(i).getRentalFreeICookie();	
				naver_title[15] = naverList.get(i).getOwnNumber();			
				naver_title[16] = naverList.get(i).getOwnNonpy();			
				naver_title[17] = naverList.get(i).getOwnFree();				
				naver_title[18] = naverList.get(i).getOwnPaidCookie();		
				naver_title[19] = naverList.get(i).getOwnFreeCookie();		
				naver_title[20] = naverList.get(i).getOwnPaidICookie();		
				naver_title[21] = naverList.get(i).getOwnFreeICookie();		
				naver_title[22] = naverList.get(i).getCookieRentalNumber();	
				naver_title[23] = naverList.get(i).getCookieRentalPaid();	
				naver_title[24] = naverList.get(i).getCookieRentalFree();	
				naver_title[25] = naverList.get(i).getCookieOwnNumber();		
				naver_title[26] = naverList.get(i).getCookieOwnPaid();		
				naver_title[27] = naverList.get(i).getCookieOwnFree();		
				naver_title[28] = naverList.get(i).getICookieRentalNumber();	
				naver_title[29] = naverList.get(i).getICookieRentalPaid();	
				naver_title[30] = naverList.get(i).getICookieRentalFree();	
				naver_title[31] = naverList.get(i).getICookieOwnNumber();	
				naver_title[32] = naverList.get(i).getICookieOwnPaid();		
				naver_title[33] = naverList.get(i).getICookieOwnFree();		
				naver_title[34] = naverList.get(i).getICookieFee();			
				naver_title[35] = naverList.get(i).getSum();						
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(naver_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_ridibooks":
			fileName = "리디북스";
			String[] ridibooks_title = {"판매처","시리즈 ID","도서 ID","제목","권/화수","저자","역자","그림","출판사","시리즈명","전자책정가","단권판매액","단권판매권","단권무료권","대여액","대여유료권","대여무료권","자유다운금액","자유다운권","세트판매액","세트판매권","세트대여액","세트대여권","취소액","취소권","취소단권판매액","취소단권판매권","취소대여액","취소대여권","취소자유다운액","취소자유다운권","취소세트판매액","취소세트판매권","취소세트대여액","취소세트대여권","정산액","종이책ISBN10","종이책ISBN13","전자책ISBN10","전자책ISBN13","CP 관리 ID","카테고리1","카테고리2"};
			length = ridibooks_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(0);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(ridibooks_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Ridibooks> ridibooksList = null;
			if(auth.equals("ROLE_ADMIN")) {
				ridibooksList = eBookService.listRidibooks(setDate);
			} else {
				ridibooksList = authService.listRidibooks(setDate, principal.getName());
			}
			for(int i=0; i<ridibooksList.size(); i++) {
				objRow = objSheet.createRow(1+i);
				ridibooks_title[0]  = ridibooksList.get(i).getSales(); 				
				ridibooks_title[1]  = ridibooksList.get(i).getSeriesId(); 				
				ridibooks_title[2]  = ridibooksList.get(i).getBookId(); 					
				ridibooks_title[3]  = ridibooksList.get(i).getProductName(); 			
				ridibooks_title[4]  = ridibooksList.get(i).getEpisode(); 				
				ridibooks_title[5]  = ridibooksList.get(i).getAuthor(); 					
				ridibooks_title[6]  = ridibooksList.get(i).getTranslators(); 			
				ridibooks_title[7]  = ridibooksList.get(i).getPicture(); 				
				ridibooks_title[8]  = ridibooksList.get(i).getBrand(); 					
				ridibooks_title[9]  = ridibooksList.get(i).getSeriesName(); 				
				ridibooks_title[10]  = ridibooksList.get(i).getEbooksPrice(); 			
				ridibooks_title[11] = ridibooksList.get(i).getSingleSaleSales(); 		
				ridibooks_title[12] = ridibooksList.get(i).getSingleSaleTicket(); 		
				ridibooks_title[13] = ridibooksList.get(i).getSingleFreeTicket(); 		
				ridibooks_title[14] = ridibooksList.get(i).getRentalAmount(); 			
				ridibooks_title[15] = ridibooksList.get(i).getRentalTicket(); 			
				ridibooks_title[16] = ridibooksList.get(i).getRentalFreeTicket();	
				ridibooks_title[17] = ridibooksList.get(i).getFreeDownAmount(); 			
				ridibooks_title[18] = ridibooksList.get(i).getFreeDownTicket(); 			
				ridibooks_title[19] = ridibooksList.get(i).getSetSaleAmount(); 			
				ridibooks_title[20] = ridibooksList.get(i).getSetSaleTicket(); 			
				ridibooks_title[21] = ridibooksList.get(i).getSetRentalAmount(); 		
				ridibooks_title[22] = ridibooksList.get(i).getSetRentalTicket(); 		
				ridibooks_title[23] = ridibooksList.get(i).getCancelAmount(); 			
				ridibooks_title[24] = ridibooksList.get(i).getCancelTicket(); 			
				ridibooks_title[25] = ridibooksList.get(i).getCancelSingleSaleSales(); 	
				ridibooks_title[26] = ridibooksList.get(i).getCancelSingleSaleTicket(); 	
				ridibooks_title[27] = ridibooksList.get(i).getCancelRentalAmount(); 		
				ridibooks_title[28] = ridibooksList.get(i).getCancelRentalTicket(); 		
				ridibooks_title[29] = ridibooksList.get(i).getCancelFreeDownAmount(); 	
				ridibooks_title[30] = ridibooksList.get(i).getCancelFreeDownTicket(); 	
				ridibooks_title[31] = ridibooksList.get(i).getCancelSetSaleAmount(); 	
				ridibooks_title[32] = ridibooksList.get(i).getCancelSetSaleTicket(); 	
				ridibooks_title[33] = ridibooksList.get(i).getCancelSetRentalAmount(); 	
				ridibooks_title[34] = ridibooksList.get(i).getCancelSetRentalTicket(); 	
				ridibooks_title[35] = ridibooksList.get(i).getPayment(); 				
				ridibooks_title[36] = ridibooksList.get(i).getPaperbookIsbn10(); 		
				ridibooks_title[37] = ridibooksList.get(i).getPaperbookIsbn13(); 		
				ridibooks_title[38] = ridibooksList.get(i).getEbookIsbn10(); 			
				ridibooks_title[39] = ridibooksList.get(i).getEbookIsbn13(); 			
				ridibooks_title[40] = ridibooksList.get(i).getCpManageId(); 				
				ridibooks_title[41] = ridibooksList.get(i).getCategory1(); 				
				ridibooks_title[42] = ridibooksList.get(i).getCategory2(); 				
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(ridibooks_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_romance":
			fileName = "로망띠끄";
			String[] romance_title = {"도서코드","타입","도서명","출판사명","저자","판매수","인터넷판매액","출판사정산액","등록일","판매처","isbn"};
			length = romance_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(2);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(romance_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Romance> romanceList = null;
			if(auth.equals("ROLE_ADMIN")) {
				romanceList = eBookService.listRomance(setDate);
			} else {
				romanceList = authService.listRomance(setDate, principal.getName());
			}
			for(int i=0; i<romanceList.size(); i++) {
				objRow = objSheet.createRow(3+i);
				romance_title[0] = romanceList.get(i).getBookCode();		
				romance_title[1] = romanceList.get(i).getType();		
				romance_title[2] = romanceList.get(i).getProductName();		
				romance_title[3] = romanceList.get(i).getBrand();			
				romance_title[4] = romanceList.get(i).getAuthor();			
				romance_title[5] = romanceList.get(i).getSaleRate();		
				romance_title[6] = romanceList.get(i).getInternetSales();	
				romance_title[7] = romanceList.get(i).getPayment();			
				romance_title[8] = romanceList.get(i).getRegDate();			
				romance_title[9] = romanceList.get(i).getDealer();			
				romance_title[10] = romanceList.get(i).getIsbn();			
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(romance_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_tocsoda":
			fileName = "톡소다";
			String[] tocsoda_title = {"상품바코드","대분류","작품명","저자명","판매금액","PC","모바일 웹/앱","IOS-IAP","합계","정산액"};
			length = tocsoda_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(4);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(tocsoda_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Tocsoda> tocsodaList = null;
			if(auth.equals("ROLE_ADMIN")) {
				tocsodaList = eBookService.listTocsoda(setDate);
			} else {
				tocsodaList = authService.listTocsoda(setDate, principal.getName());
			}
			for(int i=0; i<tocsodaList.size(); i++) {
				objRow = objSheet.createRow(5+i);
				tocsoda_title[0] = tocsodaList.get(i).getProductBacord();					
				tocsoda_title[1] = tocsodaList.get(i).getLargeCategory();			
				tocsoda_title[2] = tocsodaList.get(i).getProductName();					
				tocsoda_title[3] = tocsodaList.get(i).getAuthor();						
				tocsoda_title[4] = tocsodaList.get(i).getPc();						
				tocsoda_title[5] = tocsodaList.get(i).getAndroid();						
				tocsoda_title[6] = tocsodaList.get(i).getIos();						
				tocsoda_title[7] = tocsodaList.get(i).getIosIap();						
				tocsoda_title[8] = tocsodaList.get(i).getSum();						
				tocsoda_title[9] = tocsodaList.get(i).getPayment();						
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(tocsoda_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_winstore":
			fileName = "원스토어";
			String[] winstore_title = {"파트너명","파트너ID","사업자구분","채널상품ID","채널상품명","상품명","회차","상품ID","2차정산여부","카테고리","출판사","글작가","그림작가","타입","판매건수","취소건수","정액제 정산대상액","판매건수","판매금액","취소건수","취소금액","합계","고객결제","고객결제(현금영수증)","쿠폰/포인트등","고객결제 서비스이용료","쿠폰/포인트등","차감","정산지급","정산유형"};
			length = winstore_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(1);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(winstore_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Winstore> winstoreList = null;
			if(auth.equals("ROLE_ADMIN")) {
				winstoreList = eBookService.listWinstore(setDate);
			} else {
				winstoreList = authService.listWinstore(setDate, principal.getName());
			}
			for(int i=0; i<winstoreList.size(); i++) {
				objRow = objSheet.createRow(2+i);
				winstore_title[0]  = winstoreList.get(i).getPartnerName();					
				winstore_title[1]  = winstoreList.get(i).getPartnerId();					
				winstore_title[2]  = winstoreList.get(i).getEntrepreneur();				
				winstore_title[3]  = winstoreList.get(i).getChannelProductId();			
				winstore_title[4]  = winstoreList.get(i).getChannelProductName();			
				winstore_title[5]  = winstoreList.get(i).getProductName();					
				winstore_title[6]  = winstoreList.get(i).getEpisode();						
				winstore_title[7]  = winstoreList.get(i).getProductId();					
				winstore_title[8]  = winstoreList.get(i).getSecondSettlementStatus();		
				winstore_title[9]  = winstoreList.get(i).getCategory();					
				winstore_title[10] = winstoreList.get(i).getBrand();						
				winstore_title[11] = winstoreList.get(i).getAuthor();						
				winstore_title[12] = winstoreList.get(i).getPictureAuthor();				
				winstore_title[13] = winstoreList.get(i).getType();						
				winstore_title[14] = winstoreList.get(i).getSaleRate();					
				winstore_title[15] = winstoreList.get(i).getCencelRate();					
				winstore_title[16] = winstoreList.get(i).getSettlementTargetAmount();		
				winstore_title[17] = winstoreList.get(i).getSalesRate();					
				winstore_title[18] = winstoreList.get(i).getSalesAmount();					
				winstore_title[19] = winstoreList.get(i).getCencelsRate();					
				winstore_title[20] = winstoreList.get(i).getCencelsAmount();				
				winstore_title[21] = winstoreList.get(i).getSum();							
				winstore_title[22] = winstoreList.get(i).getCustomerPayment();				
				winstore_title[23] = winstoreList.get(i).getCustomerPaymentReceipt();		
				winstore_title[24] = winstoreList.get(i).getCustomerPaymentEtc();			
				winstore_title[25] = winstoreList.get(i).getCustomerPaymentService();		
				winstore_title[26] = winstoreList.get(i).getCustomerPaymentServiceEtc();	
				winstore_title[27] = winstoreList.get(i).getServiceDeduction();			
				winstore_title[28] = winstoreList.get(i).getPayment();						
				winstore_title[29] = winstoreList.get(i).getPaymentType();					
				
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(winstore_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_yes24":
			fileName = "예스24";
			String[] yes24_title = {"No","도서명","출판사","출판사판매가","판매서점","서점판매가","서점환불가","원가율","출판사정산액","이벤트구분","판매구분","이벤트판매가","저자명","bookID","ePubID","세트코드","종이책ISBN","전자책ISBN","판매일","환불일"};
			length = yes24_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(0);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(yes24_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Yes24> yes24List = null;
			if(auth.equals("ROLE_ADMIN")) {
				yes24List = eBookService.listYes24(setDate);
			} else {
				yes24List = authService.listYes24(setDate, principal.getName());
			}
			for(int i=0; i<yes24List.size(); i++) {
				objRow = objSheet.createRow(1+i);
				yes24_title[0]  = yes24List.get(i).getNo();							
				yes24_title[1]  = yes24List.get(i).getProductName();							
				yes24_title[2]  = yes24List.get(i).getBrand();						
				yes24_title[3]  = yes24List.get(i).getBrandSalePrice();						
				yes24_title[4]  = yes24List.get(i).getSales();						
				yes24_title[5]  = yes24List.get(i).getSalesSalePrice();						
				yes24_title[6]  = yes24List.get(i).getSalesRefundPrice();			
				yes24_title[7]  = yes24List.get(i).getCostRate();							
				yes24_title[8]  = yes24List.get(i).getPayment();						
				yes24_title[9]  = yes24List.get(i).getEventType();							
				yes24_title[10] = yes24List.get(i).getSaleType();					
				yes24_title[11] = yes24List.get(i).getEventSalePrice();						
				yes24_title[12] = yes24List.get(i).getAuthor();						
				yes24_title[13] = yes24List.get(i).getBookId();						
				yes24_title[14] = yes24List.get(i).getEpublId();								
				yes24_title[15] = yes24List.get(i).getSetCode();						
				yes24_title[16] = yes24List.get(i).getPaperbookIsbn();						
				yes24_title[17] = yes24List.get(i).getEbookIsbn();					
				yes24_title[18] = yes24List.get(i).getSaleDate();					
				yes24_title[19] = yes24List.get(i).getRefundDate();								
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(yes24_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;
		case "p_aladin":
			fileName = "알라딘";
			String[] aladin_title = {"판매/취소일시","ItemId","제목","판매형태","정가","판매가","정산액","취소주문일시","저자명","출판사명","정산처명","ISBN","CID"};
			length = aladin_title.length;		

			//칼럼이름
			objRow = objSheet.createRow(0);

			for(int i=0; i<length; i++){
				objCell = objRow.createCell(i);
				objCell.setCellValue(aladin_title[i]);
				objCell.setCellStyle(styleSub);
			}
			
			
			//본문내용
			List<Aladin> aladinList = null;
			if(auth.equals("ROLE_ADMIN")) {
				aladinList = eBookService.listAladin(setDate);
			} else {
				aladinList = authService.listAladin(setDate, principal.getName());
			}
			for(int i=0; i<aladinList.size(); i++) {
				objRow = objSheet.createRow(1+i);
				aladin_title[0]  = aladinList.get(i).getSalesCancelDate();	
				aladin_title[1]  = aladinList.get(i).getItemId();			
				aladin_title[2]  = aladinList.get(i).getProductName();		
				aladin_title[3]  = aladinList.get(i).getSalesType();		
				aladin_title[4]  = aladinList.get(i).getNetPrice();		
				aladin_title[5]  = aladinList.get(i).getSalePrice();		
				aladin_title[6]  = aladinList.get(i).getPayment();			
				aladin_title[7]  = aladinList.get(i).getCancelDate();		
				aladin_title[8]  = aladinList.get(i).getAuthor();			
				aladin_title[9]  = aladinList.get(i).getBrand();			
				aladin_title[10] = aladinList.get(i).getPublisher();		
				aladin_title[11] = aladinList.get(i).getIsbn();			
				aladin_title[12] = aladinList.get(i).getCid();					
				for(int j=0; j<length; j++) {
					objCell = objRow.createCell(j);
					objCell.setCellValue(aladin_title[j]);
					objCell.setCellStyle(styleSub);
				}
			}
			break;

		default:
			break;
		}
		

		// 플랫폼별 분기처리 끝 -----------------------------------------------------------------------
		//----------------------------------------------------------------------------------------

		//길이 설정

	//	for(int i=0; i<sub_title.length; i++){
	//		objSheet.autoSizeColumn(i);
	//		objSheet.setColumnWidth(i, (objSheet.getColumnWidth(i))+512);
	//	}

		//--------------------------------------------------------------------------------------
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
		
		return "redirect:list?platformType=" + p_type;
	}
}
