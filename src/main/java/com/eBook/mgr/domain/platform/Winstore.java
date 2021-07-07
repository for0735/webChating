package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Winstore {
	String idx;
	
	String partnerName;							//파트너명
	String partnerId;							//파트너ID
	String entrepreneur;						//사업자구분
	String channelProductId;					//채널상품ID
	String channelProductName;					//채널상품명
	String productName;							//상품명
	String episode;								//회차
	String productId;							//상품ID
	String secondSettlementStatus;				//2차정산여부
	String category;							//카테고리
	String brand;								//출판사
	String author;								//글작가
	String pictureAuthor;						//그림작가
	String type;								//타입
	String saleRate;							//판매건수
	String cencelRate;							//취소건수
	String settlementTargetAmount;				//정산대상액
	String salesRate;							//판매건수
	String salesAmount;							//판매금액
	String cencelsRate;							//취소건수
	String cencelsAmount;						//취소금액
	String sum;									//합계
	String customerPayment;						//결제수단_고객결제
	String customerPaymentReceipt;				//결제수단_고객결제(현금영수증)
	String customerPaymentEtc;					//결제수단_쿠폰/포인트등
	String customerPaymentService;				//서비스이용료_고객결제 서비스이용료
	String customerPaymentServiceEtc;			//서비스이용료_쿠폰/포인트등액
	String serviceDeduction;					//서비스이용료_차감
	String payment;								//정산지급액
	String paymentType;							//정산유형
	String writerId;							//작가id
	String setDate;								//기준월
}
