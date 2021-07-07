package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Romance {
	String idx;
	
	String bookCode;						//도서코드
	String type;							//구분
	String productName;						//상품명
	String brand;							//출판사명
	String author;							//저자명
	String saleRate;						//판매수
	String internetSales;					//인터넷판매액
	String payment;							//출판사정산액
	String regDate;							//등록일
	String dealer;							//판매처
	String isbn;							//isbn
	String writerId;						//작가id
	String setDate;							//기준월
}
