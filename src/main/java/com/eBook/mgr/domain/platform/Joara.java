package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Joara {
	String idx;
	
	String brand;							//브랜드
	String productName;						//작품명
	String productCode;						//작품코드
	String author;							//작가명
	String unit;							//단가
	String saleAmount;						//판매건수
	String ratio;							//비율
	String payment;							//정산금액
	String paymentDate;						//정산일
	String writerId;						//작가id
	String setDate;							//기준월
}
