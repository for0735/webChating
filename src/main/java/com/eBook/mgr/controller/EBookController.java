package com.eBook.mgr.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.eBook.mgr.domain.platform.Aladin;
import com.eBook.mgr.domain.platform.Bookcube;
import com.eBook.mgr.domain.platform.Epub;
import com.eBook.mgr.domain.platform.Joara;
import com.eBook.mgr.domain.platform.Kakao;
import com.eBook.mgr.domain.platform.Kyobo;
import com.eBook.mgr.domain.platform.Mrblue;
import com.eBook.mgr.domain.platform.Munpia;
import com.eBook.mgr.domain.platform.Naver;
import com.eBook.mgr.domain.platform.Ridibooks;
import com.eBook.mgr.domain.platform.Romance;
import com.eBook.mgr.domain.platform.Tocsoda;
import com.eBook.mgr.domain.platform.Winstore;
import com.eBook.mgr.domain.platform.Yes24;
import com.eBook.mgr.dto.PaymentDto;
import com.eBook.mgr.service.AuthService;
import com.eBook.mgr.service.EBookService;

@Controller
@RequestMapping("/ebook")
public class EBookController {
	
	private static final Logger log = LoggerFactory.getLogger(EBookController.class);

	@Autowired
	EBookService eBookService;	
	
	@Autowired
	AuthService authService;
	
	/*
	안되는것 2020-07-28
	 - 파일다운로드시 뭔가 오류가남
	*/
	
	/*
	 엑셀 플랫폼 속성 통일하기
	 --- 5월기준 ---
	 */
	
	
//	@RequestMapping(value = "/list", method = RequestMethod.GET)
//	public String list1(Model model, @RequestParam String platformType, Date setDate) throws Exception{
//		return "/ebook/list";
//	}
	
	// 플랫폼 별로 리스트 보여주기
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/list")
	public void list(Model model, @RequestParam String platformType, String p_year, String p_month, Principal principal) throws Exception{
		log.info("연도...: " + p_year);
		log.info("월...: " + p_month);
		model.addAttribute("platformType", platformType);
		
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
		
		String auth = authService.authRoll(principal.getName());

		
		log.info("현재 셋데이터" + setDate);
		// --------------------------------------------------------
		switch (platformType) {
		case "p_bookcube":
			log.info("p_bookcube 진입");
			List<Bookcube> bookList = new ArrayList<Bookcube>();
			if(auth.equals("ROLE_ADMIN")) {
				bookList = eBookService.listBookcube(setDate);
			} else {
				bookList = authService.listBookcube(setDate, principal.getName());
			}
			model.addAttribute("listBookcube", bookList);
			break;
		case "p_epub":
			log.info("p_epub 진입");
			List<Epub> epubList = new ArrayList<Epub>();
			if(auth.equals("ROLE_ADMIN")) {
				epubList = eBookService.listEpub(setDate);
			} else {
				epubList = authService.listEpub(setDate, principal.getName());
			}
			model.addAttribute("listEpub", epubList);
			break;
		case "p_joara":
			log.info("p_joara 진입");
			List<Joara> joaraList = new ArrayList<Joara>();
			if(auth.equals("ROLE_ADMIN")) {
				joaraList = eBookService.listJoara(setDate);
			} else {
				joaraList = authService.listJoara(setDate, principal.getName());
			}
			model.addAttribute("listJoara", joaraList);
			break;
		case "p_kakao":
			log.info("p_kakao 진입");
			List<Kakao> kakaoList = new ArrayList<Kakao>();
			if(auth.equals("ROLE_ADMIN")) {
				kakaoList = eBookService.listKakao(setDate);
			} else {
				kakaoList = authService.listKakao(setDate, principal.getName());
			}
			model.addAttribute("listKakao", kakaoList);
			break;
		case "p_kyobo":
			log.info("p_kyobo 진입");
			List<Kyobo> kyoboList = new ArrayList<Kyobo>();
			if(auth.equals("ROLE_ADMIN")) {
				kyoboList = eBookService.listkyobo(setDate);
			} else {
				kyoboList = authService.listkyobo(setDate, principal.getName());
			}
			model.addAttribute("listKyobo", kyoboList);
			break;
		case "p_mrblue":
			log.info("p_mrblue 진입");
			List<Mrblue> mrblueList = new ArrayList<Mrblue>();
			if(auth.equals("ROLE_ADMIN")) {
				mrblueList = eBookService.listMrblue(setDate);
			} else {
				mrblueList = authService.listMrblue(setDate, principal.getName());
			}
			model.addAttribute("listMrblue", mrblueList);
			break;
		case "p_munpia":
			log.info("p_munpia 진입");
			List<Munpia> munpiaList = new ArrayList<Munpia>();
			if(auth.equals("ROLE_ADMIN")) {
				munpiaList = eBookService.listMunpia(setDate);
			} else {
				munpiaList = authService.listMunpia(setDate, principal.getName());
			}
			model.addAttribute("listMunpia", munpiaList);
			break;
		case "p_naver":
			log.info("p_naver 진입");
			List<Naver> naverList = new ArrayList<Naver>();
			if(auth.equals("ROLE_ADMIN")) {
				naverList = eBookService.listNaver(setDate);
			} else {
				naverList = authService.listNaver(setDate, principal.getName());
			}
			model.addAttribute("listNaver", naverList);
			break;
		case "p_ridibooks":
			log.info("p_ridibooks 진입");
			List<Ridibooks> ridibooksList = new ArrayList<Ridibooks>();
			if(auth.equals("ROLE_ADMIN")) {
				ridibooksList = eBookService.listRidibooks(setDate);
			} else {
				ridibooksList = authService.listRidibooks(setDate, principal.getName());
			}
			model.addAttribute("listRidibooks", ridibooksList);
			break;
		case "p_romance":
			log.info("p_romance 진입");
			List<Romance> romanceList = new ArrayList<Romance>();
			if(auth.equals("ROLE_ADMIN")) {
				romanceList = eBookService.listRomance(setDate);
			} else {
				romanceList = authService.listRomance(setDate, principal.getName());
			}
			model.addAttribute("listRomance", romanceList);
			break;
		case "p_tocsoda":
			log.info("p_tocsoda 진입");
			List<Tocsoda> tocsodaList = new ArrayList<Tocsoda>();
			if(auth.equals("ROLE_ADMIN")) {
				tocsodaList = eBookService.listTocsoda(setDate);
			} else {
				tocsodaList = authService.listTocsoda(setDate, principal.getName());
			}
			model.addAttribute("listTocsoda", tocsodaList);
			break;
		case "p_winstore":
			log.info("p_winstore 진입");
			List<Winstore> winstoreList = new ArrayList<Winstore>();
			if(auth.equals("ROLE_ADMIN")) {
				winstoreList = eBookService.listWinstore(setDate);
			} else {
				winstoreList = authService.listWinstore(setDate, principal.getName());
			}
			model.addAttribute("listWinstore", winstoreList);
			break;
		case "p_yes24":
			log.info("p_yes24 진입");
			List<Yes24> yes24List = new ArrayList<Yes24>();
			if(auth.equals("ROLE_ADMIN")) {
				yes24List = eBookService.listYes24(setDate);
			} else {
				yes24List = authService.listYes24(setDate, principal.getName());
			}
			model.addAttribute("listYes24", yes24List);
			break;
		case "p_aladin":
			log.info("p_aladin 진입");
			List<Aladin> aladinList = new ArrayList<Aladin>();
			if(auth.equals("ROLE_ADMIN")) {
				aladinList = eBookService.listAladin(setDate);
			} else {
				aladinList = authService.listAladin(setDate, principal.getName());
			}
			model.addAttribute("listAladin", aladinList);
			break;

		default:
			break;
		}
		
	}
	
	/*
	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public void listPost(Model model, @RequestParam String platformType, String setDate) throws Exception{
		log.info("플랫폼타입???" + platformType);
		
		model.addAttribute("platformType", platformType);
		
		log.info("현재 셋데이터" + setDate);
		
		setDate = "2020-01-12";
		
		switch (platformType) {
		case "p_bookcube":
			List<Bookcube> bookList = eBookService.listBookcube(setDate);
			model.addAttribute("listBookcube", bookList);
			break;
//		case "p_epub":
//			model.addAttribute("listBookcube", eBookService.listEpub(setDate));
//			break;
		case "p_joara":
			log.info("p_joara 진입");
			model.addAttribute("listJoara", eBookService.listJoara(setDate));
			break;
		case "p_kakao":
			log.info("p_kakao 진입");
			model.addAttribute("listKakao", eBookService.listKakao(setDate));
			break;
		case "p_kyobo":
			log.info("p_kyobo 진입");
			model.addAttribute("listKyobo", eBookService.listkyobo(setDate));
			break;
		case "p_mrblue":
			log.info("p_mrblue 진입");
			model.addAttribute("listMrblue", eBookService.listMrblue(setDate));
			break;
		case "p_munpia":
			log.info("p_munpia 진입");
			model.addAttribute("listMunpia", eBookService.listMunpia(setDate));
			break;
		case "p_naver":
			log.info("p_naver 진입");
			model.addAttribute("listNaver", eBookService.listNaver(setDate));
			break;
		case "p_ridibooks":
			log.info("p_ridibooks 진입");
			model.addAttribute("listRidibooks", eBookService.listRidibooks(setDate));
			break;
		case "p_romance":
			log.info("p_romance 진입");
			model.addAttribute("listRomance", eBookService.listRomance(setDate));
			break;
		case "p_tocsoda":
			log.info("p_tocsoda 진입");
			model.addAttribute("listTocsoda", eBookService.listTocsoda(setDate));
			break;
		case "p_winstore":
			log.info("p_winstore 진입");
			model.addAttribute("listWinstore", eBookService.listWinstore(setDate));
			break;
		case "p_yes24":
			log.info("p_yes24 진입");
			model.addAttribute("listYes24", eBookService.listYes24(setDate));
			break;
		case "p_aladin":
			log.info("p_aladin 진입");
			model.addAttribute("listAladin", eBookService.listAladin(setDate));
			break;

		default:
			break;
		}
	}
	*/
	
