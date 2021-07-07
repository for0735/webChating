package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Aladin {
	String idx;
	
	String salesCancelDate;			//판매/취소일시
	String itemId;					//ItemId
	String productName;				//도서명
	String salesType;				//판매형태
	String netPrice;				//정가가
	String salePrice;				//판매가
	String payment;					//정산액
	String cancelDate;				//취소주문일시
	String author;					//저자명
	String brand;					//출판사명
	String publisher;				//정산처명
	String isbn;					//isbn
	String cid;						//CID
	String writerId;				//작가id
	String setDate;					//기준월
}
