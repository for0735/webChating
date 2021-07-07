package com.eBook.mgr.service;

import java.util.List;

import com.eBook.mgr.domain.Author;
import com.eBook.mgr.domain.Member;
import com.eBook.mgr.dto.AuthorListDto;

public interface MemberService {
	public void registerMember(Member member) throws Exception;
	public void registerAuthor(Author author) throws Exception;
	
	public void modifyAuthorDto(Member member, Author author) throws Exception;
	
	public void removeAuthorDto(String id, String writerId) throws Exception;
	public String readWriterId(String writerId) throws Exception;
	
	
	public void status(String id, Boolean loginStatus) throws Exception;
	public String readStatus(String id) throws Exception;
	
	
	public List<AuthorListDto> list() throws Exception;
	public String[] listId() throws Exception;
	public Member listMember(String id) throws Exception;
	public String findRealName(String id) throws Exception;
	
	
	public boolean findId(String id) throws Exception;
	
	
	public Member read(String userId) throws Exception;
	
	
}