	// 체크된 리스트 삭제하기
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public String remove(Model model, String delete_ids, String p_type) throws Exception{
		log.info("플랫폼타입검사 : " + p_type);
		String[] deleteIdsArr = delete_ids.split(",");
		
		try {
			model.addAttribute("result", true);
			for (int i=0; i<deleteIdsArr.length; i++)
			{ 
				System.out.println("해당 튜플삭제.....................     = " + deleteIdsArr[i]); 
				switch (p_type) {
				case "p_bookcube":
					eBookService.removeBookcube(deleteIdsArr[i]);
					break;
				case "p_epub":
					eBookService.removeEpub(deleteIdsArr[i]);
					break;
				case "p_joara":
					eBookService.removeJoara(deleteIdsArr[i]);
					break;
				case "p_kakao":
					eBookService.removeKakao(deleteIdsArr[i]);
					break;
				case "p_kyobo":
					eBookService.removeKyobo(deleteIdsArr[i]);
					break;
				case "p_mrblue":
					eBookService.removeMrblue(deleteIdsArr[i]);
					break;
				case "p_munpia":
					eBookService.removeMunpia(deleteIdsArr[i]);
					break;
				case "p_naver":
					eBookService.removeNaver(deleteIdsArr[i]);
					break;
				case "p_ridibooks":
					eBookService.removeRidibooks(deleteIdsArr[i]);
					break;
				case "p_romance":
					eBookService.removeRomance(deleteIdsArr[i]);
					break;
				case "p_tocsoda":
					eBookService.removeTocsoda(deleteIdsArr[i]);
					break;
				case "p_winstore":
					eBookService.removeWinstore(deleteIdsArr[i]);
					break;
				case "p_yes24":
					eBookService.removeYes24(deleteIdsArr[i]);
					break;
				case "p_aladin":
					eBookService.removeAladin(deleteIdsArr[i]);
					break;

				default:
					break;
				}
			}
			
			// mv.addObject("result", true);
		} catch (Exception e) {
			model.addAttribute("result", false);
			//mv.addObject("result", false);
			e.printStackTrace();
		}
		
		// return mv;
		return "redirect:/ebook/list?platformType=" + p_type;
	
	}
	
	
	// 적용하기 누르면 리스트에있는 목록 수정시키기
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(Model model, HttpServletRequest request, String p_type) throws Exception{
		
		int i;
		
		String[] setDate = request.getParameterValues("setDate");
		log.info("과연 날짜는?" + setDate[0]);
		
		//log.info("찍힘? " + a.length + " 값은?" + a[1]);
		
		log.info("플랫폼타입검사 : " + p_type);
		
		try {
			
			switch (p_type) {
			case "p_bookcube":
				log.info("-------------------------------------------------------------");
				eBookService.allRemoveBookcube(setDate[0]);
				Bookcube bookcube = new Bookcube();
				
				String[] productNameBookcube = request.getParameterValues("productName");
				String[] authorBookcube = request.getParameterValues("author");
				String[] brandBookcube = request.getParameterValues("brand");
				String[] salesCategoryBookcube = request.getParameterValues("salesCategory");
				String[] salesTypeBookcube = request.getParameterValues("salesType");
				String[] episodeBookcube = request.getParameterValues("episode");
				String[] netPriceBookcube = request.getParameterValues("netPrice");
				String[] salePriceBookcube = request.getParameterValues("salePrice");
				String[] discountBookcube = request.getParameterValues("discount");
				String[] feeBookcube = request.getParameterValues("fee");
				String[] netTargetPriceBookcube = request.getParameterValues("netTargetPrice");
				String[] deductionReservesEventBookcube = request.getParameterValues("deductionReservesEvent");
				String[] paymentBookcube = request.getParameterValues("payment");
				String[] bookIdBookcube = request.getParameterValues("bookId");
				String[] isbnBookcube = request.getParameterValues("isbn");
				String[] eIsbnBookcube = request.getParameterValues("eIsbn");
				String[] eIsbnEpubBookcube = request.getParameterValues("eIsbnEpub");
				String[] writerIdBookcube = request.getParameterValues("writerId");
				String[] setDateBookcube = request.getParameterValues("setDate");
				
				for(i=0; i<productNameBookcube.length; i++) {
					netPriceBookcube[i] = netPriceBookcube[i].replaceAll(",", "");
					salePriceBookcube[i] = salePriceBookcube[i].replaceAll(",", "");
					discountBookcube[i] = discountBookcube[i].replaceAll(",", "");
					feeBookcube[i] = feeBookcube[i].replaceAll(",", "");
					netTargetPriceBookcube[i] = netTargetPriceBookcube[i].replaceAll(",", "");
					paymentBookcube[i] = paymentBookcube[i].replaceAll(",", "");
					
					bookcube.setProductName(productNameBookcube[i]);
					bookcube.setAuthor(authorBookcube[i]);			
					bookcube.setBrand(brandBookcube[i]);			
					bookcube.setSalesCategory(salesCategoryBookcube[i]);			
					bookcube.setSalesType(salesTypeBookcube[i]);			
					bookcube.setEpisode(episodeBookcube[i]);			
					bookcube.setNetPrice(netPriceBookcube[i]);			
					bookcube.setSalePrice(salePriceBookcube[i]);			
					bookcube.setDiscount(discountBookcube[i]);			
					bookcube.setFee(feeBookcube[i]);			
					bookcube.setNetTargetPrice(netTargetPriceBookcube[i]);			
					bookcube.setDeductionReservesEvent(deductionReservesEventBookcube[i]);			
					bookcube.setPayment(paymentBookcube[i]);			
					bookcube.setBookId(bookIdBookcube[i]);			
					bookcube.setIsbn(isbnBookcube[i]);			
					bookcube.setEIsbn(eIsbnBookcube[i]);			
					bookcube.setEIsbnEpub(eIsbnEpubBookcube[i]);			
					bookcube.setWriterId(writerIdBookcube[i]);	
					bookcube.setSetDate(setDateBookcube[i]);	
					
					eBookService.registerBookcube(bookcube);
				}
				break;
				
			case "p_epub":
				log.info("-------------ebpudelele------");
				eBookService.allRemoveEpub(setDate[0]);
				Epub epub = new Epub();
				
				String[] indexNumEpub = request.getParameterValues("indexNum");
				String[] brandEpub = request.getParameterValues("brand");
				String[] productNameEpub = request.getParameterValues("productName");
				String[] publisherEpub = request.getParameterValues("publisher");
				String[] publisherAmountEpub = request.getParameterValues("publisherAmount");
				String[] salesEpub = request.getParameterValues("sales");
				String[] salesAmountEpub = request.getParameterValues("salesAmount");
				String[] cencelsAmountEpub = request.getParameterValues("cencelsAmount");
				String[] costRateEpub = request.getParameterValues("costRate");
				String[] paymentEpub = request.getParameterValues("payment");
				String[] eventTypeEpub = request.getParameterValues("eventType");
				String[] salesTypeEpub = request.getParameterValues("salesType");
				String[] eventAmountEpub = request.getParameterValues("eventAmount");
				String[] authorEpub = request.getParameterValues("author");
				String[] epubidEpub = request.getParameterValues("epubid");
				String[] setCodeEpub = request.getParameterValues("setCode");
				String[] paperbookIsbnEpub = request.getParameterValues("paperbookIsbn");
				String[] ebookIsbnEpub = request.getParameterValues("ebookIsbn");
				String[] salseDateEpub = request.getParameterValues("salseDate");
				String[] cencelsDateEpub = request.getParameterValues("cencelsDate");
				String[] writerIdEpub = request.getParameterValues("writerId");
				String[] setDateEpub = request.getParameterValues("setDate");
				
				
				for(i=0; i<productNameEpub.length; i++) {
					publisherAmountEpub[i] = publisherAmountEpub[i].replaceAll(",", "");
					salesAmountEpub[i] = salesAmountEpub[i].replaceAll(",", "");
					cencelsAmountEpub[i] = cencelsAmountEpub[i].replaceAll(",", "");
					paymentEpub[i] = paymentEpub[i].replaceAll(",", "");
					
					epub.setIndexNum(indexNumEpub[i]);
					epub.setBrand(brandEpub[i]);
					epub.setProductName(productNameEpub[i]);
					epub.setPublisher(publisherEpub[i]);
					epub.setPublisherAmount(publisherAmountEpub[i]);
					epub.setSales(salesEpub[i]);
					epub.setSalesAmount(salesAmountEpub[i]);
					epub.setCencelsAmount(cencelsAmountEpub[i]);
					epub.setCostRate(costRateEpub[i]);
					epub.setPayment(paymentEpub[i]);
					epub.setEventAmount(eventTypeEpub[i]);
					epub.setSalesType(salesTypeEpub[i]);
					epub.setEventAmount(eventAmountEpub[i]);
					epub.setAuthor(authorEpub[i]);
					epub.setEpubid(epubidEpub[i]);
					epub.setSetCode(setCodeEpub[i]);
					epub.setPaperbookIsbn(paperbookIsbnEpub[i]);
					epub.setEbookIsbn(ebookIsbnEpub[i]);
					epub.setSalseDate(salseDateEpub[i]);
					epub.setCencelsDate(cencelsDateEpub[i]);
					epub.setWriterId(writerIdEpub[i]);
					epub.setSetDate(setDateEpub[i]);
					
					eBookService.registerEpub(epub);
				}
				break;
				
			case "p_joara":
				eBookService.allRemoveJoara(setDate[0]);
				Joara joara = new Joara();
				
				String[] brandJoara = request.getParameterValues("brand");
				String[] productNameJoara = request.getParameterValues("productName");
				String[] productCodeJoara = request.getParameterValues("productCode");
				String[] authorJoara = request.getParameterValues("author");
				String[] unitJoara = request.getParameterValues("unit");
				String[] saleAmountJoara = request.getParameterValues("saleAmount");
				String[] ratioJoara = request.getParameterValues("ratio");
				String[] paymentJoara = request.getParameterValues("payment");
				String[] paymentDateJoara = request.getParameterValues("paymentDate");
				String[] writerIdJoara = request.getParameterValues("writerId");
				String[] setDateJoara = request.getParameterValues("setDate");
				
				for(i=0; i<brandJoara.length; i++) {
					unitJoara[i] = unitJoara[i].replaceAll(",", "");
					saleAmountJoara[i] = saleAmountJoara[i].replaceAll(",", "");
					paymentJoara[i] = paymentJoara[i].replaceAll(",", "");
					
					joara.setBrand(brandJoara[i]);
					joara.setProductName(productNameJoara[i]);
					joara.setProductCode(productCodeJoara[i]);
					joara.setAuthor(authorJoara[i]);
					joara.setUnit(unitJoara[i]);
					joara.setSaleAmount(saleAmountJoara[i]);
					joara.setRatio(ratioJoara[i]);
					joara.setPayment(paymentJoara[i]);
					joara.setPaymentDate(paymentDateJoara[i]);
					joara.setWriterId(writerIdJoara[i]);
					joara.setSetDate(setDateJoara[i]);
					
					eBookService.registerJoara(joara);
				}
				break;
				
			case "p_kakao":
				eBookService.allRemoveKakao(setDate[0]);
				Kakao kakao = new Kakao();
				
				String[] brandKakao = request.getParameterValues("brand");
				String[] specialGeneral2Kakao = request.getParameterValues("specialGeneral2");
				String[] entrepreneurKakao = request.getParameterValues("entrepreneur");
				String[] entrepreneurIdKakao = request.getParameterValues("entrepreneurId");
				String[] publisherKakao = request.getParameterValues("publisher");
				String[] contractUidKakao = request.getParameterValues("contractUid");
				String[] contractCategoryKakao = request.getParameterValues("contractCategory");
				String[] contractKakao = request.getParameterValues("contract");
				String[] newContractKakao = request.getParameterValues("newContract");
				String[] categoryKakao = request.getParameterValues("category");
				String[] seriesIdKakao = request.getParameterValues("seriesId");
				String[] productNameKakao = request.getParameterValues("productName");
				String[] productCodeKakao = request.getParameterValues("productCode");
				String[] lableKakao = request.getParameterValues("lable");
				String[] authorKakao = request.getParameterValues("author");
				String[] isIsbnKakao = request.getParameterValues("isIsbn");
				String[] cpSettlementRateAndroidKakao = request.getParameterValues("cpSettlementRateAndroid");
				String[] cpSettlementRateIosKakao = request.getParameterValues("cpSettlementRateIos");
				String[] useTicketTypeKakao = request.getParameterValues("useTicketType");
				String[] useTicketAmountKakao = request.getParameterValues("useTicketAmount");
				String[] cacheSumKakao = request.getParameterValues("cacheSum");
				String[] cpp1Kakao = request.getParameterValues("cpp1");
				String[] cpl1Kakao = request.getParameterValues("cpl1");
				String[] cp1_1Kakao = request.getParameterValues("cp1_1");
				String[] cp2_1Kakao = request.getParameterValues("cp2_1");
				String[] cp3_1Kakao = request.getParameterValues("cp3_1");
				String[] cp7_1Kakao = request.getParameterValues("cp7_1");
				String[] cp8_1Kakao = request.getParameterValues("cp8_1");
				String[] cp9_1Kakao = request.getParameterValues("cp9_1");
				String[] eventCache1Kakao = request.getParameterValues("eventCache1");
				String[] originalSumKakao = request.getParameterValues("originalSum");
				String[] cpp2Kakao = request.getParameterValues("cpp2");
				String[] cpl2Kakao = request.getParameterValues("cpl2");
				String[] cp1_2Kakao = request.getParameterValues("cp1_2");
				String[] cp2_2Kakao = request.getParameterValues("cp2_2");
				String[] cp3_2Kakao = request.getParameterValues("cp3_2");
				String[] cp7_2Kakao = request.getParameterValues("cp7_2");
				String[] cp8_2Kakao = request.getParameterValues("cp8_2");
				String[] cp9_2Kakao = request.getParameterValues("cp9_2");
				String[] eventCache2Kakao = request.getParameterValues("eventCache2");
				String[] valueSupplyKakao = request.getParameterValues("valueSupply");
				String[] taxAmountKakao = request.getParameterValues("taxAmount");
				String[] paymentKakao = request.getParameterValues("payment");
				String[] writerIdKakao = request.getParameterValues("writerId");
				String[] setDateKakao = request.getParameterValues("setDate");
				
				for(i=0; i<brandKakao.length; i++) {
					useTicketAmountKakao[i] = useTicketAmountKakao[i].replaceAll(",", "");
					cacheSumKakao[i] = cacheSumKakao[i].replaceAll(",", "");
					cpp1Kakao[i] = cpp1Kakao[i].replaceAll(",", "");
					cpl1Kakao[i] = cpl1Kakao[i].replaceAll(",", "");
					cp1_1Kakao[i] = cp1_1Kakao[i].replaceAll(",", "");
					cp2_1Kakao[i] = cp2_1Kakao[i].replaceAll(",", "");
					cp3_1Kakao[i] = cp3_1Kakao[i].replaceAll(",", "");
					cp7_1Kakao[i] = cp7_1Kakao[i].replaceAll(",", "");
					cp8_1Kakao[i] = cp8_1Kakao[i].replaceAll(",", "");
					cp9_1Kakao[i] = cp9_1Kakao[i].replaceAll(",", "");
					eventCache1Kakao[i] = eventCache1Kakao[i].replaceAll(",", "");
					originalSumKakao[i] = originalSumKakao[i].replaceAll(",", "");
					cpp2Kakao[i] = cpp2Kakao[i].replaceAll(",", "");
					cpl2Kakao[i] = cpl2Kakao[i].replaceAll(",", "");
					cp1_2Kakao[i] = cp1_2Kakao[i].replaceAll(",", "");
					cp2_2Kakao[i] = cp2_2Kakao[i].replaceAll(",", "");
					cp3_2Kakao[i] = cp3_2Kakao[i].replaceAll(",", "");
					cp7_2Kakao[i] = cp7_2Kakao[i].replaceAll(",", "");
					cp8_2Kakao[i] = cp8_2Kakao[i].replaceAll(",", "");
					cp9_2Kakao[i] = cp9_2Kakao[i].replaceAll(",", "");
					eventCache2Kakao[i] = eventCache2Kakao[i].replaceAll(",", "");
					valueSupplyKakao[i] = valueSupplyKakao[i].replaceAll(",", "");
					taxAmountKakao[i] = taxAmountKakao[i].replaceAll(",", "");
					paymentKakao[i] = paymentKakao[i].replaceAll(",", "");
					
					kakao.setBrand(brandKakao[i]);
					kakao.setSpecialGeneral2(specialGeneral2Kakao[i]);
					kakao.setEntrepreneur(entrepreneurKakao[i]);
					kakao.setEntrepreneurId(entrepreneurIdKakao[i]);
					kakao.setPublisher(publisherKakao[i]);
					kakao.setContractUid(contractUidKakao[i]);
					kakao.setContractCategory(contractCategoryKakao[i]);
					kakao.setContract(contractKakao[i]);
					kakao.setNewContract(newContractKakao[i]);
					kakao.setCategory(categoryKakao[i]);
					kakao.setSeriesId(seriesIdKakao[i]);
					kakao.setProductName(productNameKakao[i]);
					kakao.setProductCode(productCodeKakao[i]);
					kakao.setLable(lableKakao[i]);
					kakao.setAuthor(authorKakao[i]);
					kakao.setIsIsbn(isIsbnKakao[i]);
					kakao.setCpSettlementRateAndroid(cpSettlementRateAndroidKakao[i]);
					kakao.setCpSettlementRateIos(cpSettlementRateIosKakao[i]);
					kakao.setUseTicketType(useTicketTypeKakao[i]);
					kakao.setUseTicketAmount(useTicketAmountKakao[i]);
					kakao.setCacheSum(cacheSumKakao[i]);
					kakao.setCpp1(cpp1Kakao[i]);
					kakao.setCpl1(cpl1Kakao[i]);
					kakao.setCp1_1(cp1_1Kakao[i]);
					kakao.setCp2_1(cp2_1Kakao[i]);
					kakao.setCp3_1(cp3_1Kakao[i]);
					kakao.setCp7_1(cp7_1Kakao[i]);
					kakao.setCp8_1(cp8_1Kakao[i]);
					kakao.setCp9_1(cp9_1Kakao[i]);
					kakao.setEventCache1(eventCache1Kakao[i]);
					kakao.setOriginalSum(originalSumKakao[i]);
					kakao.setCpp2(cpp2Kakao[i]);
					kakao.setCpl2(cpl2Kakao[i]);
					kakao.setCp1_2(cp1_2Kakao[i]);
					kakao.setCp2_2(cp2_2Kakao[i]);
					kakao.setCp3_2(cp3_2Kakao[i]);
					kakao.setCp7_2(cp7_2Kakao[i]);
					kakao.setCp8_2(cp8_2Kakao[i]);
					kakao.setCp9_2(cp9_2Kakao[i]);
					kakao.setEventCache2(eventCache2Kakao[i]);
					kakao.setValueSupply(valueSupplyKakao[i]);
					kakao.setTaxAmount(taxAmountKakao[i]);
					kakao.setPayment(paymentKakao[i]);
					kakao.setWriterId(writerIdKakao[i]);
					kakao.setSetDate(setDateKakao[i]);
					
					eBookService.registerKakao(kakao);
				}
				break;
				
			case "p_kyobo":
				eBookService.allRemoveKyobo(setDate[0]);
				Kyobo kyobo = new Kyobo();
				
				String[] brandKyobo = request.getParameterValues("brand");
				String[] salesCategoryKyobo = request.getParameterValues("salesCategory");
				String[] salesTypeKyobo = request.getParameterValues("salesType");
				String[] productKyobo = request.getParameterValues("product");
				String[] mainSectionKyobo = request.getParameterValues("mainSection");
				String[] bookBarcodeKyobo = request.getParameterValues("bookBarcode");
				String[] isbnKyobo = request.getParameterValues("isbn");
				String[] epubIsbnKyobo = request.getParameterValues("epubIsbn");
				String[] pdfIsbnKyobo = request.getParameterValues("pdfIsbn");
				String[] productNameKyobo = request.getParameterValues("productName");
				String[] episodeKyobo = request.getParameterValues("episode");
				String[] authorKyobo = request.getParameterValues("author");
				String[] netPriceKyobo = request.getParameterValues("netPrice");
				String[] salePriceKyobo = request.getParameterValues("salePrice");
				String[] saleRateKyobo = request.getParameterValues("saleRate");
				String[] downloadKyobo = request.getParameterValues("download");
				String[] netTargetPriceKyobo = request.getParameterValues("netTargetPrice");
				String[] netTargetPriceAmountKyobo = request.getParameterValues("netTargetPriceAmount");
				String[] paymentKyobo = request.getParameterValues("payment");
				String[] accountingRatesKyobo = request.getParameterValues("accountingRates");
				String[] bookNumberKyobo = request.getParameterValues("bookNumber");
				String[] writerIdKyobo = request.getParameterValues("writerId");
				String[] setDateKyobo = request.getParameterValues("setDate");
				
				for(i=0; i<brandKyobo.length; i++) {
					kyobo.setBrand(brandKyobo[i]);
					kyobo.setSalesCategory(salesCategoryKyobo[i]);
					kyobo.setSalesType(salesTypeKyobo[i]);
					kyobo.setProduct(productKyobo[i]);
					kyobo.setMainSection(mainSectionKyobo[i]);
					kyobo.setBookBarcode(bookBarcodeKyobo[i]);
					kyobo.setIsbn(isbnKyobo[i]);
					kyobo.setEpubIsbn(epubIsbnKyobo[i]);
					kyobo.setPdfIsbn(pdfIsbnKyobo[i]);
					kyobo.setProductName(productNameKyobo[i]);
					kyobo.setEpisode(episodeKyobo[i]);
					kyobo.setAuthor(authorKyobo[i]);
					kyobo.setNetPrice(netPriceKyobo[i]);
					kyobo.setSalePrice(salePriceKyobo[i]);
					kyobo.setSaleRate(saleRateKyobo[i]);
					kyobo.setDownload(downloadKyobo[i]);
					kyobo.setNetTargetPrice(netTargetPriceKyobo[i]);
					kyobo.setNetTargetPriceAmount(netTargetPriceAmountKyobo[i]);
					kyobo.setPayment(paymentKyobo[i]);
					kyobo.setAccountingRates(accountingRatesKyobo[i]);
					kyobo.setBookNumber(bookNumberKyobo[i]);
					kyobo.setWriterId(writerIdKyobo[i]);
					kyobo.setSetDate(setDateKyobo[i]);
					
					eBookService.registerKyobo(kyobo);
				}
				break;
				
			case "p_mrblue":
				eBookService.allRemoveMrblue(setDate[0]);
				Mrblue mrblue = new Mrblue();
				
				String[] brandMrblue = request.getParameterValues("brand");
				String[] dateMrblue = request.getParameterValues("date");
				String[] productNameMrblue = request.getParameterValues("productName");
				String[] authorMrblue = request.getParameterValues("author");
				String[] codeMrblue = request.getParameterValues("code");
				String[] fixedNumMrblue = request.getParameterValues("fixedNum");
				String[] fixedSalesMrblue = request.getParameterValues("fixedSales");
				String[] fixedPaymentMrblue = request.getParameterValues("fixedPayment");
				String[] aAppOnerentalNumMrblue = request.getParameterValues("aAppOnerentalNum");
				String[] aAppOnerentalUseMrblue = request.getParameterValues("aAppOnerentalUse");
				String[] aAppOnerentalPaymentMrblue = request.getParameterValues("aAppOnerentalPayment");
				String[] aAppOnemngrNumMrblue = request.getParameterValues("aAppOnemngrNum");
				String[] aAppOnemngrUseMrblue = request.getParameterValues("aAppOnemngrUse");
				String[] aAppOnemngrPaymentMrblue = request.getParameterValues("aAppOnemngrPayment");
				String[] aAppAllrentalNumMrblue = request.getParameterValues("aAppAllrentalNum");
				String[] aAppAllrentalUseMrblue = request.getParameterValues("aAppAllrentalUse");
				String[] aAppAllrentalPaymentMrblue = request.getParameterValues("aAppAllrentalPayment");
				String[] aAppAllmngrNumMrblue = request.getParameterValues("aAppAllmngrNum");
				String[] aAppAllmngrUseMrblue = request.getParameterValues("aAppAllmngrUse");
				String[] aAppAllmngrPaymentMrblue = request.getParameterValues("aAppAllmngrPayment");
				String[] iAppOnerentalNumMrblue = request.getParameterValues("iAppOnerentalNum");
				String[] iAppOnerentalUseMrblue = request.getParameterValues("iAppOnerentalUse");
				String[] iAppOnerentalPaymentMrblue = request.getParameterValues("iAppOnerentalPayment");
				String[] iAppOnemngrNumMrblue = request.getParameterValues("iAppOnemngrNum");
				String[] iAppOnemngrUseMrblue = request.getParameterValues("iAppOnemngrUse");
				String[] iAppOnemngrPaymentMrblue = request.getParameterValues("iAppOnemngrPayment");
				String[] iAppAllrentalNumMrblue = request.getParameterValues("iAppAllrentalNum");
				String[] iAppAllrentalUseMrblue = request.getParameterValues("iAppAllrentalUse");
				String[] iAppAllrentalPaymentMrblue = request.getParameterValues("iAppAllrentalPayment");
				String[] iAppAllmngrNumMrblue = request.getParameterValues("iAppAllmngrNum");
				String[] iAppAllmngrUseMrblue = request.getParameterValues("iAppAllmngrUse");
				String[] iAppAllmngrPaymentMrblue = request.getParameterValues("iAppAllmngrPayment");
				String[] amountNumMrblue = request.getParameterValues("amountNum");
				String[] amountUseMrblue = request.getParameterValues("amountUse");
				String[] amountPaymentMrblue = request.getParameterValues("amountPayment");
				String[] amountSettlementMrblue = request.getParameterValues("amountSettlement");
				String[] salesMrblue = request.getParameterValues("sales");
				String[] paymentMrblue = request.getParameterValues("payment");
				String[] writerIdMrblue = request.getParameterValues("writerId");
				String[] setDateMrblue = request.getParameterValues("setDate");
				
				for(i=0; i<brandMrblue.length; i++) {
					fixedNumMrblue[i] = fixedNumMrblue[i].replaceAll(",", "");
					fixedSalesMrblue[i] = fixedSalesMrblue[i].replaceAll(",", "");
					fixedPaymentMrblue[i] = fixedPaymentMrblue[i].replaceAll(",", "");
					aAppOnerentalNumMrblue[i] = aAppOnerentalNumMrblue[i].replaceAll(",", "");
					aAppOnerentalUseMrblue[i] = aAppOnerentalUseMrblue[i].replaceAll(",", "");
					aAppOnerentalPaymentMrblue[i] = aAppOnerentalPaymentMrblue[i].replaceAll(",", "");
					aAppOnemngrNumMrblue[i] = aAppOnemngrNumMrblue[i].replaceAll(",", "");
					aAppOnemngrUseMrblue[i] = aAppOnemngrUseMrblue[i].replaceAll(",", "");
					aAppOnemngrPaymentMrblue[i] = aAppOnemngrPaymentMrblue[i].replaceAll(",", "");
					aAppAllrentalNumMrblue[i] = aAppAllrentalNumMrblue[i].replaceAll(",", "");
					aAppAllrentalUseMrblue[i] = aAppAllrentalUseMrblue[i].replaceAll(",", "");
					aAppAllrentalPaymentMrblue[i] = aAppAllrentalPaymentMrblue[i].replaceAll(",", "");
					aAppAllmngrNumMrblue[i] = aAppAllmngrNumMrblue[i].replaceAll(",", "");
					aAppAllmngrUseMrblue[i] = aAppAllmngrUseMrblue[i].replaceAll(",", "");
					aAppAllmngrPaymentMrblue[i] = aAppAllmngrPaymentMrblue[i].replaceAll(",", "");
					iAppOnerentalNumMrblue[i] = iAppOnerentalNumMrblue[i].replaceAll(",", "");
					iAppOnerentalUseMrblue[i] = iAppOnerentalUseMrblue[i].replaceAll(",", "");
					iAppOnerentalPaymentMrblue[i] = iAppOnerentalPaymentMrblue[i].replaceAll(",", "");
					iAppOnemngrNumMrblue[i] = iAppOnemngrNumMrblue[i].replaceAll(",", "");
					iAppOnemngrUseMrblue[i] = iAppOnemngrUseMrblue[i].replaceAll(",", "");
					iAppOnemngrPaymentMrblue[i] = iAppOnemngrPaymentMrblue[i].replaceAll(",", "");
					iAppAllrentalNumMrblue[i] = iAppAllrentalNumMrblue[i].replaceAll(",", "");
					iAppAllrentalUseMrblue[i] = iAppAllrentalUseMrblue[i].replaceAll(",", "");
					iAppAllrentalPaymentMrblue[i] = iAppAllrentalPaymentMrblue[i].replaceAll(",", "");
					iAppAllmngrNumMrblue[i] = iAppAllmngrNumMrblue[i].replaceAll(",", "");
					iAppAllmngrUseMrblue[i] = iAppAllmngrUseMrblue[i].replaceAll(",", "");
					iAppAllmngrPaymentMrblue[i] = iAppAllmngrPaymentMrblue[i].replaceAll(",", "");
					amountNumMrblue[i] = amountNumMrblue[i].replaceAll(",", "");
					amountUseMrblue[i] = amountUseMrblue[i].replaceAll(",", "");
					amountPaymentMrblue[i] = amountPaymentMrblue[i].replaceAll(",", "");
					amountSettlementMrblue[i] = amountSettlementMrblue[i].replaceAll(",", "");
					salesMrblue[i] = salesMrblue[i].replaceAll(",", "");
					paymentMrblue[i] = paymentMrblue[i].replaceAll(",", "");
					
					mrblue.setBrand(brandMrblue[i]);
					mrblue.setDate(dateMrblue[i]);
					mrblue.setProductName(productNameMrblue[i]);
					mrblue.setAuthor(authorMrblue[i]);
					mrblue.setCode(codeMrblue[i]);
					mrblue.setFixedNum(fixedNumMrblue[i]);
					mrblue.setFixedSales(fixedSalesMrblue[i]);
					mrblue.setFixedPayment(fixedPaymentMrblue[i]);
					mrblue.setAAppOnerentalNum(aAppOnerentalNumMrblue[i]);
					mrblue.setAAppOnerentalUse(aAppOnerentalUseMrblue[i]);
					mrblue.setAAppOnerentalPayment(aAppOnerentalPaymentMrblue[i]);
					mrblue.setAAppOnemngrNum(aAppOnemngrNumMrblue[i]);
					mrblue.setAAppOnemngrUse(aAppOnemngrUseMrblue[i]);
					mrblue.setAAppOnemngrPayment(aAppOnemngrPaymentMrblue[i]);
					mrblue.setAAppAllrentalNum(aAppAllrentalNumMrblue[i]);
					mrblue.setAAppAllrentalUse(aAppAllrentalUseMrblue[i]);
					mrblue.setAAppAllrentalPayment(aAppAllrentalPaymentMrblue[i]);
					mrblue.setAAppAllmngrNum(aAppAllmngrNumMrblue[i]);
					mrblue.setAAppAllmngrUse(aAppAllmngrUseMrblue[i]);
					mrblue.setAAppAllmngrPayment(aAppAllmngrPaymentMrblue[i]);
					mrblue.setIAppOnerentalNum(iAppOnerentalNumMrblue[i]);
					mrblue.setIAppOnerentalUse(iAppOnerentalUseMrblue[i]);
					mrblue.setIAppOnerentalPayment(iAppOnerentalPaymentMrblue[i]);
					mrblue.setIAppOnemngrNum(iAppOnemngrNumMrblue[i]);
					mrblue.setIAppOnemngrUse(iAppOnemngrUseMrblue[i]);
					mrblue.setIAppOnemngrPayment(iAppOnemngrPaymentMrblue[i]);
					mrblue.setIAppAllrentalNum(iAppAllrentalNumMrblue[i]);
					mrblue.setIAppAllrentalUse(iAppAllrentalUseMrblue[i]);
					mrblue.setIAppAllrentalPayment(iAppAllrentalPaymentMrblue[i]);
					mrblue.setIAppAllmngrNum(iAppAllmngrNumMrblue[i]);
					mrblue.setIAppAllmngrUse(iAppAllmngrUseMrblue[i]);
					mrblue.setIAppAllmngrPayment(iAppAllmngrPaymentMrblue[i]);
					mrblue.setAmountNum(amountNumMrblue[i]);
					mrblue.setAmountUse(amountUseMrblue[i]);
					mrblue.setAmountPayment(amountPaymentMrblue[i]);
					mrblue.setAmountSettlement(amountSettlementMrblue[i]);
					mrblue.setSales(salesMrblue[i]);
					mrblue.setPayment(paymentMrblue[i]);
					mrblue.setWriterId(writerIdMrblue[i]);
					mrblue.setSetDate(setDateMrblue[i]);
					
					eBookService.registerMrblue(mrblue);
				}
				break;
				
			case "p_munpia":
				eBookService.allRemoveMunpia(setDate[0]);
				Munpia munpia = new Munpia();
				
				String[] authorMunpia = request.getParameterValues("author");
				String[] brandMunpia = request.getParameterValues("brand");
				String[] dateMunpia = request.getParameterValues("date");
				String[] classificationMunpia = request.getParameterValues("classification");
				String[] productNameMunpia = request.getParameterValues("productName");
				String[] purchaseMunpia = request.getParameterValues("purchase");
				String[] purchaseCancelMunpia = request.getParameterValues("purchaseCancel");
				String[] rentalMunpia = request.getParameterValues("rental");
				String[] rentalCencelMunpia = request.getParameterValues("rentalCencel");
				String[] totalSalesMunpia = request.getParameterValues("totalSales");
				String[] totalSalesCencelMunpia = request.getParameterValues("totalSalesCencel");
				String[] realSaleMunpia = request.getParameterValues("realSale");
				String[] subPaymentMunpia = request.getParameterValues("subPayment");
				String[] iosMunpia = request.getParameterValues("ios");
				String[] paymentMunpia = request.getParameterValues("payment");
				String[] writerIdMunpia = request.getParameterValues("writerId");
				String[] setDateMunpia = request.getParameterValues("setDate");
				
				for(i=0; i<authorMunpia.length; i++) {
					totalSalesMunpia[i] = totalSalesMunpia[i].replaceAll(",", "");
					totalSalesCencelMunpia[i] = totalSalesCencelMunpia[i].replaceAll(",", "");
					realSaleMunpia[i] = realSaleMunpia[i].replaceAll(",", "");
					subPaymentMunpia[i] = subPaymentMunpia[i].replaceAll(",", "");
					iosMunpia[i] = iosMunpia[i].replaceAll(",", "");
					paymentMunpia[i] = paymentMunpia[i].replaceAll(",", "");
					
					munpia.setAuthor(authorMunpia[i]);
					munpia.setBrand(brandMunpia[i]);
					munpia.setDate(dateMunpia[i]);
					munpia.setClassification(classificationMunpia[i]);
					munpia.setProductName(productNameMunpia[i]);
					munpia.setPurchase(purchaseMunpia[i]);
					munpia.setPurchaseCancel(purchaseCancelMunpia[i]);
					munpia.setRental(rentalMunpia[i]);
					munpia.setRentalCencel(rentalCencelMunpia[i]);
					munpia.setTotalSales(totalSalesMunpia[i]);
					munpia.setTotalSalesCencel(totalSalesCencelMunpia[i]);
					munpia.setRealSale(realSaleMunpia[i]);
					munpia.setSubPayment(subPaymentMunpia[i]);
					munpia.setIos(iosMunpia[i]);
					munpia.setPayment(paymentMunpia[i]);
					munpia.setWriterId(writerIdMunpia[i]);
					munpia.setSetDate(setDateMunpia[i]);
					
					eBookService.registerMunpia(munpia);
				}
				break;
				
			case "p_naver":
				eBookService.allRemoveNaver(setDate[0]);
				Naver naver = new Naver();
				
				String[] productNameNaver = request.getParameterValues("productName");
				String[] productNoNaver = request.getParameterValues("productNo");
				String[] providerCodeNaver = request.getParameterValues("providerCode");
				String[] cpNameNaver = request.getParameterValues("cpName");
				String[] authorNaver = request.getParameterValues("author");
				String[] brandNaver = request.getParameterValues("brand");
				String[] ageLimitNaver = request.getParameterValues("ageLimit");
				String[] serviceDayNaver = request.getParameterValues("serviceDay");
				String[] rentalNumberNaver = request.getParameterValues("rentalNumber");
				String[] rentalNonpyNaver = request.getParameterValues("rentalNonpy");
				String[] rentalFreeNaver = request.getParameterValues("rentalFree");
				String[] rentalPaidCookieNaver = request.getParameterValues("rentalPaidCookie");
				String[] rentalFreeCookieNaver = request.getParameterValues("rentalFreeCookie");
				String[] rentalPaidICookieNaver = request.getParameterValues("rentalPaidICookie");
				String[] rentalFreeICookieNaver = request.getParameterValues("rentalFreeICookie");
				String[] ownNumberNaver = request.getParameterValues("ownNumber");
				String[] ownNonpyNaver = request.getParameterValues("ownNonpy");
				String[] ownFreeNaver = request.getParameterValues("ownFree");
				String[] ownPaidCookieNaver = request.getParameterValues("ownPaidCookie");
				String[] ownFreeCookieNaver = request.getParameterValues("ownFreeCookie");
				String[] ownPaidICookieNaver = request.getParameterValues("ownPaidICookie");
				String[] ownFreeICookieNaver = request.getParameterValues("ownFreeICookie");
				String[] cookieRentalNumberNaver = request.getParameterValues("cookieRentalNumber");
				String[] cookieRentalPaidNaver = request.getParameterValues("cookieRentalPaid");
				String[] cookieRentalFreeNaver = request.getParameterValues("cookieRentalFree");
				String[] cookieOwnNumberNaver = request.getParameterValues("cookieOwnNumber");
				String[] cookieOwnPaidNaver = request.getParameterValues("cookieOwnPaid");
				String[] cookieOwnFreeNaver = request.getParameterValues("cookieOwnFree");
				String[] iCookieRentalNumberNaver = request.getParameterValues("iCookieRentalNumber");
				String[] iCookieRentalPaidNaver = request.getParameterValues("iCookieRentalPaid");
				String[] iCookieRentalFreeNaver = request.getParameterValues("iCookieRentalFree");
				String[] iCookieOwnNumberNaver = request.getParameterValues("iCookieOwnNumber");
				String[] iCookieOwnPaidNaver = request.getParameterValues("iCookieOwnPaid");
				String[] iCookieOwnFreeNaver = request.getParameterValues("iCookieOwnFree");
				String[] iCookieFeeNaver = request.getParameterValues("iCookieFee");
				String[] sumNaver = request.getParameterValues("sum");
				String[] paymentNaver = request.getParameterValues("payment");
				String[] writerIdNaver = request.getParameterValues("writerId");
				String[] setDateNaver = request.getParameterValues("setDate");
				
				for(i=0; i<productNameNaver.length; i++) {
					rentalNumberNaver[i] = rentalNumberNaver[i].replaceAll(",", "");
					rentalNonpyNaver[i] = rentalNonpyNaver[i].replaceAll(",", "");
					rentalFreeNaver[i] = rentalFreeNaver[i].replaceAll(",", "");
					rentalPaidCookieNaver[i] = rentalPaidCookieNaver[i].replaceAll(",", "");
					rentalFreeCookieNaver[i] = rentalFreeCookieNaver[i].replaceAll(",", "");
					rentalPaidICookieNaver[i] = rentalPaidICookieNaver[i].replaceAll(",", "");
					rentalFreeICookieNaver[i] = rentalFreeICookieNaver[i].replaceAll(",", "");
					ownNumberNaver[i] = ownNumberNaver[i].replaceAll(",", "");
					ownNonpyNaver[i] = ownNonpyNaver[i].replaceAll(",", "");
					ownFreeNaver[i] = ownFreeNaver[i].replaceAll(",", "");
					ownPaidCookieNaver[i] = ownPaidCookieNaver[i].replaceAll(",", "");
					ownFreeCookieNaver[i] = ownFreeCookieNaver[i].replaceAll(",", "");
					ownPaidICookieNaver[i] = ownPaidICookieNaver[i].replaceAll(",", "");
					ownFreeICookieNaver[i] = ownFreeICookieNaver[i].replaceAll(",", "");
					cookieRentalNumberNaver[i] = cookieRentalNumberNaver[i].replaceAll(",", "");
					cookieRentalPaidNaver[i] = cookieRentalPaidNaver[i].replaceAll(",", "");
					cookieRentalFreeNaver[i] = cookieRentalFreeNaver[i].replaceAll(",", "");
					cookieOwnNumberNaver[i] = cookieOwnNumberNaver[i].replaceAll(",", "");
					cookieOwnPaidNaver[i] = cookieOwnPaidNaver[i].replaceAll(",", "");
					cookieOwnFreeNaver[i] = cookieOwnFreeNaver[i].replaceAll(",", "");
					iCookieRentalNumberNaver[i] = iCookieRentalNumberNaver[i].replaceAll(",", "");
					iCookieRentalPaidNaver[i] = iCookieRentalPaidNaver[i].replaceAll(",", "");
					iCookieRentalFreeNaver[i] = iCookieRentalFreeNaver[i].replaceAll(",", "");
					iCookieOwnNumberNaver[i] = iCookieOwnNumberNaver[i].replaceAll(",", "");
					iCookieOwnPaidNaver[i] = iCookieOwnPaidNaver[i].replaceAll(",", "");
					iCookieOwnFreeNaver[i] = iCookieOwnFreeNaver[i].replaceAll(",", "");
					iCookieFeeNaver[i] = iCookieFeeNaver[i].replaceAll(",", "");
					sumNaver[i] = sumNaver[i].replaceAll(",", "");
					paymentNaver[i] = paymentNaver[i].replaceAll(",", "");
					
					naver.setProductName(productNameNaver[i]);
					naver.setProductNo(productNoNaver[i]);
					naver.setProviderCode(providerCodeNaver[i]);
					naver.setCpName(cpNameNaver[i]);
					naver.setAuthor(authorNaver[i]);
					naver.setBrand(brandNaver[i]);
					naver.setAgeLimit(ageLimitNaver[i]);
					naver.setServiceDay(serviceDayNaver[i]);
					naver.setRentalNumber(rentalNumberNaver[i]);
					naver.setRentalNonpy(rentalNonpyNaver[i]);
					naver.setRentalFree(rentalFreeNaver[i]);
					naver.setRentalPaidCookie(rentalPaidCookieNaver[i]);
					naver.setRentalFreeCookie(rentalFreeCookieNaver[i]);
					naver.setRentalPaidICookie(rentalPaidICookieNaver[i]);
					naver.setRentalFreeICookie(rentalFreeICookieNaver[i]);
					naver.setOwnNumber(ownNumberNaver[i]);
					naver.setOwnNonpy(ownNonpyNaver[i]);
					naver.setOwnFree(ownFreeNaver[i]);
					naver.setOwnPaidCookie(ownPaidCookieNaver[i]);
					naver.setOwnFreeCookie(ownFreeCookieNaver[i]);
					naver.setOwnPaidICookie(ownPaidICookieNaver[i]);
					naver.setOwnFreeICookie(ownFreeICookieNaver[i]);
					naver.setCookieRentalNumber(cookieRentalNumberNaver[i]);
					naver.setCookieRentalPaid(cookieRentalPaidNaver[i]);
					naver.setCookieRentalFree(cookieRentalFreeNaver[i]);
					naver.setCookieOwnNumber(cookieOwnNumberNaver[i]);
					naver.setCookieOwnPaid(cookieOwnPaidNaver[i]);
					naver.setCookieOwnFree(cookieOwnFreeNaver[i]);
					naver.setICookieRentalNumber(iCookieRentalNumberNaver[i]);
					naver.setICookieRentalPaid(iCookieRentalPaidNaver[i]);
					naver.setICookieRentalFree(iCookieRentalFreeNaver[i]);
					naver.setICookieOwnNumber(iCookieOwnNumberNaver[i]);
					naver.setICookieOwnPaid(iCookieOwnPaidNaver[i]);
					naver.setICookieOwnFree(iCookieOwnFreeNaver[i]);
					naver.setICookieFee(iCookieFeeNaver[i]);
					naver.setSum(sumNaver[i]);
					naver.setPayment(paymentNaver[i]);
					naver.setWriterId(writerIdNaver[i]);
					naver.setSetDate(setDateNaver[i]);
					
					eBookService.registerNaver(naver);
				}
				break;
				
			case "p_ridibooks":
				eBookService.allRemoveRidibooks(setDate[0]);
				Ridibooks ridibooks = new Ridibooks();
				
				String[] salesRidibooks = request.getParameterValues("sales");
				String[] seriesIdRidibooks = request.getParameterValues("seriesId");
				String[] bookIdRidibooks = request.getParameterValues("bookId");
				String[] productNameRidibooks = request.getParameterValues("productName");
				String[] episodeRidibooks = request.getParameterValues("episode");
				String[] authorRidibooks = request.getParameterValues("author");
				String[] translatorsRidibooks = request.getParameterValues("translators");
				String[] pictureRidibooks = request.getParameterValues("picture");
				String[] brandRidibooks = request.getParameterValues("brand");
				String[] seriesNameRidibooks = request.getParameterValues("seriesName");
				String[] ebooksPriceRidibooks = request.getParameterValues("ebooksPrice");
				String[] singleSaleSalesRidibooks = request.getParameterValues("singleSaleSales");
				String[] singleSaleTicketRidibooks = request.getParameterValues("singleSaleTicket");
				String[] singleFreeTicketRidibooks = request.getParameterValues("singleFreeTicket");
				String[] rentalAmountRidibooks = request.getParameterValues("rentalAmount");
				String[] rentalTicketRidibooks = request.getParameterValues("rentalTicket");
				String[] rentalFreeTicketRidibooks = request.getParameterValues("rentalFreeTicket");
				String[] freeDownAmountRidibooks = request.getParameterValues("freeDownAmount");
				String[] freeDownTicketRidibooks = request.getParameterValues("freeDownTicket");
				String[] setSaleAmountRidibooks = request.getParameterValues("setSaleAmount");
				String[] setSaleTicketRidibooks = request.getParameterValues("setSaleTicket");
				String[] setRentalAmountRidibooks = request.getParameterValues("setRentalAmount");
				String[] setRentalTicketRidibooks = request.getParameterValues("setRentalTicket");
				String[] cancelAmountRidibooks = request.getParameterValues("cancelAmount");
				String[] cancelTicketRidibooks = request.getParameterValues("cancelTicket");
				String[] cancelSingleSaleSalesRidibooks = request.getParameterValues("cancelSingleSaleSales");
				String[] cancelSingleSaleTicketRidibooks = request.getParameterValues("cancelSingleSaleTicket");
				String[] cancelRentalAmountRidibooks = request.getParameterValues("cancelRentalAmount");
				String[] cancelRentalTicketRidibooks = request.getParameterValues("cancelRentalTicket");
				String[] cancelFreeDownAmountRidibooks = request.getParameterValues("cancelFreeDownAmount");
				String[] cancelFreeDownTicketRidibooks = request.getParameterValues("cancelFreeDownTicket");
				String[] cancelSetSaleAmountRidibooks = request.getParameterValues("cancelSetSaleAmount");
				String[] cancelSetSaleTicketRidibooks = request.getParameterValues("cancelSetSaleTicket");
				String[] cancelSetRentalAmountRidibooks = request.getParameterValues("cancelSetRentalAmount");
				String[] cancelSetRentalTicketRidibooks = request.getParameterValues("cancelSetRentalTicket");
				String[] paymentRidibooks = request.getParameterValues("payment");
				String[] paperbookIsbn10Ridibooks = request.getParameterValues("paperbookIsbn10");
				String[] paperbookIsbn13Ridibooks = request.getParameterValues("paperbookIsbn13");
				String[] ebookIsbn10Ridibooks = request.getParameterValues("ebookIsbn10");
				String[] ebookIsbn13Ridibooks = request.getParameterValues("ebookIsbn13");
				String[] cpManageIdRidibooks = request.getParameterValues("cpManageId");
				String[] category1Ridibooks = request.getParameterValues("category1");
				String[] category2Ridibooks = request.getParameterValues("category2");
				String[] writerIdRidibooks = request.getParameterValues("writerId");
				String[] setDateRidibooks = request.getParameterValues("setDate");
				
				for(i=0; i<seriesIdRidibooks.length; i++) {
					paymentRidibooks[i] = paymentRidibooks[i].replaceAll(",", "");
					
					ridibooks.setSales(salesRidibooks[i]);
					ridibooks.setSeriesId(seriesIdRidibooks[i]);
					ridibooks.setBookId(bookIdRidibooks[i]);
					ridibooks.setProductName(productNameRidibooks[i]);
					ridibooks.setEpisode(episodeRidibooks[i]);
					ridibooks.setAuthor(authorRidibooks[i]);
					ridibooks.setTranslators(translatorsRidibooks[i]);
					ridibooks.setPicture(pictureRidibooks[i]);
					ridibooks.setBrand(brandRidibooks[i]);
					ridibooks.setSeriesName(seriesNameRidibooks[i]);
					ridibooks.setEbooksPrice(ebooksPriceRidibooks[i]);
					ridibooks.setSingleSaleSales(singleSaleSalesRidibooks[i]);
					ridibooks.setSingleSaleTicket(singleSaleTicketRidibooks[i]);
					ridibooks.setSingleFreeTicket(singleFreeTicketRidibooks[i]);
					ridibooks.setRentalAmount(rentalAmountRidibooks[i]);
					ridibooks.setRentalTicket(rentalTicketRidibooks[i]);
					ridibooks.setRentalFreeTicket(rentalFreeTicketRidibooks[i]);
					ridibooks.setFreeDownAmount(freeDownAmountRidibooks[i]);
					ridibooks.setFreeDownTicket(freeDownTicketRidibooks[i]);
					ridibooks.setSetSaleAmount(setSaleAmountRidibooks[i]);
					ridibooks.setSetSaleTicket(setSaleTicketRidibooks[i]);
					ridibooks.setSetRentalAmount(setRentalAmountRidibooks[i]);
					ridibooks.setSetRentalTicket(setRentalTicketRidibooks[i]);
					ridibooks.setCancelAmount(cancelAmountRidibooks[i]);
					ridibooks.setCancelTicket(cancelTicketRidibooks[i]);
					ridibooks.setCancelSingleSaleSales(cancelSingleSaleSalesRidibooks[i]);
					ridibooks.setCancelSingleSaleTicket(cancelSingleSaleTicketRidibooks[i]);
					ridibooks.setCancelRentalAmount(cancelRentalAmountRidibooks[i]);
					ridibooks.setCancelRentalTicket(cancelRentalTicketRidibooks[i]);
					ridibooks.setCancelFreeDownAmount(cancelFreeDownAmountRidibooks[i]);
					ridibooks.setCancelFreeDownTicket(cancelFreeDownTicketRidibooks[i]);
					ridibooks.setCancelSetSaleAmount(cancelSetSaleAmountRidibooks[i]);
					ridibooks.setCancelSetSaleTicket(cancelSetSaleTicketRidibooks[i]);
					ridibooks.setCancelSetRentalAmount(cancelSetRentalAmountRidibooks[i]);
					ridibooks.setCancelSetRentalTicket(cancelSetRentalTicketRidibooks[i]);
					ridibooks.setPayment(paymentRidibooks[i]);
					ridibooks.setPaperbookIsbn10(paperbookIsbn10Ridibooks[i]);
					ridibooks.setPaperbookIsbn13(paperbookIsbn13Ridibooks[i]);
					ridibooks.setEbookIsbn10(ebookIsbn10Ridibooks[i]);
					ridibooks.setEbookIsbn13(ebookIsbn13Ridibooks[i]);
					ridibooks.setCpManageId(cpManageIdRidibooks[i]);
					ridibooks.setCategory1(category1Ridibooks[i]);
					ridibooks.setCategory2(category2Ridibooks[i]);
					ridibooks.setWriterId(writerIdRidibooks[i]);
					ridibooks.setSetDate(setDateRidibooks[i]);
					
					eBookService.registerRidibooks(ridibooks);
				}
				break;
				
			case "p_romance":
				eBookService.allRemoveRomance(setDate[0]);
				Romance romance = new Romance();
				
				String[] bookCodeRomance = request.getParameterValues("bookCode");
				String[] typeRomance = request.getParameterValues("type");
				String[] productNameRomance = request.getParameterValues("productName");
				String[] brandRomance = request.getParameterValues("brand");
				String[] authorRomance = request.getParameterValues("author");
				String[] saleRateRomance = request.getParameterValues("saleRate");
				String[] internetSalesRomance = request.getParameterValues("internetSales");
				String[] paymentRomance = request.getParameterValues("payment");
				String[] regDateRomance = request.getParameterValues("regDate");
				String[] dealerRomance = request.getParameterValues("dealer");
				String[] isbnRomance = request.getParameterValues("isbn");
				String[] writerIdRomance = request.getParameterValues("writerId");
				String[] setDateRomance = request.getParameterValues("setDate");
				
				for(i=0; i<bookCodeRomance.length; i++) {
					internetSalesRomance[i] = internetSalesRomance[i].replaceAll(",", "");
					paymentRomance[i] = paymentRomance[i].replaceAll(",", "");
					
					romance.setBookCode(bookCodeRomance[i]);
					romance.setType(typeRomance[i]);
					romance.setProductName(productNameRomance[i]);
					romance.setBrand(brandRomance[i]);
					romance.setAuthor(authorRomance[i]);
					romance.setSaleRate(saleRateRomance[i]);
					romance.setInternetSales(internetSalesRomance[i]);
					romance.setPayment(paymentRomance[i]);
					romance.setRegDate(regDateRomance[i]);
					romance.setDealer(dealerRomance[i]);
					romance.setIsbn(isbnRomance[i]);
					romance.setWriterId(writerIdRomance[i]);
					romance.setSetDate(setDateRomance[i]);
					
					eBookService.registerRomance(romance);
				}
				break;
				
			case "p_tocsoda":
				eBookService.allRemoveTocsoda(setDate[0]);
				Tocsoda tocsoda = new Tocsoda();
				
				String[] productBacordTocsoda = request.getParameterValues("productBacord");
				String[] productNameTocsoda = request.getParameterValues("productName");
				String[] authorTocsoda = request.getParameterValues("author");
				String[] brandTocsoda = request.getParameterValues("brand");
				String[] pcTocsoda = request.getParameterValues("pc");
				String[] androidTocsoda = request.getParameterValues("android");
				String[] iosTocsoda = request.getParameterValues("ios");
				String[] iosIapTocsoda = request.getParameterValues("iosIap");
				String[] sumTocsoda = request.getParameterValues("sum");
				String[] paymentTocsoda = request.getParameterValues("payment");
				String[] writerIdTocsoda = request.getParameterValues("writerId");
				String[] setDateTocsoda = request.getParameterValues("setDate");
				
				for(i=0; i<productBacordTocsoda.length; i++) {
					pcTocsoda[i] = pcTocsoda[i].replaceAll(",", "");
					androidTocsoda[i] = androidTocsoda[i].replaceAll(",", "");
					iosTocsoda[i] = iosTocsoda[i].replaceAll(",", "");
					iosIapTocsoda[i] = iosIapTocsoda[i].replaceAll(",", "");
					sumTocsoda[i] = sumTocsoda[i].replaceAll(",", "");
					paymentTocsoda[i] = paymentTocsoda[i].replaceAll(",", "");
					
					tocsoda.setProductBacord(productBacordTocsoda[i]);
					tocsoda.setProductName(productNameTocsoda[i]);
					tocsoda.setAuthor(authorTocsoda[i]);
					tocsoda.setBrand(brandTocsoda[i]);
					tocsoda.setPc(pcTocsoda[i]);
					tocsoda.setAndroid(androidTocsoda[i]);
					tocsoda.setIos(iosTocsoda[i]);
					tocsoda.setIosIap(iosIapTocsoda[i]);
					tocsoda.setSum(sumTocsoda[i]);
					tocsoda.setPayment(paymentTocsoda[i]);
					tocsoda.setWriterId(writerIdTocsoda[i]);
					tocsoda.setSetDate(setDateTocsoda[i]);
					
					eBookService.registerTocsoda(tocsoda);
				}
				break;
				
			case "p_winstore":
				eBookService.allRemoveWinstore(setDate[0]);
				Winstore winstore = new Winstore();
				
				String[] partnerNameWinstore = request.getParameterValues("partnerName");
				String[] partnerIdWinstore = request.getParameterValues("partnerId");
				String[] entrepreneurWinstore = request.getParameterValues("entrepreneur");
				String[] channelProductIdWinstore = request.getParameterValues("channelProductId");
				String[] channelProductNameWinstore = request.getParameterValues("channelProductName");
				String[] productNameWinstore = request.getParameterValues("productName");
				String[] episodeWinstore = request.getParameterValues("episode");
				String[] productIdWinstore = request.getParameterValues("productId");
				String[] secondSettlementStatusWinstore = request.getParameterValues("secondSettlementStatus");
				String[] categoryWinstore = request.getParameterValues("category");
				String[] brandWinstore = request.getParameterValues("brand");
				String[] authorWinstore = request.getParameterValues("author");
				String[] pictureAuthorWinstore = request.getParameterValues("pictureAuthor");
				String[] typeWinstore = request.getParameterValues("type");
				String[] saleRateWinstore = request.getParameterValues("saleRate");
				String[] cencelRateWinstore = request.getParameterValues("cencelRate");
				String[] settlementTargetAmountWinstore = request.getParameterValues("settlementTargetAmount");
				String[] salesRateWinstore = request.getParameterValues("salesRate");
				String[] salesAmountWinstore = request.getParameterValues("salesAmount");
				String[] cencelsRateWinstore = request.getParameterValues("cencelsRate");
				String[] cencelsAmountWinstore = request.getParameterValues("cencelsAmount");
				String[] sumWinstore = request.getParameterValues("sum");
				String[] customerPaymentWinstore = request.getParameterValues("customerPayment");
				String[] customerPaymentReceiptWinstore = request.getParameterValues("customerPaymentReceipt");
				String[] customerPaymentEtcWinstore = request.getParameterValues("customerPaymentEtc");
				String[] customerPaymentServiceWinstore = request.getParameterValues("customerPaymentService");
				String[] customerPaymentServiceEtcWinstore = request.getParameterValues("customerPaymentServiceEtc");
				String[] serviceDeductionWinstore = request.getParameterValues("serviceDeduction");
				String[] paymentWinstore = request.getParameterValues("payment");
				String[] paymentTypeWinstore = request.getParameterValues("paymentType");
				String[] writerIdWinstore = request.getParameterValues("writerId");
				String[] setDateWinstore = request.getParameterValues("setDate");
				
				for(i=0; i<partnerNameWinstore.length; i++) {
					settlementTargetAmountWinstore[i] = settlementTargetAmountWinstore[i].replaceAll(",", "");
					salesAmountWinstore[i] = salesAmountWinstore[i].replaceAll(",", "");
					cencelsAmountWinstore[i] = cencelsAmountWinstore[i].replaceAll(",", "");
					sumWinstore[i] = sumWinstore[i].replaceAll(",", "");
					customerPaymentWinstore[i] = customerPaymentWinstore[i].replaceAll(",", "");
					customerPaymentReceiptWinstore[i] = customerPaymentReceiptWinstore[i].replaceAll(",", "");
					customerPaymentEtcWinstore[i] = customerPaymentEtcWinstore[i].replaceAll(",", "");
					customerPaymentServiceWinstore[i] = customerPaymentServiceWinstore[i].replaceAll(",", "");
					customerPaymentServiceEtcWinstore[i] = customerPaymentServiceEtcWinstore[i].replaceAll(",", "");
					serviceDeductionWinstore[i] = serviceDeductionWinstore[i].replaceAll(",", "");
					paymentWinstore[i] = paymentWinstore[i].replaceAll(",", "");
					
					winstore.setPartnerName(partnerNameWinstore[i]);
					winstore.setPartnerId(partnerIdWinstore[i]);
					winstore.setEntrepreneur(entrepreneurWinstore[i]);
					winstore.setChannelProductId(channelProductIdWinstore[i]);
					winstore.setChannelProductName(channelProductNameWinstore[i]);
					winstore.setProductName(productNameWinstore[i]);
					winstore.setEpisode(episodeWinstore[i]);
					winstore.setProductId(productIdWinstore[i]);
					winstore.setSecondSettlementStatus(secondSettlementStatusWinstore[i]);
					winstore.setCategory(categoryWinstore[i]);
					winstore.setBrand(brandWinstore[i]);
					winstore.setAuthor(authorWinstore[i]);
					winstore.setPictureAuthor(pictureAuthorWinstore[i]);
					winstore.setType(typeWinstore[i]);
					winstore.setSaleRate(saleRateWinstore[i]);
					winstore.setCencelRate(cencelRateWinstore[i]);
					winstore.setSettlementTargetAmount(settlementTargetAmountWinstore[i]);
					winstore.setSalesRate(salesRateWinstore[i]);
					winstore.setSalesAmount(salesAmountWinstore[i]);
					winstore.setCencelsRate(cencelsRateWinstore[i]);
					winstore.setCencelsAmount(cencelsAmountWinstore[i]);
					winstore.setSum(sumWinstore[i]);
					winstore.setCustomerPayment(customerPaymentWinstore[i]);
					winstore.setCustomerPaymentReceipt(customerPaymentReceiptWinstore[i]);
					winstore.setCustomerPaymentEtc(customerPaymentEtcWinstore[i]);
					winstore.setCustomerPaymentService(customerPaymentServiceWinstore[i]);
					winstore.setCustomerPaymentServiceEtc(customerPaymentServiceEtcWinstore[i]);
					winstore.setServiceDeduction(serviceDeductionWinstore[i]);
					winstore.setPayment(paymentWinstore[i]);
					winstore.setPaymentType(paymentTypeWinstore[i]);
					winstore.setWriterId(writerIdWinstore[i]);
					winstore.setSetDate(setDateWinstore[i]);
					
					eBookService.registerWinstore(winstore);
				}
				break;
				
			case "p_yes24":
				eBookService.allRemoveYes24(setDate[0]);
				Yes24 yes24 = new Yes24();
				
				String[] noYes24 = request.getParameterValues("no");
				String[] productNameYes24 = request.getParameterValues("productName");
				String[] brandYes24 = request.getParameterValues("brand");
				String[] brandSalePriceYes24 = request.getParameterValues("brandSalePrice");
				String[] salesYes24 = request.getParameterValues("sales");
				String[] salesSalePriceYes24 = request.getParameterValues("salesSalePrice");
				String[] salesRefundPriceYes24 = request.getParameterValues("salesRefundPrice");
				String[] costRateYes24 = request.getParameterValues("costRate");
				String[] paymentYes24 = request.getParameterValues("payment");
				String[] eventTypeYes24 = request.getParameterValues("eventType");
				String[] saleTypeYes24 = request.getParameterValues("saleType");
				String[] eventSalePriceYes24 = request.getParameterValues("eventSalePrice");
				String[] authorYes24 = request.getParameterValues("author");
				String[] bookIdYes24 = request.getParameterValues("bookId");
				String[] epublIdYes24 = request.getParameterValues("epublId");
				String[] setCodeYes24 = request.getParameterValues("setCode");
				String[] paperbookIsbnYes24 = request.getParameterValues("paperbookIsbn");
				String[] ebookIsbnYes24 = request.getParameterValues("ebookIsbn");
				String[] saleDateYes24 = request.getParameterValues("saleDate");
				String[] refundDateYes24 = request.getParameterValues("refundDate");
				String[] writerIdYes24 = request.getParameterValues("writerId");
				String[] setDateYes24 = request.getParameterValues("setDate");
				
				for(i=0; i<noYes24.length; i++) {
					brandSalePriceYes24[i] = brandSalePriceYes24[i].replaceAll(",", "");
					salesSalePriceYes24[i] = salesSalePriceYes24[i].replaceAll(",", "");
					salesRefundPriceYes24[i] = salesRefundPriceYes24[i].replaceAll(",", "");
					paymentYes24[i] = paymentYes24[i].replaceAll(",", "");
					eventSalePriceYes24[i] = eventSalePriceYes24[i].replaceAll(",", "");
					
					yes24.setNo(noYes24[i]);
					yes24.setProductName(productNameYes24[i]);
					yes24.setBrand(brandYes24[i]);
					yes24.setBrandSalePrice(brandSalePriceYes24[i]);
					yes24.setSales(salesYes24[i]);
					yes24.setSalesSalePrice(salesSalePriceYes24[i]);
					yes24.setSalesRefundPrice(salesRefundPriceYes24[i]);
					yes24.setCostRate(costRateYes24[i]);
					yes24.setPayment(paymentYes24[i]);
					yes24.setEventType(eventTypeYes24[i]);
					yes24.setSaleType(saleTypeYes24[i]);
					yes24.setEventSalePrice(eventSalePriceYes24[i]);
					yes24.setAuthor(authorYes24[i]);
					yes24.setEpublId(epublIdYes24[i]);
					yes24.setSetCode(setCodeYes24[i]);
					yes24.setBookId(bookIdYes24[i]);
					yes24.setPaperbookIsbn(paperbookIsbnYes24[i]);
					yes24.setEbookIsbn(ebookIsbnYes24[i]);
					yes24.setSaleDate(saleDateYes24[i]);
					yes24.setRefundDate(refundDateYes24[i]);
					yes24.setWriterId(writerIdYes24[i]);
					yes24.setSetDate(setDateYes24[i]);
					
					eBookService.registerYes24(yes24);
				}
				break;
				
			case "p_aladin":
				eBookService.allRemoveAladin(setDate[0]);
				Aladin aladin = new Aladin();
				
				String[] salesCancelDateAladin = request.getParameterValues("salesCancelDate");
				String[] itemIdAladin = request.getParameterValues("itemId");
				String[] productNameAladin = request.getParameterValues("productName");
				String[] salesTypeAladin = request.getParameterValues("salesType");
				String[] netPriceAladin = request.getParameterValues("netPrice");
				String[] salePriceAladin = request.getParameterValues("salePrice");
				String[] paymentAladin = request.getParameterValues("payment");
				String[] cancelDateAladin = request.getParameterValues("cancelDate");
				String[] authorAladin = request.getParameterValues("author");
				String[] brandAladin = request.getParameterValues("brand");
				String[] publisherAladin = request.getParameterValues("publisher");
				String[] isbnAladin = request.getParameterValues("isbn");
				String[] cidAladin = request.getParameterValues("cid");
				String[] writerIdAladin = request.getParameterValues("writerId");
				String[] setDateAladin = request.getParameterValues("setDate");
				
				for(i=0; i<salesCancelDateAladin.length; i++) {
					netPriceAladin[i] = netPriceAladin[i].replaceAll(",", "");
					salePriceAladin[i] = salePriceAladin[i].replaceAll(",", "");
					paymentAladin[i] = paymentAladin[i].replaceAll(",", "");
					
					aladin.setSalesCancelDate(salesCancelDateAladin[i]);
					aladin.setItemId(itemIdAladin[i]);
					aladin.setProductName(productNameAladin[i]);
					aladin.setSalesType(salesTypeAladin[i]);
					aladin.setNetPrice(netPriceAladin[i]);
					aladin.setSalePrice(salePriceAladin[i]);
					aladin.setPayment(paymentAladin[i]);
					aladin.setCancelDate(cancelDateAladin[i]);
					aladin.setAuthor(authorAladin[i]);
					aladin.setBrand(brandAladin[i]);
					aladin.setPublisher(publisherAladin[i]);
					aladin.setIsbn(isbnAladin[i]);
					aladin.setCid(cidAladin[i]);
					aladin.setWriterId(writerIdAladin[i]);
					aladin.setSetDate(setDateAladin[i]);
					
					eBookService.registerAladin(aladin);
				}
				break;

				
				
				
			default:
				break;
			}
			
			
			
			model.addAttribute("result", true);
			
			// mv.addObject("result", true);
		} catch (Exception e) {
			model.addAttribute("result", false);
			//mv.addObject("result", false);
			e.printStackTrace();
		}
//		
//		// return mv;
		return "redirect:/ebook/list?platformType=" + p_type;
	
	}
	
	
	private String[] divStr(String fullStr) {
		String[] str = null;
		
		str = fullStr.split(",");
		
		return str;
	}
	
}












