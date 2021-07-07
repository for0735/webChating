package com.eBook.mgr.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.eBook.mgr.domain.Author;
import com.eBook.mgr.domain.platform.Bookcube;
import com.eBook.mgr.dto.AuthorListDto;
import com.eBook.mgr.dto.PaymentDto;
import com.eBook.mgr.service.AuthService;
import com.eBook.mgr.service.AuthorService;
import com.eBook.mgr.service.MemberService;
import com.eBook.mgr.service.PaymentService;

@Controller
@RequestMapping("/ebook")
public class PaymentController {
	
	private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
	
	@Autowired
	PaymentService paymentService;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	AuthorService authorService;
	
	@Autowired
	AuthService authService;

	@RequestMapping(value = "/payment")
	public void payment(Model model, String p_year_select, String p_month_select, Principal principal) throws Exception{
		
		log.info("===============================???"); 
		log.info("연... : " + p_year_select);
		log.info("월... : " + p_month_select);
		
		// setDate계산 ---------------------------------------------
		String setDate = ""; 

		if(p_month_select != null) {
			p_year_select = p_year_select.replace("년", "");
			p_month_select = p_month_select.replace("월", "");
			
			setDate = p_year_select + "-" + p_month_select;
		} else {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			String monthString;
			
			if(month < 10) {
				monthString = "0" + Integer.toString(month);
			} else {
				monthString = Integer.toString(month);
			}
			
			setDate = Integer.toString(year) + "-" + monthString;
		}
		
		model.addAttribute("setDate", setDate);

		
		log.info("현재 셋데이터" + setDate);
		// --------------------------------------------------------
		
		String auth = authService.authRoll(principal.getName());
		log.info("권한? : " + auth);
		List<PaymentDto> paymentDto = new ArrayList<PaymentDto>();
		
		if(auth.equals("ROLE_ADMIN")) {
			paymentDto = paymentService.listPayment(setDate);
		} else {
			paymentDto = authService.listPayment(setDate, principal.getName());
			if(paymentDto.size() > 0) {
				if(paymentDto.get(0) == null) {
					paymentDto = new ArrayList<PaymentDto>();
				}
			}
		}
		
		log.info("paymentController : " + paymentDto.size());
		model.addAttribute("paymentList", paymentDto);
	}
	
