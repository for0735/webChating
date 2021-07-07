package com.eBook.mgr.domain.platform;

import java.util.Date;

import lombok.Data;

@Data
public class Naver {
	String idx;
	
	String productName;									//상품명
	String productNo;									//컨텐츠No
	String providerCode;								//공급자코드
	String cpName;										//CP명
	String author;										//저자명
	String brand;										//출판사
	String ageLimit;									//나이제한
	String serviceDay;									//서비스일
	String rentalNumber;								//대여권_건수
	String rentalNonpy;									//대여권_비정산
	String rentalFree;									//대여권_무료이용권
	String rentalPaidCookie;							//대여권_유상쿠키
	String rentalFreeCookie;							//대여권_무상쿠키
	String rentalPaidICookie;							//대여권_유상i쿠키
	String rentalFreeICookie;							//대여권_무상i쿠키
	String ownNumber;									//소장권_건수
	String ownNonpy;									//소장권_비정산품명
	String ownFree;										//소장권_무료이용권
	String ownPaidCookie;								//소장권_유상쿠키
	String ownFreeCookie;								//소장권_무상쿠키
	String ownPaidICookie;								//소장권_유상i쿠키
	String ownFreeICookie;								//소장권_무상i쿠키
	String cookieRentalNumber;							//쿠키로대여_건수
	String cookieRentalPaid;							//쿠키로대여_유상
	String cookieRentalFree;							//쿠키로대여_무상
	String cookieOwnNumber;								//쿠키로구매_건수
	String cookieOwnPaid;								//쿠키로구매_유상
	String cookieOwnFree;								//쿠키로구매_무상
	String iCookieRentalNumber;							//i쿠키로대여_건수
	String iCookieRentalPaid;							//i쿠키로대여_유상
	String iCookieRentalFree;							//i쿠키로대여_무상
	String iCookieOwnNumber;							//i쿠키로구매_건수
	String iCookieOwnPaid;								//i쿠키로구매_유상
	String iCookieOwnFree;								//i쿠키로구매_무상
	String iCookieFee;									//i쿠키 수수료
	String sum;											//합계
	String payment;										//출판사 정산
	String writerId;									//작가id
	String setDate;										//기준월
}
