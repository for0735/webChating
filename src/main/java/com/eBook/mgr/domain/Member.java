package com.eBook.mgr.domain;

import java.util.Date;
import java.util.List;


import lombok.Data;

@Data
public class Member {	
	int idx;								// 기본추가 번호
	String id;								// ID
	String pw;								// PW
	String realName;						// 본명
	String ctzNumber;						// 주민번호
	private Boolean loginStatus;			// 로그인 활성/비활성
	String auth;							// 권한
	String manager;							// 담당자
	int authNumber;							// 권한번호: 0 : 사용자, 1 :  관리자
	
	private Date regDate;
	private Date updDate;
	
	private List<MemberAuth> authList;
	

//	String author;							// 작가명
//	String accountNumber;					// 계좌번호
//	String virtuousTax;						// 선인세
//	String settlementRatio;					// 정산비율
}