	@RequestMapping(value = "/finalPayment", method = RequestMethod.POST)
	public ModelAndView finalCreate(Model model, String p_year, String p_month, HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		
		// setDate계산 ---------------------------------------------
		String setDate = ""; 

		if(p_month != null) {
			p_year = p_year.replace("년", "");
			p_month = p_month.replace("월", "");
			
			setDate = p_year + "-" + p_month;
		} else {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			String monthString;
			
			if(month < 10) {
				monthString = "0" + Integer.toString(month);
			} else {
				monthString = Integer.toString(month);
			}
			
			setDate = Integer.toString(year) + "-" + monthString;
		}
		
		model.addAttribute("setDate", setDate);
		log.info("현재 셋데이터 : " + setDate);
		
		

		try {
			paymentService.removeList(setDate);
			
			PaymentDto paymentDto = new PaymentDto();
			String writerList[] = paymentService.writerIdList();
			
			log.info("길이가?? : " + writerList.length);
			log.info("첫번째 원소 : " + writerList[0]);
			for(int i=0; i<writerList.length; i++) {
				AuthorListDto authorListDto = new AuthorListDto();

				authorListDto = authorService.read(writerList[i]);
				System.out.println(writerList[i]);
				System.out.println(setDate);
				
				paymentDto = paymentService.allPayment(writerList[i], setDate);
				
				paymentDto.setAuthor(authorListDto.getAuthor());
				paymentDto.setRealName(authorListDto.getRealName());
				paymentDto.setCtzNumber(authorListDto.getCtzNumber());
				paymentDto.setAccountNumber(authorListDto.getAccountNumber());
				paymentDto.setSetDate(setDate + "-10");
				paymentDto.setWriterId(writerList[i]);
				paymentDto.setSettlementRatio(authorListDto.getSettlementRatio());
				paymentDto.setCarryAmount(authorListDto.getCarryAmount());
				paymentDto.setVirtuousTax(authorListDto.getVirtuousTax());
				
				// 여기서부터 정산계산
				String allPayment;						// 총매출
				String authorSettlement;				// 작가정산액
				String payment;							// 정산액합계
				
				//총매출계산
				allPayment = allpay(paymentDto);
				paymentDto.setAllPayment(allPayment);
				
				//작가정산액계산
				authorSettlement = Integer.toString((int)(Integer.parseInt(paymentDto.getAllPayment()) * Double.parseDouble(paymentDto.getSettlementRatio())));
				paymentDto.setAuthorSettlement(authorSettlement);
				
				//정산액합계계산
				payment = Integer.toString(Integer.parseInt(paymentDto.getAuthorSettlement()) + Integer.parseInt(paymentDto.getCarryAmount()));
				paymentDto.setPayment(payment);
				
				//소득세계산
				int a = Integer.parseInt(paymentDto.getPayment()) - Integer.parseInt(paymentDto.getVirtuousTax());
				a = (int) (a * 0.03);
				a = (int) (a * 0.1);
				a = a * 10;
				paymentDto.setIncomeTax(Integer.toString(a));
				
				//원천징수세계산
				a = (int) (a * 0.1);
				a = (int) (a * 0.1);
				a = a * 10;
				paymentDto.setWthldTax(Integer.toString(a));
				
				//작가지급액계산
				int tempAmount;
				tempAmount = Integer.parseInt(paymentDto.getPayment()) - Integer.parseInt(paymentDto.getVirtuousTax()) - Integer.parseInt(paymentDto.getWthldTax()) - Integer.parseInt(paymentDto.getIncomeTax());
				paymentDto.setAuthorPaid(Integer.toString(tempAmount));
				
				System.out.println(paymentDto);
				
				//선인세 이월정산금 다음달 존재여부 판단 업데이트
				if(tempAmount <= 0) {
					paymentService.updateCarryAmount(paymentDto.getPayment(), writerList[i]);
				} else {
					paymentService.updateCarryAmount("0", writerList[i]);
					paymentService.updateVirtuousTax(writerList[i]);
				}
				
				//정산행집어넣기
				paymentService.insertPayment(paymentDto);
			}
			
			log.info("정산되었습니다");
			mv.addObject("result", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			e.getStackTrace();
			mv.addObject("result", false);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/createPayment", method = RequestMethod.POST)
	public ModelAndView create(Model model, String p_year, String p_month, HttpServletRequest request) throws Exception{
		ModelAndView mv = new ModelAndView("jsonView");
		
		// setDate계산 ---------------------------------------------
		String setDate = ""; 

		if(p_month != null) {
			p_year = p_year.replace("년", "");
			p_month = p_month.replace("월", "");
			
			setDate = p_year + "-" + p_month;
		} else {
			Calendar cal = Calendar.getInstance();
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			String monthString;
			
			if(month < 10) {
				monthString = "0" + Integer.toString(month);
			} else {
				monthString = Integer.toString(month);
			}
			
			setDate = Integer.toString(year) + "-" + monthString;
		}
		
		model.addAttribute("setDate", setDate);
		log.info("현재 셋데이터 : " + setDate);
		
		

		try {
			paymentService.removeList(setDate);
			
			PaymentDto paymentDto = new PaymentDto();
			String writerList[] = paymentService.writerIdList();
			
			log.info("길이가?? : " + writerList.length);
			log.info("첫번째 원소 : " + writerList[0]);
			for(int i=0; i<writerList.length; i++) {
				AuthorListDto authorListDto = new AuthorListDto();

				authorListDto = authorService.read(writerList[i]);
				System.out.println(writerList[i]);
				System.out.println(setDate);
				
				paymentDto = paymentService.allPayment(writerList[i], setDate);
				
				paymentDto.setAuthor(authorListDto.getAuthor());
				paymentDto.setRealName(authorListDto.getRealName());
				paymentDto.setCtzNumber(authorListDto.getCtzNumber());
				paymentDto.setAccountNumber(authorListDto.getAccountNumber());
				paymentDto.setSetDate(setDate + "-10");
				paymentDto.setWriterId(writerList[i]);
				paymentDto.setSettlementRatio(authorListDto.getSettlementRatio());
				paymentDto.setCarryAmount(authorListDto.getCarryAmount());
				paymentDto.setVirtuousTax(authorListDto.getVirtuousTax());
				
				// 여기서부터 정산계산
				String allPayment;						// 총매출
				String authorSettlement;				// 작가정산액
				String payment;							// 정산액합계
				
				//총매출계산
				allPayment = allpay(paymentDto);
				paymentDto.setAllPayment(allPayment);
				
				//작가정산액계산
				authorSettlement = Integer.toString((int)(Integer.parseInt(paymentDto.getAllPayment()) * Double.parseDouble(paymentDto.getSettlementRatio())));
				paymentDto.setAuthorSettlement(authorSettlement);
				
				//정산액합계계산
				payment = Integer.toString(Integer.parseInt(paymentDto.getAuthorSettlement()) + Integer.parseInt(paymentDto.getCarryAmount()));
				paymentDto.setPayment(payment);
				
				//소득세계산
				int a = Integer.parseInt(paymentDto.getPayment()) - Integer.parseInt(paymentDto.getVirtuousTax());
				a = (int) (a * 0.03);
				a = (int) (a * 0.1);
				a = a * 10;
				paymentDto.setIncomeTax(Integer.toString(a));
				
				//원천징수세계산
				a = (int) (a * 0.1);
				a = (int) (a * 0.1);
				a = a * 10;
				paymentDto.setWthldTax(Integer.toString(a));
				
				//작가지급액계산
				int tempAmount;
				tempAmount = Integer.parseInt(paymentDto.getPayment()) - Integer.parseInt(paymentDto.getVirtuousTax()) - Integer.parseInt(paymentDto.getWthldTax()) - Integer.parseInt(paymentDto.getIncomeTax());
				paymentDto.setAuthorPaid(Integer.toString(tempAmount));
				
				System.out.println(paymentDto);
				
				//정산행집어넣기
				paymentService.insertPayment(paymentDto);
			}
			
			log.info("정산되었습니다");
			mv.addObject("result", true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			e.getStackTrace();
			mv.addObject("result", false);
		}
		
		return mv;
	}
	
	@RequestMapping(value = "/paymentUpload", method = RequestMethod.POST)
	public String paymentUpload(String p_type, HttpServletResponse response, HttpServletRequest request, String[] nowDate, Principal principal) throws Exception{
		
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
		
		
		
		// 데이터 넣는 부분----------------------------------------
		fileName = "정산파일";
		String[] payment_title = {"작가명","본명","주민등록번호","계좌번호","북큐브","이퍼브","조아라","카카오페이지","교보문고","미스터블루","문피아","네이버","리디북스","로망띠끄","톡소다","원스토어","예스24","알라딘","총매출","정산비율","작가정산액","이월정산액","정산액합계","선인세","소득세","원천징수세","작가지급액"};
		int length = payment_title.length;		

		//칼럼이름
		objRow = objSheet.createRow(0);

		for(int i=0; i<length; i++){
			objCell = objRow.createCell(i);
			objCell.setCellValue(payment_title[i]);
			objCell.setCellStyle(styleSub);
		}
		
		
		//본문내용
		List<PaymentDto> paymentDto = null;
		if(auth.equals("ROLE_ADMIN")) {
			paymentDto = paymentService.listPayment(setDate);
		} else {
			paymentDto = authService.listPayment(setDate, principal.getName());
			if(paymentDto.size() > 0) {
				if(paymentDto.get(0) == null) {
					paymentDto = new ArrayList<PaymentDto>();
				}
			}
		}
		
		for(int i=0; i<paymentDto.size(); i++) {
			objRow = objSheet.createRow(1+i);
			payment_title[0] = paymentDto.get(i).getAuthor();
			payment_title[1] = paymentDto.get(i).getRealName();
			payment_title[2] = paymentDto.get(i).getCtzNumber();
			payment_title[3] = paymentDto.get(i).getAccountNumber();
			payment_title[4] = paymentDto.get(i).getPayBookcube();
			payment_title[5] = paymentDto.get(i).getPayEpub();
			payment_title[6] = paymentDto.get(i).getPayJoara();
			payment_title[7] = paymentDto.get(i).getPayKakao();
			payment_title[8] = paymentDto.get(i).getPayKyobo();
			payment_title[9] = paymentDto.get(i).getPayMrblue();
			payment_title[10] = paymentDto.get(i).getPayMunpia();
			payment_title[11] = paymentDto.get(i).getPayNaver();
			payment_title[12] = paymentDto.get(i).getPayRidibooks();
			payment_title[13] = paymentDto.get(i).getPayRomance();
			payment_title[14] = paymentDto.get(i).getPayTocsoda();
			payment_title[15] = paymentDto.get(i).getPayWinstore();
			payment_title[16] = paymentDto.get(i).getPayYes24();
			payment_title[17] = paymentDto.get(i).getPayAladin();
			payment_title[18] = paymentDto.get(i).getAllPayment();
			payment_title[19] = paymentDto.get(i).getSettlementRatio();
			payment_title[20] = paymentDto.get(i).getAuthorSettlement();
			payment_title[21] = paymentDto.get(i).getCarryAmount();
			payment_title[22] = paymentDto.get(i).getPayment();
			payment_title[23] = paymentDto.get(i).getVirtuousTax();
			payment_title[24] = paymentDto.get(i).getIncomeTax();
			payment_title[25] = paymentDto.get(i).getWthldTax();
			payment_title[26] = paymentDto.get(i).getAuthorPaid();
			
			for(int j=0; j<length; j++) {
				objCell = objRow.createCell(j);
				objCell.setCellValue(payment_title[j]);
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
		
		return "redirect:payment";
		
	}
	
	public String allpay(PaymentDto paymentDto) {
		String a;
		int sum;
		
		sum = Integer.parseInt(paymentDto.getPayBookcube()) +
				Integer.parseInt(paymentDto.getPayEpub()) +
				Integer.parseInt(paymentDto.getPayJoara()) +
				Integer.parseInt(paymentDto.getPayKakao()) +
				Integer.parseInt(paymentDto.getPayKyobo()) +
				Integer.parseInt(paymentDto.getPayMrblue()) +
				Integer.parseInt(paymentDto.getPayMunpia()) +
				Integer.parseInt(paymentDto.getPayNaver()) +
				Integer.parseInt(paymentDto.getPayRidibooks()) +
				Integer.parseInt(paymentDto.getPayRomance()) +
				Integer.parseInt(paymentDto.getPayTocsoda()) +
				Integer.parseInt(paymentDto.getPayWinstore()) +
				Integer.parseInt(paymentDto.getPayYes24()) +
				Integer.parseInt(paymentDto.getPayAladin());
		
		a = Integer.toString(sum);
		return a;
	}
}
