package com.eBook.mgr.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eBook.mgr.domain.Author;
import com.eBook.mgr.domain.Member;
import com.eBook.mgr.domain.MemberAuth;
import com.eBook.mgr.dto.AuthorListDto;
import com.eBook.mgr.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);
	
	@Autowired
	private MemberMapper mapper;

	@Override
	public void registerMember(Member member) throws Exception {
		mapper.createMember(member);
		
		String auth = "";
		if( member.getAuthNumber() == 0 )
			auth = "ROLE_USER";
		if( member.getAuthNumber() == 1 )
			auth = "ROLE_ADMIN";
		
		// member 테이블에 삽인한 후, idx 조회
		member = mapper.read(member.getId());
		
		MemberAuth memberAuth = new MemberAuth();
		memberAuth.setIdx(member.getIdx());
		memberAuth.setAuth(auth); 

		log.info("id : " + member.getId());
		log.info("본명 : " + member.getRealName());
		log.info("권한 : " + memberAuth.getAuth());
		
		
		mapper.createAuth(memberAuth);
	}

	@Override
	public String findRealName(String id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.findRealName(id);
	}

	@Override
	public void modifyAuthorDto(Member member, Author author) throws Exception {
		// TODO Auto-generated method stub
		mapper.updateAuthor(author);
		mapper.updateMember(member);
	}

	@Override
	public void removeAuthorDto(String id, String writerId) throws Exception {
		// TODO Auto-generated method stub
		mapper.deleteAuthor(writerId);
		
		String isNull = "notNull";
		String[] useId = mapper.readId(id);
		
		try {
			log.info("서비스단아이디값?? : " + useId[0]);
		} catch (Exception e) {
			// TODO: handle exception
			isNull = null;
		}
		
		if(isNull != null) {
			return;
		} else {
			log.info("널값찍혔음!");
			mapper.deleteMember(id);
		}
		
	}

	@Override
	public String readWriterId(String writerId) throws Exception {
		// TODO Auto-generated method stub
		return mapper.readWriterId(writerId);
	}

	@Override
	public void registerAuthor(Author author) throws Exception {
		// TODO Auto-generated method stub
		mapper.createAuthor(author);
	}

	@Override
	public void status(String id, Boolean loginStatus) throws Exception {
		// TODO Auto-generated method stub
		if(loginStatus == true) {
			mapper.unUseLogin(id);
		} else {
			mapper.useLogin(id);
		}
	}

	@Override
	public String readStatus(String id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.readStatus(id);
	}
	
	
	@Override
	public boolean findId(String id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.findId(id);
	}
	
	
	

	@Override
	public List<AuthorListDto> list() throws Exception {
		return mapper.list();
	}

	@Override
	public String[] listId() throws Exception {
		// TODO Auto-generated method stub
		return mapper.listId();
	}

	@Override
	public Member listMember(String id) throws Exception {
		// TODO Auto-generated method stub
		return mapper.listMember(id);
	}

	@Override
	public Member read(String userId) throws Exception {
		return mapper.read(userId);
	}
	
	
}
