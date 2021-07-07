package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Mrblue {
	String idx;
	
	String brand;							//브랜드
	String date;							//일자
	String productName;						//작품명
	String author;							//작가명
	String code;							//코드
	String fixedNum;						//정액_건수
	String fixedSales;						//정액_매출액
	String fixedPayment;					//정액_정산액
	String aAppOnerentalNum;				//a앱머니_권별대여_건
	String aAppOnerentalUse;				//a앱머니_권별대여_이용권
	String aAppOnerentalPayment;			//a앱머니_권별대여_매출액
	String aAppOnemngrNum;					//a앱머니_권별소장_건
	String aAppOnemngrUse;					//a앱머니_권별소장_이용권
	String aAppOnemngrPayment;				//a앱머니_권별소장_매출액
	String aAppAllrentalNum;				//a앱머니_전권대여_건
	String aAppAllrentalUse;				//a앱머니_전권대여_이용권
	String aAppAllrentalPayment;			//a앱머니_전권대여_매출액
	String aAppAllmngrNum;					//a앱머니_전권소장_건
	String aAppAllmngrUse;					//a앱머니_전권소장_이용권
	String aAppAllmngrPayment;				//a앱머니_전권소장_매출액'
	String iAppOnerentalNum;				//i앱머니_권별대여_건
	String iAppOnerentalUse;				//i앱머니_권별대여_이용권
	String iAppOnerentalPayment;			//i앱머니_권별대여_매출액
	String iAppOnemngrNum;					//i앱머니_권별소장_건
	String iAppOnemngrUse;					//i앱머니_권별소장_이용권
	String iAppOnemngrPayment;				//i앱머니_권별소장_매출액
	String iAppAllrentalNum;				//i앱머니_전권대여_건
	String iAppAllrentalUse;				//i앱머니_전권대여_이용권
	String iAppAllrentalPayment;			//i앱머니_전권대여_매출액
	String iAppAllmngrNum;					//i앱머니_전권소장_건
	String iAppAllmngrUse;					//i앱머니_전권소장_이용권
	String iAppAllmngrPayment;				//i앱머니_전권소장_매출액
	String amountNum;						//계_권수
	String amountUse;						//계_이용권
	String amountPayment;					//계_매출액
	String amountSettlement;				//계_정산액
	String sales;							//매출액
	String payment;							//정산액
	
	String writerId;						//작가id
	String setDate;							//기준월

}
