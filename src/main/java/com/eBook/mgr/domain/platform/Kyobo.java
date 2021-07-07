package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Kyobo {
	String idx;
	
	String brand;								//브랜드
	String salesCategory;						//판매구분
	String salesType;							//판매형태
	String product;								//상품
	String mainSection;							//대분류
	String bookBarcode;							//도서바코드
	String isbn;								//ISBN
	String epubIsbn;							//EPUB ISBN
	String pdfIsbn;								//PDF ISBN
	String productName;							//상품명
	String episode;								//회차명
	String author;								//저자명
	String netPrice;							//정가
	String salePrice;							//판매가
	String saleRate;							//판매량
	String download;							//다운로드수
	String netTargetPrice;						//정산대상판매가
	String netTargetPriceAmount;				//정산대상판매총액
	String payment;								//지불액
	String accountingRates;						//정산율(%)
	String bookNumber;							//북넘버
	String writerId; 							//작가id
	String setDate; 								//기준월
}	
