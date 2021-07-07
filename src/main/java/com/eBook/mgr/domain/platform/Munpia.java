package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Munpia {
	String idx;
	
	String author;							//작가
	String brand;							//브랜드
	String date;							//날짜
	String classification;					//분류
	String productName;						//작품
	String purchase;						//구매
	String purchaseCancel;					//구매취소
	String rental;							//대여
	String rentalCencel;					//대여취소
	String totalSales;						//총판매
	String totalSalesCencel;				//총판매취소
	String realSale;						//실판매
	String subPayment;						//정산
	String ios;								//IOS
	String payment;							//최종정산
	
	String writerId;						//작가id
	String setDate;							//기준월
}
