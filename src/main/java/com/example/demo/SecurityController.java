package com.example.demo;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController{

	@Autowired
	HttpSession session;

	/**
	  *ログインなしでアクセスできないように
	 */

	public ModelAndView security(ModelAndView mv) {
		if(session.getAttribute("securityCode") == null) {
			mv.addObject("error", "セッションが無効です。ログインし直してください。");
			mv.setViewName("login");
		}
		return mv;

	}


}
