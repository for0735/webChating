package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Tocsoda {
	String idx;
	
	String productBacord;					//상품바코드
	String largeCategory;					//대분류
	String productName;						//작품명
	String author;							//작가명
	String brand;							//출판사
	String pc;								//PC
	String android;							//안드로이드
	String ios;								//IOS
	String iosIap;							//IOS-IAP
	String sum;								//합계
	String payment;							//정산액
	String writerId;						//작가id
	String setDate;							//기준월
}
