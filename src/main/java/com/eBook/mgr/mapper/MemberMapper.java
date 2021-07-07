package com.eBook.mgr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.eBook.mgr.domain.Author;
import com.eBook.mgr.domain.Member;
import com.eBook.mgr.domain.MemberAuth;
import com.eBook.mgr.dto.AuthorListDto;

@Mapper
public interface MemberMapper {
	//회원 등록
	public void createMember(Member member) throws Exception;
	public void createAuthor(Author author) throws Exception;

	//회원 리스트조회
	public List<AuthorListDto> list() throws Exception;
	public String[] listId() throws Exception;
	public Member listMember(String id) throws Exception;
	public String findRealName(String id) throws Exception;
	
	
	//회원 업데이트
	public void updateMember(Member member) throws Exception;
	public void updateAuthor(Author author) throws Exception;
	
	
	//회원 삭제
	public void deleteMember(String id) throws Exception;
	public void deleteAuthor(String writerId) throws Exception;
	public String[] readId(String id) throws Exception;
	public String readWriterId(String writerId) throws Exception;
	
	
	//회원 활성/비활성
	public void useLogin(String id) throws Exception;
	public void unUseLogin(String id) throws Exception;
	public String readStatus(String id) throws Exception;
	
	
	//중복된 회원 찾기
	public boolean findId(String id) throws Exception;
	
	
	
	public void createAuth(MemberAuth memberAuth);
	
	public Member readByUserId(String userId);
	
	
	
	
	public Member read(String userId) throws Exception;
}