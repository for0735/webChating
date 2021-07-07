package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Bookcube {
	String idx;
	
	String productName;						//제목
	String author;							//저자
	String brand;							//출판권자
	String salesCategory;					//판매처
	String salesType;						//판매유형
	String episode;							//부수
	String netPrice;						//정가
	String salePrice;						//판매액
	String discount;						//할인
	String fee;								//수수료
	String netTargetPrice;					//정산대상금액
	String deductionReservesEvent;			//적립금 이벤트 차감
	String payment;							//정산액
	String bookId;							//북넘버
	String isbn;							//isbn
	String eIsbn;							//e_isbn
	String eIsbnEpub;						//e-ISBN(ePub)
	String writerId;						//작가id
	String setDate;							//기준월
}
