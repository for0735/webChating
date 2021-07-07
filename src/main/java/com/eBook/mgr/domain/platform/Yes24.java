package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Yes24 {
	String idx;
	
	String no;									//no
	String productName;							//도서명
	String brand;								//출판사
	String brandSalePrice;						//출판사판매가
	String sales;								//판매서점
	String salesSalePrice;						//서점판매가
	String salesRefundPrice;					//서점환불가
	String costRate;							//원가율
	String payment;								//출판사정산액
	String eventType;							//이벤트구분
	String saleType;							//판매구분
	String eventSalePrice;						//이벤트판매가
	String author;								//저자명
	String bookId;								//bookID
	String epublId;								//ePubID
	String setCode;								//세트코드
	String paperbookIsbn;						//종이책isbn
	String ebookIsbn;							//전자책isbn
	String saleDate;							//판매일
	String refundDate;							//환불일
	String writerId;							//작가id
	String setDate;								//기준월
}
