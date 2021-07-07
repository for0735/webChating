package com.eBook.mgr.dto;

import lombok.Data;

@Data
public class PaymentDto {
	
	int idx;
	String author;
	String realName;						// 본명
	String ctzNumber;						// 주민번호
	String accountNumber;					// 계좌번호
	
	String payBookcube;
	String payEpub;
	String payJoara;
	String payKakao;
	String payKyobo;
	String payMrblue;
	String payMunpia;
	String payNaver;
	String payRidibooks;
	String payRomance;
	String payTocsoda;
	String payWinstore;
	String payYes24;
	String payAladin;
	
	String allPayment;						// 총매출
	String settlementRatio;					// 정산비율
	String authorSettlement;				// 작가정산액
	String carryAmount;						// 이월정산액
	String payment;							// 정산액합계
	String virtuousTax;						// 선인세
	String incomeTax;						// 소득세
	String wthldTax;						// 원천징수세
	String authorPaid;						// 작가지급액
	String writerId;						// 작가ID
	
	String setDate;							//기준월
}
