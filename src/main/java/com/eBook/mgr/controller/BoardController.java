package com.eBook.mgr.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.eBook.mgr.domain.Board;
import com.eBook.mgr.service.BoardService;



@Controller
@RequestMapping("/board")
public class BoardController {
	
	@Autowired
	private BoardService service;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public void list(Model model) throws Exception{
		model.addAttribute("board", new Board());
		model.addAttribute("list", service.list());
		
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(String title, Model model) throws Exception{
		Board board = new Board();
		board.setTitle(title);
		
		model.addAttribute("board", board);
		
		model.addAttribute("list", service.search(title));
		
		return "/board/list";
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public void registerForm(Board board, Model model, Principal principal) throws Exception {
		model.addAttribute("getName", principal.getName());
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String register(Board board, Model model, Principal principal, String useids) throws Exception{
		board.setWriter(principal.getName());
		service.register(board);
		
		System.out.println("공지" + useids);
		System.out.println("번호" + service.lastNumber());
		
		if(useids.equals("use")) {
			service.remove_use();
			service.select_use(service.lastNumber());
		}
		
		model.addAttribute("msg", "등록이 완료되었습니다.");
		
		return "redirect:list";
		
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public void modify(int boardNo, Board board, Model model) throws Exception{
		model.addAttribute(service.read(boardNo));
	}
	
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(Board board, Model model, String useids) throws Exception {
		
		service.modify(board);

		System.out.println("공지" + useids);

		if(useids.equals("use")) {
			service.remove_use();
			service.select_use(board.getBoardNo());
		}
		
		model.addAttribute("msg", "수정이 완료되었습니다.");
		
		return "redirect:list";
	}
	
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public String remove(@RequestParam("boardNo") int boardNo, Model model) throws Exception{
		service.remove(boardNo);
		
		model.addAttribute("msg", "삭제가 완료되었습니다");
		
		return "redirect:list";
	}
	
}
