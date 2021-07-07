package com.eBook.mgr.domain;

import java.util.Date;

import lombok.Data;

@Data
public class Board {
	private int boardNo;		// 게시판번호
	private String title;		// 제목
	private String content;		// 내용
	private String writer;		// 작성자
	private Date regDate;		// 등록일
	private String useUpdate; 		// 공지로등록 true가 등록 "ㅇ" 체크되어있는것이 공지
}
