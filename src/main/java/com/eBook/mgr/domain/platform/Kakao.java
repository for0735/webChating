package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Kakao {
	String idx;
	
	String brand;							//브랜드
	String specialGeneral2;					//특별/할인
	String entrepreneur;					//사업자명
	String entrepreneurId;					//사업자번호
	String publisher;						//발행자명
	String contractUid;						//계약UID
	String contractCategory;				//계약유형
	String contract;						//계약(과/면세)
	String newContract;						//당월신규계약여부
	String category;						//카테고리
	String seriesId;						//시리즈ID
	String productName;						//시리즈명
	String productCode;						//제품코드(발행자가 부여하는 코드)
	String lable;							//레이블
	String author;							//작가명
	String isIsbn;							//ISBN(유/무)
	String cpSettlementRateAndroid;			//CP정산율_안드로이드/이벤트캐시
	String cpSettlementRateIos;				//CP정산율_iOS
	String useTicketType;					//이용권 종류
	String useTicketAmount;					//이용권 금액
	String cacheSum;						//캐시합계
	String cpp1;							//CPP
	String cpl1;							//CPI
	String cp1_1;							//CP1
	String cp2_1;							//CP2
	String cp3_1;							//CP3
	String cp7_1;							//CP7
	String cp8_1;							//CP8
	String cp9_1;							//CP9
	String eventCache1;						//이벤트캐시
	String originalSum;						//원화합계
	String cpp2;							//CPP
	String cpl2;							//CPI
	String cp1_2;							//CP1
	String cp2_2;							//CP2
	String cp3_2;							//CP3
	String cp7_2;							//CP7
	String cp8_2;							//CP8
	String cp9_2;							//CP8
	String eventCache2;						//이벤트캐시
	String valueSupply;						//공급가액
	String taxAmount;						//세액
	String payment;							//합계금액
	String writerId; 						//작가id
	String setDate; 							//기준월
}
