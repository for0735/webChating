package com.eBook.mgr.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {
	
	private static final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);
	
	@ExceptionHandler(Exception.class)
	public String handle(Exception e, Model model) {
		log.info(e.toString());
		
		model.addAttribute("exception", e);
		return "error/errorCommon";
	}

}
