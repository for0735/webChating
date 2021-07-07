package com.eBook.mgr.domain;

import lombok.Data;


@Data
public class Author {
	String id;
	String author;							// 작가명
	String accountNumber;					// 계좌번호
	String virtuousTax;						// 선인세
	String settlementRatio;					// 정산비율
	String writerId;						// 작가ID
	String carryAmount;						// 이월정산액
}
