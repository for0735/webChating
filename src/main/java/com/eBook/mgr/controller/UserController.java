package com.eBook.mgr.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eBook.mgr.domain.Member;
import com.eBook.mgr.domain.platform.Bookcube;
import com.eBook.mgr.dto.AuthorListDto;
import com.eBook.mgr.service.MemberService;

@Controller
@RequestMapping("/ebook")
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	MemberService memberService;
	
	// 회원관리
	@RequestMapping(value = "/userList", method = RequestMethod.GET)
	public void userList(Model model) {
		
		try {
			List<AuthorListDto> memberList = memberService.list();
			model.addAttribute("memberList", memberList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
