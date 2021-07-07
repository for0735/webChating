package com.eBook.mgr.dto;

import java.util.Date;
import java.util.List;

import com.eBook.mgr.domain.MemberAuth;

import lombok.Data;

@Data
public class AuthorListDto {		
	int idx;				
	String writerId;
	String id;			
	String author;					
	String realName;			
	String accountNumber;				
	String ctzNumber;				
	String virtuousTax;		
	private Boolean loginStatus;		
	String settlementRatio;							
	String manager;	
	String carryAmount;	

	private Date regDate;
	private Date updDate;
	
	private List<MemberAuth> authList;			 
}
