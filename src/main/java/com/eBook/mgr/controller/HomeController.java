package com.eBook.mgr.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eBook.mgr.domain.Board;
import com.eBook.mgr.service.BoardService;
import com.eBook.mgr.service.MemberService;

@Controller
public class HomeController {

	@Autowired
	MemberService memberService;
	
	@Autowired
	BoardService boardService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER')")
	public String home(Locale locale, Model model, Principal principal) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		try {
			Board board = new Board();
			board = boardService.mainRead();
			
			model.addAttribute("main_title", board.getTitle());
			model.addAttribute("main_content", board.getContent());
			model.addAttribute("serverTime", formattedDate);
			model.addAttribute("real_name", memberService.findRealName(principal.getName()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "index";
	}
	
	@RequestMapping(value = "/notice2", method = RequestMethod.GET)
	public String notice(Model model) throws Exception{
		
		return "/board/notice";
	}
}
