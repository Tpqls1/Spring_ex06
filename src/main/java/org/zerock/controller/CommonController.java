package org.zerock.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class CommonController {
	@GetMapping("/accessError")
	public void accessDenied(Authentication auth, Model model) {
		model.addAttribute("msg", "이 페이지는 접근할 수 없는 페이지입니다.");
	}
	
	//	애플리케이션에서 정의한 페이지를 사용 
	//	error, logout : 여러가지 경우에 의하여 로그인 페이지로 올 수가 있다. 
	@GetMapping("/customLogin")
	public void loginInput(String error, String logout, Model model) {
		log.info("error: " + error);
		log.info("logout: " + logout);
		
		if(error != null) {
			model.addAttribute("error","Login Error Check Your Account");
			
		}
		
		if(logout != null) {
			model.addAttribute("logout","Logout!!!");
		}
		//	url 페이지 : /customLogin.jsp
	}
}
