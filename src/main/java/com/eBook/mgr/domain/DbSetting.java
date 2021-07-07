package com.eBook.mgr.domain;

import lombok.Data;

@Data
public class DbSetting {
	String idx;

	String productName;						//제목
	String author;							//작가
	String brand;							//출판권자
	String netPrice;						//정가
	String manager;							//담당자
	
	String writerId;						//작가id
}
