package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Epub {
	String idx;
	
	String indexNum; 							//번호
	String brand;								//브랜드
	String productName; 						//도서명
	String publisher; 							//출판사
	String publisherAmount; 					//출판사판매가
	String sales; 								//판매서점
	String salesAmount; 						//서점판매가
	String cencelsAmount; 						//서점환불가
	String costRate; 							//원가율
	String payment; 							//출판사정산액
	String eventType; 							//이벤트구분
	String salesType; 							//판매구분
	String eventAmount; 						//이벤트판매가
	String author; 								//작가명
	String epubid; 								//ePubID
	String setCode; 							//세트코드
	String paperbookIsbn; 						//종이책ISBN
	String ebookIsbn; 							//전자책ISBN
	String salseDate; 							//판매일
	String cencelsDate; 						//환불일
	String writerId; 							//작가id
	String setDate; 								//기준월
}
