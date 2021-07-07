package com.eBook.mgr.common.security;

import com.eBook.mgr.common.security.domain.CustomUser;
import com.eBook.mgr.domain.Member;
import com.eBook.mgr.mapper.MemberMapper;
import com.eBook.mgr.service.MemberService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private MemberService memberService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		

		logger.warn("Load User By UserName : " + userName);
		try {
			String state = memberService.readStatus(userName);
			System.out.println("로그인 스테이트 : " + state);
			
			if(state.equals("1")) {
				Member member = memberMapper.readByUserId(userName);
				
				logger.warn("queried by member mapper: " + member);

				return member == null ? null : new CustomUser(member);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	} 

}
